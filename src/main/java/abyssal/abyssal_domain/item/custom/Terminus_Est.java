package abyssal.abyssal_domain.item.custom;

import abyssal.abyssal_domain.network.ModPackets;
import abyssal.abyssal_domain.util.BorderZone;
import abyssal.abyssal_domain.util.BorderZoneManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Terminus_Est extends SwordItem {

    private static final int BORDER_RADIUS = 10;
    private static final int COOLDOWN_TICKS = 20; // 1 second
    private static final int RECHARGE_TICKS = 3200; // 1 minute = 60s * 20 ticks

    public Terminus_Est(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof ServerPlayerEntity player)) return super.postHit(stack, target, attacker);
        if (!(target instanceof ServerPlayerEntity targetPlayer)) return super.postHit(stack, target, attacker);

        // Initialize NBT if not present
        if (!stack.hasNbt()) stack.setNbt(new net.minecraft.nbt.NbtCompound());
        var nbt = stack.getNbt();
        if (!nbt.contains("Charge")) nbt.putBoolean("Charge", true);
        if (!nbt.contains("RechargeTimer")) nbt.putInt("RechargeTimer", 0);

        boolean hasCharge = nbt.getBoolean("Charge");
        int rechargeTimer = nbt.getInt("RechargeTimer");
        World world = attacker.getEntityWorld();

        // Only allow ability if charge is available and not on cooldown
        if (!hasCharge || player.getItemCooldownManager().isCoolingDown(this)) return super.postHit(stack, target, attacker);

        // Consume charge
        nbt.putBoolean("Charge", false);

        // Start 1-second cooldown for the attack
        player.getItemCooldownManager().set(this, COOLDOWN_TICKS);

        // Apply BorderZone logic
        BorderZone zone = new BorderZone(player, targetPlayer, target.getBlockPos(), BORDER_RADIUS);
        BorderZoneManager.addZone(zone);

        sendFakeBorder(player, target.getBlockPos(), BORDER_RADIUS);
        sendFakeBorder(targetPlayer, target.getBlockPos(), BORDER_RADIUS);

        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        if (!(entity instanceof ServerPlayerEntity)) return;

        if (!stack.hasNbt()) stack.setNbt(new net.minecraft.nbt.NbtCompound());
        var nbt = stack.getNbt();
        if (!nbt.contains("Charge")) nbt.putBoolean("Charge", true);
        if (!nbt.contains("RechargeTimer")) nbt.putInt("RechargeTimer", 0);

        boolean hasCharge = nbt.getBoolean("Charge");
        int rechargeTimer = nbt.getInt("RechargeTimer");

        // Only count recharge if charge is spent
        if (!hasCharge) {
            rechargeTimer++;
            if (rechargeTimer >= RECHARGE_TICKS) {
                nbt.putBoolean("Charge", true); // recharge
                rechargeTimer = 0;
            }
            nbt.putInt("RechargeTimer", rechargeTimer);
        }
    }

    private void sendFakeBorder(ServerPlayerEntity player, BlockPos center, int radius) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(center.getX());
        buf.writeInt(center.getY());
        buf.writeInt(center.getZ());
        buf.writeInt(radius);

        ServerPlayNetworking.send(player, ModPackets.FAKE_BORDER, buf);
    }

    // ---- ITEM BAR METHODS ----

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        boolean hasCharge = stack.hasNbt() && stack.getNbt().getBoolean("Charge");
        return hasCharge ? 0 : 13; // Full bar = available, empty = spent
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xff9300; // Orange
    }
}