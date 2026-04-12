package abyssal.abyssal_domain.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MirrorTridentEntity extends TridentEntity implements HomingProjectile {

    private static final TrackedData<Boolean> HOMING =
            DataTracker.registerData(MirrorTridentEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    // 🧠 SWARM LOCK (only one attacker per target)
    private static final Map<UUID, MirrorTridentEntity> TARGET_LOCK = new HashMap<>();

    private Entity target;
    private final Random random = new Random();

    // ─────────────────────────────
    // STATE
    // ─────────────────────────────

    private int homingDelay = 0;

    private double homingStrength = 0.15;
    private double homingNoise = 0.12;

    private int attackCycles = 0;
    private int maxAttackCycles = 1;

    private double orbitRadius = 6.0;

    private int phaseTicks = 0;

    private boolean canDealDamage = true;

    private enum Phase {
        ORBIT,
        STRIKE,
        RETREAT
    }

    private Phase phase = Phase.ORBIT;

    private int spawnGracePeriod = 5;
    private boolean hasHitTarget = false;

    public MirrorTridentEntity(EntityType<? extends TridentEntity> type, World world) {
        super(type, world);
    }

    // ─────────────────────────────
    // DATA TRACKER
    // ─────────────────────────────

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HOMING, false);
    }

    private boolean isHoming() {
        return this.dataTracker.get(HOMING);
    }

    // ─────────────────────────────
    // LOCK SYSTEM
    // ─────────────────────────────

    private boolean canAttackNow() {
        if (target == null) return false;

        MirrorTridentEntity current = TARGET_LOCK.get(target.getUuid());
        return current == null || current == this;
    }

    private void acquireLock() {
        if (target == null) return;
        TARGET_LOCK.put(target.getUuid(), this);
    }

    private void releaseLock() {
        if (target == null) return;

        UUID id = target.getUuid();
        if (TARGET_LOCK.get(id) == this) {
            TARGET_LOCK.remove(id);
        }
    }

    @Override
    public void remove(net.minecraft.entity.Entity.RemovalReason reason) {
        releaseLock();
        super.remove(reason);
    }

    // ─────────────────────────────
    // SETTERS
    // ─────────────────────────────

    @Override
    public void setTarget(Entity target) {
        this.target = target;
    }

    @Override
    public void setHomingEnabled(boolean enabled) {
        this.dataTracker.set(HOMING, enabled);

        if (enabled) {
            this.homingDelay = 2 + random.nextInt(6);
        }
    }

    @Override
    public void setHomingStrength(double strength) {
        this.homingStrength = Math.max(0.05, Math.min(1.0, strength));
    }

    @Override
    public void setHomingDelay(int ticks) {
        this.homingDelay = Math.max(0, ticks);
    }

    @Override
    public void setHomingNoise(double noise) {
        this.homingNoise = Math.max(0.0, Math.min(1.0, noise));
    }

    @Override
    public void setMaxAttackCycles(int cycles) {
        this.maxAttackCycles = Math.max(1, cycles);
    }

    @Override
    public void setOrbitRadius(double radius) {
        this.orbitRadius = Math.max(3.0, radius);
    }

    // ─────────────────────────────
    // STEERING
    // ─────────────────────────────

    private Vec3d steer(Vec3d current, Vec3d desiredDir, double speed) {

        Vec3d desired = desiredDir.multiply(speed);
        double steer = 0.2;

        return current.multiply(1.0 - steer)
                .add(desired.multiply(steer));
    }

    // ─────────────────────────────
    // LIFETIME CONDITIONS
    // ─────────────────────────────

    private boolean shouldDie() {
        if (target == null || !target.isAlive()) return true;
        if (attackCycles >= maxAttackCycles) return true;
        return false;
    }

    // ─────────────────────────────
    // MAIN TICK
    // ─────────────────────────────

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            if (spawnGracePeriod > 0) {
                spawnGracePeriod--;
                return;
            }

            if (shouldDie()) {
                this.discard();
                return;
            }
        }

        if (!isHoming() || target == null || !target.isAlive()) return;

        if (homingDelay > 0) {
            homingDelay--;
            return;
        }

        Vec3d pos = this.getPos();
        Vec3d vel = this.getVelocity();
        double speed = Math.max(1.0, vel.length());

        Vec3d targetPos = target.getPos().add(0, target.getHeight() * 0.5, 0);
        Vec3d targetVel = target.getVelocity();

        double angle = (this.getId() * 137.5) % 360;

        Vec3d orbitPoint = targetPos.add(
                Math.cos(Math.toRadians(angle)) * orbitRadius,
                1.0,
                Math.sin(Math.toRadians(angle)) * orbitRadius
        );

        phaseTicks++;

        // 💀 enforce swarm lock rules
        if (!canAttackNow() && phase == Phase.STRIKE) {
            phase = Phase.ORBIT;
        }

        switch (phase) {

            // 🛰️ ORBIT (SWARM FORMATION)
            case ORBIT -> {

                canDealDamage = false;

                Vec3d dir = orbitPoint.subtract(pos);

                if (dir.length() < 1.5) {
                    dir = targetPos.subtract(pos);
                }

                setVelocity(steer(vel, dir.normalize(), speed * 0.9));

                // only locked trident can attack
                if (pos.distanceTo(targetPos) < orbitRadius - 1.0 && canAttackNow()) {
                    phase = Phase.STRIKE;
                    phaseTicks = 0;
                    acquireLock();
                }
            }

            // 🎯 STRIKE (ONLY ONE AT A TIME)
            case STRIKE -> {

                canDealDamage = true;

                Vec3d predicted = targetPos.add(targetVel.multiply(0.3));
                Vec3d dir = predicted.subtract(pos).normalize();

                setVelocity(steer(vel, dir, speed * 1.9));

                if (phaseTicks > 8) {
                    phase = Phase.RETREAT;
                    phaseTicks = 0;

                    releaseLock();
                }
            }

            // 🌪️ RETREAT (RESET DISTANCE)
            case RETREAT -> {

                canDealDamage = false;

                Vec3d away = pos.subtract(targetPos).normalize();
                Vec3d retreatPoint = targetPos.add(away.multiply(orbitRadius + 3.0));

                Vec3d dir = retreatPoint.subtract(pos).normalize();

                setVelocity(steer(vel, dir, speed * 1.3));

                if (pos.distanceTo(targetPos) > orbitRadius + 2.5) {

                    phase = Phase.ORBIT;
                    phaseTicks = 0;
                    attackCycles++;
                }
            }
        }

        // 🧱 obstacle avoidance
        if (!this.getWorld().isSpaceEmpty(this.getBoundingBox().offset(vel.multiply(0.4)))) {
            setVelocity(getVelocity().add(0, 0.2, 0));
        }
    }
}