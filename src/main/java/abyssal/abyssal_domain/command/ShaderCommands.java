package abyssal.abyssal_domain.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.literal;

public class ShaderCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("shader")
            .requires(src -> src.hasPermissionLevel(2))
            .then(literal("apply")
                .executes(ctx -> applyShaderEffect(ctx, "abyssal_glow")))
            .then(literal("clear")
                .executes(ctx -> clearShaderEffect(ctx)))
            .then(literal("world")
                .executes(ctx -> triggerWorldEffect(ctx))));
    }
    
    private static int applyShaderEffect(CommandContext<ServerCommandSource> ctx, String shaderName) {
        ServerCommandSource source = ctx.getSource();
        
        if (source.getEntity() instanceof ServerPlayerEntity player) {
            player.sendMessage(Text.literal("Applied shader effect: " + shaderName).formatted(Formatting.GOLD));
            
            abyssal.abyssal_domain.network.ModPackets.sendShaderEffect(player, shaderName);
            
            return 1;
        }
        
        source.sendFeedback(() -> Text.literal("No player found"), false);
        return 0;
    }
    
    private static int clearShaderEffect(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        
        if (source.getEntity() instanceof ServerPlayerEntity player) {
            player.sendMessage(Text.literal("Cleared shader effects").formatted(Formatting.GREEN));
            abyssal.abyssal_domain.network.ModPackets.sendShaderEffect(player, "none");
            return 1;
        }
        
        return 0;
    }
    
    private static int triggerWorldEffect(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        Vec3d pos = source.getPosition();
        
        for (ServerPlayerEntity player : source.getServer().getPlayerManager().getPlayerList()) {
            if (player.squaredDistanceTo(pos) < 2500) {
                player.sendMessage(Text.literal(
                    "Shader effect triggered at " + (int)pos.x + ", " + (int)pos.y + ", " + (int)pos.z
                ).formatted(Formatting.LIGHT_PURPLE));
                
                abyssal.abyssal_domain.network.ModPackets.sendShaderWorldEffect(player, pos.x, pos.y, pos.z);
            }
        }
        
        source.getWorld().playSound(
            pos.getX(), pos.getY(), pos.getZ(),
            net.minecraft.sound.SoundEvents.BLOCK_PORTAL_TRIGGER, 
            net.minecraft.sound.SoundCategory.BLOCKS, 1.5f, 0.4f,
            false
        );
        
        source.sendFeedback(() -> Text.literal("Shader world effect triggered!").formatted(Formatting.LIGHT_PURPLE), true);
        
        return 1;
    }
}