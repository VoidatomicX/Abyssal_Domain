package abyssal.abyssal_domain;

import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.entity.custom.GoobichthysEntity;
import abyssal.abyssal_domain.item.ModItemGroups;
import abyssal.abyssal_domain.item.ModItems;
import abyssal.abyssal_domain.util.BorderZoneManager;
import abyssal.abyssal_domain.util.ModSpawns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Abyssal_domain implements ModInitializer {
    public static final String MOD_ID = "abyssal_domain";
public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        ModItemGroups.registerItemGroups();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModSpawns.register();

        FabricDefaultAttributeRegistry.register(ModEntities.GOOBICHTHYS, GoobichthysEntity.createGoobichthyAttributes());

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            BorderZoneManager.tick(server);
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

    }
}
