package abyssal.abyssal_domain.util;

import abyssal.abyssal_domain.item.custom.Terminus_Est;
import abyssal.abyssal_domain.network.ModPackets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ShieldZoneManager {

    private static final List<ShieldZone> zones = new ArrayList<>();
    private static final List<FrozenProjectile> frozenProjectiles = new ArrayList<>();
    private static final int PROJECTILE_CHECK_INTERVAL = 10;

    public static void addZone(ShieldZone zone) {
        zones.add(zone);
    }

    public static void removeZonesForPlayer(UUID playerId) {
        zones.removeIf(zone -> zone.getCaster().equals(playerId) || zone.getTarget().equals(playerId));
    }

    public static void tick(MinecraftServer server) {
        Iterator<ShieldZone> zoneIterator = zones.iterator();

        while (zoneIterator.hasNext()) {
            ShieldZone zone = zoneIterator.next();
            ServerPlayerEntity caster = getPlayerByUUID(server, zone.getCaster());
            ServerPlayerEntity target = getPlayerByUUID(server, zone.getTarget());

            if (caster == null) {
                broadcastClearShield(null, target);
                zoneIterator.remove();
                continue;
            }

            if (target == null || !target.isAlive()) {
                if (caster.isAlive()) {
                    triggerDischargeForCaster(caster);
                }
                broadcastClearShield(caster, target);
                zoneIterator.remove();
                continue;
            }

            if (!caster.isAlive()) {
                broadcastClearShield(caster, target);
                zoneIterator.remove();
                continue;
            }

            zone.enforce(caster.getWorld(), caster, target);

            if (caster.getWorld() instanceof ServerWorld serverWorld) {
                long time = serverWorld.getTime();
                if (time % PROJECTILE_CHECK_INTERVAL == 0) {
                    Box zoneBox = new Box(
                        zone.getCenter().getX() - zone.getRadius() - 5,
                        zone.getCenter().getY() - 2,
                        zone.getCenter().getZ() - zone.getRadius() - 5,
                        zone.getCenter().getX() + zone.getRadius() + 5,
                        zone.getCenter().getY() + zone.getHeight() + 2,
                        zone.getCenter().getZ() + zone.getRadius() + 5
                    );

                    for (Entity entity : serverWorld.getEntitiesByClass(Entity.class, zoneBox, e -> true)) {
                        if (zone.shouldBlockEntity(entity)) {
                            handleProjectileBlock(entity, zone, caster);
                        }
                    }
                }
            }

            if (!zone.isActive()) {
                broadcastClearShield(caster, target);
                zoneIterator.remove();
            }
        }

        Iterator<FrozenProjectile> frozenIterator = frozenProjectiles.iterator();
        while (frozenIterator.hasNext()) {
            FrozenProjectile fp = frozenIterator.next();
            fp.tick();
            if (fp.isExpired()) {
                fp.release();
                frozenIterator.remove();
            }
        }
    }

    private static void triggerDischargeForCaster(ServerPlayerEntity caster) {
        for (int i = 0; i < caster.getInventory().size(); i++) {
            var stack = caster.getInventory().getStack(i);
            if (stack.getItem() instanceof Terminus_Est) {
                caster.getItemCooldownManager().set(stack.getItem(), 1200);
                break;
            }
        }
    }

    private static void handleProjectileBlock(Entity entity, ShieldZone zone, ServerPlayerEntity caster) {
        if (entity instanceof ProjectileEntity) {
            boolean alreadyFrozen = frozenProjectiles.stream().anyMatch(fp -> fp.entity.getId() == entity.getId());
            if (!alreadyFrozen) {
                FrozenProjectile fp = new FrozenProjectile(entity, zone);
                frozenProjectiles.add(fp);
                zone.drainDurability(5);
                zone.onProjectileHit(caster);
            }
        }
    }

    private static ServerPlayerEntity getPlayerByUUID(MinecraftServer server, UUID uuid) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (player.getUuid().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    private static void broadcastClearShield(ServerPlayerEntity caster, ServerPlayerEntity target) {
        if (caster != null) {
            ModPackets.sendClearShield(caster);
        }
        if (target != null && target != caster) {
            ModPackets.sendClearShield(target);
        }
    }

    public static List<ShieldZone> getZones() {
        return new ArrayList<>(zones);
    }

    public static boolean isPlayerInShield(UUID playerId) {
        return zones.stream().anyMatch(zone ->
            zone.getCaster().equals(playerId) || zone.getTarget().equals(playerId));
    }

    public static ShieldZone getZoneForPlayer(UUID playerId) {
        return zones.stream().filter(zone ->
            zone.getCaster().equals(playerId) || zone.getTarget().equals(playerId))
            .findFirst()
            .orElse(null);
    }

    public static void triggerDischarge(ShieldZone zone, ServerPlayerEntity killer, ServerPlayerEntity victim) {
        if (zone == null || killer == null || victim == null) return;

        broadcastClearShield(killer, victim);
        zones.remove(zone);

        broadcastDischargeEffect(killer, victim);
    }

    private static void broadcastDischargeEffect(ServerPlayerEntity caster, ServerPlayerEntity target) {
        if (caster == null) return;

        caster.getWorld().playSound(null, caster.getBlockPos(),
            net.minecraft.sound.SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
            net.minecraft.sound.SoundCategory.PLAYERS, 1.0f, 0.5f);

        caster.getWorld().playSound(null, target.getBlockPos(),
            net.minecraft.sound.SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT,
            net.minecraft.sound.SoundCategory.PLAYERS, 1.0f, 0.5f);
    }

    public static void clearAll() {
        zones.clear();
        frozenProjectiles.clear();
    }

    public static class FrozenProjectile {
        private final Entity entity;
        private final ShieldZone zone;
        private final Vec3d frozenPos;
        private int freezeTicks;
        private static final int FREEZE_DURATION = 60;

        public FrozenProjectile(Entity entity, ShieldZone zone) {
            this.entity = entity;
            this.zone = zone;
            this.frozenPos = entity.getPos().add(0, 0.5, 0);
            this.freezeTicks = FREEZE_DURATION;

            entity.setVelocity(0, 0, 0);
            entity.velocityModified = true;
            entity.setNoGravity(true);

            broadcastFreezeEffect();
        }

        private void broadcastFreezeEffect() {
            if (entity.getWorld() instanceof ServerWorld serverWorld) {
                for (PlayerEntity nearby : serverWorld.getServer().getPlayerManager().getPlayerList()) {
                    if (nearby.distanceTo(entity) < 50) {
                        ModPackets.sendFrozenProjectile((ServerPlayerEntity) nearby,
                            (int)frozenPos.x, (int)frozenPos.y, (int)frozenPos.z);
                    }
                }
            }
        }

        public void tick() {
            freezeTicks--;
            entity.setNoGravity(true);
            entity.setPosition(frozenPos);
            entity.velocityModified = true;
        }

        public boolean isExpired() {
            return freezeTicks <= 0;
        }

        public void release() {
            entity.setNoGravity(false);
            entity.setVelocity(0, -0.5, 0);
            entity.velocityModified = true;
        }
    }
}
