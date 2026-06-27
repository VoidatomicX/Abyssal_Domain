package abyssal.abyssal_domain.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class ShieldZone {

    private final UUID caster;
    private final UUID target;
    private final BlockPos center;
    private final int radius;
    private final int height;
    private boolean active = true;
    private long creationTime;
    private int durability = 100;

    public ShieldZone(ServerPlayerEntity caster, ServerPlayerEntity target, BlockPos center, int radius) {
        this.caster = caster.getUuid();
        this.target = target.getUuid();
        this.center = center;
        this.radius = radius;
        this.height = 10;
        this.creationTime = System.currentTimeMillis();
    }
    
    public void setDurability(int durability) {
        this.durability = durability;
    }
    
    public int getDurability() {
        return durability;
    }
    
    public void drainDurability(int amount) {
        durability -= amount;
        if (durability <= 0) {
            active = false;
        }
    }

    public boolean contains(Vec3d pos) {
        double dx = pos.x - center.getX();
        double dz = pos.z - center.getZ();
        double dy = pos.y - center.getY();
        return dx * dx + dz * dz <= radius * radius && dy >= -1 && dy <= height;
    }

    public boolean containsPlayer(ServerPlayerEntity player) {
        return contains(player.getPos());
    }

    public void enforce(World world, ServerPlayerEntity p1, ServerPlayerEntity p2) {
        if (!active) return;

        if (!p1.isAlive() || !p2.isAlive() || !p1.getWorld().equals(p2.getWorld())) {
            active = false;
            return;
        }

        if (!containsPlayer(p1)) {
            teleportInside(p1);
        }
        if (!containsPlayer(p2)) {
            teleportInside(p2);
        }
    }

    public boolean shouldBlockEntity(Entity entity) {
        if (!active) return false;
        if (!(entity instanceof ProjectileEntity)) return false;
        if (entity.getUuid().equals(caster) || entity.getUuid().equals(target)) return false;
        
        Vec3d pos = entity.getPos();
        double dx = pos.x - center.getX();
        double dz = pos.z - center.getZ();
        double distSq = dx * dx + dz * dz;
        
        if (distSq > radius * radius) {
            Vec3d vel = entity.getVelocity();
            double dot = dx * vel.x + dz * vel.z;
            if (dot > 0) {
                return true;
            }
        }
        
        return false;
    }

    public Vec3d getReflectionVector(Entity entity) {
        Vec3d velocity = entity.getVelocity();
        
        Vec3d pos = entity.getPos();
        double dx = pos.x - center.getX();
        double dz = pos.z - center.getZ();
        
        double dist = Math.sqrt(dx * dx + dz * dz);
        if (dist < 0.001) return velocity.multiply(-0.5);
        
        Vec3d normal = new Vec3d(dx / dist, 0, dz / dist);
        double dot = velocity.x * normal.x + velocity.z * normal.z;
        
        return new Vec3d(
            velocity.x - 2 * dot * normal.x,
            velocity.y * 0.3,
            velocity.z - 2 * dot * normal.z
        ).multiply(0.7);
    }

    private void teleportInside(ServerPlayerEntity player) {
        Vec3d dir = player.getPos().subtract(center.getX(), player.getY(), center.getZ());

        if (dir.lengthSquared() < 0.001) {
            dir = new Vec3d(1, 0, 0);
        } else {
            dir = dir.normalize();
        }

        Vec3d newPos = new Vec3d(
                center.getX(),
                player.getY(),
                center.getZ()
        ).add(dir.multiply(radius - 0.5));

        player.requestTeleport(newPos.x, newPos.y, newPos.z);
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public UUID getCaster() { return caster; }
    public UUID getTarget() { return target; }
    public BlockPos getCenter() { return center; }
    public int getRadius() { return radius; }
    public int getHeight() { return height; }
    public long getCreationTime() { return creationTime; }

    public void onProjectileHit(ServerPlayerEntity caster) {
        caster.getWorld().playSound(null, caster.getBlockPos(),
            net.minecraft.sound.SoundEvents.BLOCK_GLASS_BREAK,
            net.minecraft.sound.SoundCategory.PLAYERS, 0.5f, 1.0f);
    }

}
