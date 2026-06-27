package abyssal.abyssal_domain;

import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.command.ShaderCommands;
import abyssal.abyssal_domain.enchants.ModEnchantments;
import abyssal.abyssal_domain.effect.ModEffects;
import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.entity.custom.GoobichthysEntity;
import abyssal.abyssal_domain.item.ModItemGroups;
import abyssal.abyssal_domain.item.ModItems;
import abyssal.abyssal_domain.item.custom.GrapplingHook;
import abyssal.abyssal_domain.item.custom.Terminus_Est;
import abyssal.abyssal_domain.util.BorderZoneManager;
import abyssal.abyssal_domain.util.ModLootTableModifiers;
import abyssal.abyssal_domain.util.ShieldZoneManager;
import abyssal.abyssal_domain.util.ModSpawns;
import abyssal.abyssal_domain.world.ModConfiguredFeatures;
import abyssal.abyssal_domain.world.ModOrePlacement;
import abyssal.abyssal_domain.world.gen.ModOreGeneration;
import abyssal.abyssal_domain.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.literal;

public class Abyssal_domain implements ModInitializer {
    public static final String MOD_ID = "abyssal_domain";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static boolean commandsRegistered = false;

    @Override
    public void onInitialize() {

        ModItemGroups.registerItemGroups();
        ModEnchantments.register();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModSpawns.register();
        ModLootTableModifiers.modifyLootTables();
        ModEffects.registerEffects();



        FabricDefaultAttributeRegistry.register(ModEntities.GOOBICHTHYS, GoobichthysEntity.createGoobichthyAttributes());

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            BorderZoneManager.tick(server);

            if (!commandsRegistered && server.getPlayerManager().getPlayerList().size() > 0) {
                commandsRegistered = true;
                var dispatcher = server.getCommandManager().getDispatcher();
                abyssal.abyssal_domain.command.ShaderCommands.register(dispatcher);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ShieldZoneManager.tick(server);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                for (int i = 0; i < player.getInventory().size(); i++) {
                    var stack = player.getInventory().getStack(i);
                    if (stack.getItem() == ModItems.Grappling_Hook) {
                        GrapplingHook.tickHook(player.getWorld(), player, stack);
                    }
                    if (stack.getItem() == ModItems.Voruna) {
                        abyssal.abyssal_domain.item.custom.trident.VorunaItem.tickVoruna(player.getWorld(), player, stack);
                    }
                }
            });
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (!(entity instanceof ServerPlayerEntity)) return;
            if (!(source.getAttacker() instanceof ServerPlayerEntity killer)) return;

            for (int i = 0; i < killer.getInventory().size(); i++) {
                var stack = killer.getInventory().getStack(i);
                if (stack.getItem() == ModItems.Terminus_Est) {
                    Terminus_Est.addEnergyOnKill(stack);
                    break;
                }
            }
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (!world.getRegistryKey().getValue().getPath().equals("abyssal_dimension")) return;

            world.iterateEntities().forEach(entity -> {
                if (entity instanceof LivingEntity living) {

                    Vec3d vel = living.getVelocity();

                    if (!living.isOnGround()) {
                        living.setVelocity(vel.x, vel.y + 0.04, vel.z);
                    }
                }
            });
        });

        StrippableBlockRegistry.register(ModBlocks.Crepe_Myrtle_Log, ModBlocks.Stripped_Crepe_Myrtle_Log);
        StrippableBlockRegistry.register(ModBlocks.Crepe_Myrtle_Wood, ModBlocks.Stripped_Crepe_Myrtle_Wood);

        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.Crepe_Myrtle_Log, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.Crepe_Myrtle_Wood, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.Stripped_Crepe_Myrtle_Log, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.Stripped_Crepe_Myrtle_Wood, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.Crepe_Myrtle_PLANKS.planks, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.Crepe_Myrtle_Leaves, 30, 60);

        ModWorldGeneration.generateModWorldGen();

    }
}
