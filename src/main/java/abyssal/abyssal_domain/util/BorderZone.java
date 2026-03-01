package abyssal.abyssal_domain.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class BorderZone {

    private final UUID player1;
    private final UUID player2;
    private final BlockPos center;
    private final int radius;
    private boolean active = true;

    public BorderZone(ServerPlayerEntity player1, ServerPlayerEntity player2, BlockPos center, int radius) {
        this.player1 = player1.getUuid();
        this.player2 = player2.getUuid();
        this.center = center;
        this.radius = radius;
    }

    public boolean contains(ServerPlayerEntity player) {
        Vec3d pos = player.getPos();
        double dx = pos.x - center.getX();
        double dz = pos.z - center.getZ();
        return dx * dx + dz * dz <= radius * radius;
    }

    public void enforce(ServerPlayerEntity player1Entity, ServerPlayerEntity player2Entity) {
        if (!active) return;

        if (!player1Entity.isAlive() || !player2Entity.isAlive() ||
                !player1Entity.getWorld().equals(player2Entity.getWorld())) {
            active = false;
            return;
        }

        if (!contains(player1Entity)) teleportInside(player1Entity);
        if (!contains(player2Entity)) teleportInside(player2Entity);
    }

    private void teleportInside(ServerPlayerEntity player) {
        Vec3d dir = player.getPos().subtract(center.getX(), player.getY(), center.getZ()).normalize();
        Vec3d newPos = new Vec3d(center.getX(), player.getY(), center.getZ()).add(dir.multiply(radius - 0.5));
        player.requestTeleport(newPos.x, newPos.y, newPos.z);
    }

    public boolean isActive() { return active; }
    public UUID getPlayer1() { return player1; }
    public UUID getPlayer2() { return player2; }
    public BlockPos getCenter() { return center; }
    public int getRadius() { return radius; }
}