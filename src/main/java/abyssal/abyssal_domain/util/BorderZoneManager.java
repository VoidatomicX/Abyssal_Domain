package abyssal.abyssal_domain.util;

import abyssal.abyssal_domain.network.ModPackets;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BorderZoneManager {

    private static final List<BorderZone> activeZones = new ArrayList<>();

    public static void addZone(BorderZone zone) {
        activeZones.add(zone);
    }

    public static void tick(MinecraftServer server) {
        Iterator<BorderZone> it = activeZones.iterator();

        while (it.hasNext()) {
            BorderZone zone = it.next();

            ServerPlayerEntity p1 = server.getPlayerManager().getPlayer(zone.getPlayer1());
            ServerPlayerEntity p2 = server.getPlayerManager().getPlayer(zone.getPlayer2());

            boolean invalid =
                    p1 == null ||
                            p2 == null ||
                            !p1.isAlive() ||
                            !p2.isAlive() ||
                            !zone.isActive();

            if (invalid) {
                sendClear(p1);
                sendClear(p2);
                it.remove();
                continue;
            }

            zone.enforce(p1, p2);
        }
    }

    private static void sendClear(ServerPlayerEntity player) {
        if (player == null) return;

        PacketByteBuf buf = PacketByteBufs.create();
        ServerPlayNetworking.send(player, ModPackets.CLEAR_FAKE_BORDER, buf);
    }
}