package abyssal.abyssal_domain.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import static abyssal.abyssal_domain.Abyssal_domain.MOD_ID;

public class ModPackets {

    public static final Identifier FAKE_BORDER =
            new Identifier("abyssal_domain", "fake_border");

    public static final Identifier CLEAR_FAKE_BORDER =
            new Identifier("abyssal_domain", "clear_fake_border");

    public static final Identifier CHAIN_EFFECTS =
            new Identifier("abyssal_domain", "chain_effects");

    public static final Identifier CHAIN_TICK =
            new Identifier("abyssal_domain", "chain_tick");

    public static final Identifier CLEAR_CHAIN_EFFECTS =
            new Identifier("abyssal_domain", "clear_chain_effects");

    public static final Identifier JUDGMENT_EFFECT =
            new Identifier("abyssal_domain", "judgment_effect");

    public static final Identifier SHIELD_PACKET =
            new Identifier("abyssal_domain", "shield_packet");

    public static final Identifier CLEAR_SHIELD =
            new Identifier("abyssal_domain", "clear_shield");

    public static final Identifier JUDGMENT_SYNC =
            new Identifier("abyssal_domain", "judgment_sync");

    public static final Identifier CUSTOM_DEATH =
            new Identifier("abyssal_domain", "custom_death");

    public static final Identifier GRAPPLE_SWING =
            new Identifier("abyssal_domain", "grapple_swing");

    public static final Identifier FROZEN_PROJECTILE =
            new Identifier("abyssal_domain", "frozen_projectile");

    public static final Identifier SHADER_EFFECT =
            new Identifier("abyssal_domain", "shader_effect");
    
    public static final Identifier SHADER_WORLD_EFFECT =
            new Identifier("abyssal_domain", "shader_world_effect");

    public static void sendShieldPacket(ServerPlayerEntity player, BlockPos center, int radius, int height) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(center.getX());
        buf.writeInt(center.getY());
        buf.writeInt(center.getZ());
        buf.writeInt(radius);
        buf.writeInt(height);
        ServerPlayNetworking.send(player, SHIELD_PACKET, buf);
    }

    public static void sendClearShield(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        ServerPlayNetworking.send(player, CLEAR_SHIELD, buf);
    }

    public static void sendJudgmentSync(ServerPlayerEntity player, int targetId, boolean isCaster, int timer) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(targetId);
        buf.writeBoolean(isCaster);
        buf.writeInt(timer);
        ServerPlayNetworking.send(player, JUDGMENT_SYNC, buf);
    }

    public static void sendCustomDeath(ServerPlayerEntity player, String message) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(message);
        ServerPlayNetworking.send(player, CUSTOM_DEATH, buf);
    }

    public static void sendFrozenProjectile(ServerPlayerEntity player, int x, int y, int z) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        ServerPlayNetworking.send(player, FROZEN_PROJECTILE, buf);
    }
    
    public static void sendShaderEffect(ServerPlayerEntity player, String shaderName) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(shaderName);
        ServerPlayNetworking.send(player, SHADER_EFFECT, buf);
    }
    
    public static void sendShaderWorldEffect(ServerPlayerEntity player, double x, double y, double z) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        ServerPlayNetworking.send(player, SHADER_WORLD_EFFECT, buf);
    }
}
