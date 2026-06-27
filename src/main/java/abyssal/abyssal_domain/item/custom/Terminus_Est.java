package abyssal.abyssal_domain.item.custom;

import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.entity.custom.TerminusShieldEntity;
import abyssal.abyssal_domain.network.ModPackets;
import abyssal.abyssal_domain.util.ShieldZone;
import abyssal.abyssal_domain.util.ShieldZoneManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class Terminus_Est extends SwordItem {

    private static final int BORDER_RADIUS = 10;
    private static final int BORDER_HEIGHT = 10;
    private static final int MAX_ENERGY = 100;
    private static final int ENERGY_PER_KILL = 25;
    private static final int SHIELD_ACTIVATION_COST = 50;
    private static final int SHIELD_TICK_COST = 1;

    public static void addEnergyOnKill(ItemStack stack) {
        if (!(stack.getItem() instanceof Terminus_Est)) return;
        NbtCompound nbt = stack.getOrCreateNbt();
        int energy = nbt.getInt("Energy");
        energy = Math.min(energy + ENERGY_PER_KILL, MAX_ENERGY);
        nbt.putInt("Energy", energy);
    }

    public Terminus_Est(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        NbtCompound nbt = getOrCreateNbt(stack);
        int energy = nbt.getInt("Energy");

        if (energy < SHIELD_ACTIVATION_COST) {
            if (world.isClient) {
                user.sendMessage(Text.literal("Not enough energy!").formatted(Formatting.RED), true);
            }
            return TypedActionResult.fail(stack);
        }

        if (world.isClient) return TypedActionResult.consume(stack);

        if (!(user instanceof ServerPlayerEntity player)) return TypedActionResult.fail(stack);

        TerminusShieldEntity existing = getPlayerShield(player);
        if (existing != null) {
            existing.discard();
        }

        energy -= SHIELD_ACTIVATION_COST;
        nbt.putInt("Energy", energy);

        TerminusShieldEntity shield = new TerminusShieldEntity(ModEntities.TERMINUS_SHIELD, world);
        shield.setPosition(player.getPos().add(0, 1.0, 0));
        shield.setOwner(player);
        shield.setShieldHp(50);
        shield.startHolding();
        world.spawnEntity(shield);

        return TypedActionResult.consume(stack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (world.isClient) return;
        if (!(user instanceof ServerPlayerEntity player)) return;

        TerminusShieldEntity shield = getPlayerShield(player);
        if (shield != null) {
            shield.discard();
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof ServerPlayerEntity player)) return super.postHit(stack, target, attacker);
        if (!(target instanceof ServerPlayerEntity targetPlayer)) return super.postHit(stack, target, attacker);

        BlockPos pos = target.getBlockPos().add(0, 1, 0);
        ShieldZone zone = new ShieldZone(player, targetPlayer, pos, BORDER_RADIUS);
        zone.setDurability(100);
        ShieldZoneManager.addZone(zone);

        sendShieldPacket(player, pos, BORDER_RADIUS, BORDER_HEIGHT);
        sendShieldPacket(targetPlayer, pos, BORDER_RADIUS, BORDER_HEIGHT);

        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        if (!(entity instanceof ServerPlayerEntity player)) return;

        NbtCompound nbt = getOrCreateNbt(stack);
        int energy = nbt.getInt("Energy");

        TerminusShieldEntity shield = getPlayerShield(player);
        if (shield != null && shield.isHolding()) {
            if (player.getActiveItem() != stack || !player.isUsingItem()) {
                shield.discard();
            } else if (world.getTime() % 20 == 0) {
                energy -= SHIELD_TICK_COST;
                if (energy <= 0) {
                    energy = 0;
                    nbt.putInt("Energy", energy);
                    shield.discard();
                    player.stopUsingItem();
                } else {
                    nbt.putInt("Energy", energy);
                }
            }
        }

        if (selected) {
            shield = getPlayerShield(player);
            if (shield != null && shield.isHolding()) {
                player.sendMessage(Text.literal(
                    "Energy: " + energy + "  |  Shield: " + shield.getShieldHp() + "/" + shield.getMaxShieldHp()
                ).formatted(Formatting.GOLD), true);
            } else {
                player.sendMessage(Text.literal(
                    "Energy: " + energy + "/" + MAX_ENERGY
                ).formatted(Formatting.GOLD), true);
            }
        }
    }

    private TerminusShieldEntity getPlayerShield(PlayerEntity player) {
        for (Entity e : player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(5.0))) {
            if (e instanceof TerminusShieldEntity shield && shield.getOwnerUuid() != null && shield.getOwnerUuid().equals(player.getUuid())) {
                return shield;
            }
        }
        return null;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        NbtCompound nbt = getOrCreateNbt(stack);
        int energy = nbt.getInt("Energy");
        return 13 * energy / MAX_ENERGY;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        NbtCompound nbt = getOrCreateNbt(stack);
        int energy = nbt.getInt("Energy");
        if (energy < 25) return 0xff0000;
        if (energy < 50) return 0xff9300;
        return 0x00ff00;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, net.minecraft.client.item.TooltipContext context) {
        NbtCompound nbt = getOrCreateNbt(stack);
        int energy = nbt.getInt("Energy");

        tooltip.add(Text.literal("Energy: " + energy + "/" + MAX_ENERGY).formatted(Formatting.GOLD));
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Hit a player to trap you both in an arena").formatted(Formatting.DARK_GREEN));
        tooltip.add(Text.literal("Hold right-click for a personal barrier (costs " + SHIELD_ACTIVATION_COST + " energy)").formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.literal("Kill players to gain " + ENERGY_PER_KILL + " energy").formatted(Formatting.GRAY));
    }

    private NbtCompound getOrCreateNbt(ItemStack stack) {
        if (!stack.hasNbt()) stack.setNbt(new NbtCompound());
        var nbt = stack.getNbt();
        if (!nbt.contains("Energy")) nbt.putInt("Energy", 0);
        return nbt;
    }

    private void sendShieldPacket(ServerPlayerEntity player, BlockPos center, int radius, int height) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(center.getX());
        buf.writeInt(center.getY());
        buf.writeInt(center.getZ());
        buf.writeInt(radius);
        buf.writeInt(height);
        ServerPlayNetworking.send(player, ModPackets.SHIELD_PACKET, buf);
    }
}
