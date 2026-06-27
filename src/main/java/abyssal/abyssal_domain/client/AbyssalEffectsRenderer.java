package abyssal.abyssal_domain.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

public class AbyssalEffectsRenderer {

    private static MinecraftClient client;
    private static boolean shaderActive = false;
    private static String currentShader = "none";
    private static float shaderTime = 0.0f;
    private static double worldEffectX, worldEffectY, worldEffectZ;
    private static int worldEffectTicks = 0;
    
    public static void init(MinecraftClient mc) {
        client = mc;
    }
    
    public static void applyShader(String shaderName) {
        if (client == null) return;
        
        clearShader();
        
        if (shaderName != null && !shaderName.equals("none")) {
            currentShader = shaderName;
            shaderActive = true;
            shaderTime = 0.0f;
        }
    }
    
    public static void clearShader() {
        shaderActive = false;
        currentShader = "none";
    }
    
    public static void triggerWorldEffect(double x, double y, double z, int ticks) {
        if (client == null) return;
        
        worldEffectX = x;
        worldEffectY = y;
        worldEffectZ = z;
        worldEffectTicks = ticks;
    }
    
    public static void render(float tickDelta) {
        if (client == null || client.world == null) return;
        
        shaderTime += tickDelta * 0.05f;
        
        if (worldEffectTicks > 0) {
            worldEffectTicks--;
            renderWorldEffect(tickDelta);
        }
        
        if (shaderActive) {
            renderShaderOverlay(tickDelta);
        }
    }
    
    private static void renderShaderOverlay(float tickDelta) {
        if (client.player == null || client.getFramebuffer() == null) return;
        
        var buffer = Tessellator.getInstance().getBuffer();
        
        Framebuffer fb = client.getFramebuffer();
        float alpha = 0.15f + (float)Math.sin(shaderTime * 2) * 0.05f;
        
        buffer.vertex(0, 0, 0).texture(0, 0).color(0.8f, 0.2f, 1.0f, alpha);
        buffer.vertex(client.getWindow().getFramebufferWidth(), 0, 0).texture(1, 0).color(0.8f, 0.2f, 1.0f, alpha);
        buffer.vertex(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight(), 0).texture(1, 1).color(0.8f, 0.2f, 1.0f, alpha);
        buffer.vertex(0, client.getWindow().getFramebufferHeight(), 0).texture(0, 1).color(0.8f, 0.2f, 1.0f, alpha);
        
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
    
    private static void renderWorldEffect(float tickDelta) {
        if (client.world == null) return;
        
        var world = client.world;
        var player = client.player;
        if (player == null) return;
        
        double px = player.getX();
        double py = player.getY();
        double pz = player.getZ();
        
        for (int i = 0; i < 30; i++) {
            double angle = i * 0.21 + shaderTime * 3;
            double radius = 2.5 + Math.sin(shaderTime * 4 + i * 0.5) * 1.0;
            
            double wx = worldEffectX + Math.cos(angle) * radius;
            double wy = worldEffectY + 1 + Math.sin(shaderTime * 5 + i) * 0.3;
            double wz = worldEffectZ + Math.sin(angle) * radius;
            
            world.addParticle(
                net.minecraft.particle.ParticleTypes.REVERSE_PORTAL,
                wx, wy, wz,
                0.0, 0.01, 0.0
            );
            
            if (i % 3 == 0) {
                world.addParticle(
                    net.minecraft.particle.ParticleTypes.WITCH,
                    wx + (world.random.nextDouble() - 0.5) * 2,
                    wy + (world.random.nextDouble() - 0.5) * 2,
                    wz + (world.random.nextDouble() - 0.5) * 2,
                    0.0, 0.0, 0.0
                );
            }
        }
        
        float pulse = (float)(0.3 + Math.sin(shaderTime * 6) * 0.1);
        world.addParticle(
            net.minecraft.particle.ParticleTypes.FLASH,
            worldEffectX, worldEffectY + 1, worldEffectZ,
            0, pulse, 0
        );
    }
    
    public static boolean isShaderActive() {
        return shaderActive;
    }
    
    public static String getCurrentShader() {
        return currentShader;
    }
}