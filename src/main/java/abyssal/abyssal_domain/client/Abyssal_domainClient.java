package abyssal.abyssal_domain.client;

import abyssal.abyssal_domain.network.ModPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;

public class Abyssal_domainClient implements ClientModInitializer {

    private static BlockPos borderCenter;
    private static int borderRadius;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.FAKE_BORDER, (client, handler, buf, responseSender) -> {
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            int radius = buf.readInt();

            borderCenter = new BlockPos(x, y, z);
            borderRadius = radius;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (borderCenter != null && client.world != null) {
                renderCircularBorder(client.world, client);
            }
        });
    }

    private void renderCircularBorder(ClientWorld world, MinecraftClient client) {
        int steps = 80;
        for (int i = 0; i < steps; i++) {
            double angle = 2 * Math.PI * i / steps;
            double x = borderCenter.getX() + Math.cos(angle) * borderRadius + world.random.nextDouble() * 0.2;
            double z = borderCenter.getZ() + Math.sin(angle) * borderRadius + world.random.nextDouble() * 0.2;
            double y = borderCenter.getY() + 1.0 + world.random.nextDouble() * 2;
            world.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, 0.05, 0);
        }
    }
}