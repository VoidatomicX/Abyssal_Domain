package abyssal.abyssal_domain.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
            if (p1 == null || p2 == null || !zone.isActive()) {
                it.remove();
                continue;
            }
            zone.enforce(p1, p2);
        }
    }
}