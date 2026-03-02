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

public class Terminus_Est extends SwordItem {

    private static final int BORDER_RADIUS = 10;

    public Terminus_Est(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof ServerPlayerEntity player)) return super.postHit(stack, target, attacker);
        if (!(target instanceof ServerPlayerEntity targetPlayer)) return super.postHit(stack, target, attacker);

        BorderZone zone = new BorderZone(player, targetPlayer, target.getBlockPos(), BORDER_RADIUS);
        BorderZoneManager.addZone(zone);

        sendFakeBorder(player, target.getBlockPos(), BORDER_RADIUS);
        sendFakeBorder(targetPlayer, target.getBlockPos(), BORDER_RADIUS);

        return super.postHit(stack, target, attacker);
    }

    private void sendFakeBorder(ServerPlayerEntity player, BlockPos center, int radius) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(center.getX());
        buf.writeInt(center.getY());
        buf.writeInt(center.getZ());
        buf.writeInt(radius);

        ServerPlayNetworking.send(player, ModPackets.FAKE_BORDER, buf);
    }
}