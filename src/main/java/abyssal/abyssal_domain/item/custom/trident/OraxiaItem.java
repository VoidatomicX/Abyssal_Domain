package abyssal.abyssal_domain.item.custom.trident;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import abyssal.abyssal_domain.enchants.ModEnchantments;
import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.entity.custom.HomingProjectile;
import abyssal.abyssal_domain.entity.custom.MirrorTridentEntity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.projectile.ProjectileUtil;

import java.util.Random;

public class OraxiaItem extends AbyssalTridentsItem {

    private static final int COUNT = 8;
    private static final double SPEED = 2.5;
    private static final double SPREAD_DEGREES = 25.0;

    public OraxiaItem(Settings settings) {
        super(settings, 40, true, false);
    }

    @Override
    protected void onChargeRelease(World world, PlayerEntity player, ItemStack stack, int chargeTime) {

        Vec3d baseDir = player.getRotationVec(1.0F);
        Vec3d spawnPos = player.getEyePos();
        Random random = new Random();

        boolean hasEnchant = EnchantmentHelper.getLevel(ModEnchantments.TARGETING, stack) > 0;
        Entity target = hasEnchant ? findTarget(player, 64) : null;

        int extraHits = EnchantmentHelper.getLevel(ModEnchantments.TARGETING, stack);
        int maxCycles = 1 + extraHits;

        for (int i = 0; i < COUNT; i++) {

            MirrorTridentEntity trident = new MirrorTridentEntity(
                    ModEntities.MIRROR_TRIDENT,
                    world
            );

            trident.setOwner(player);
            trident.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

            Vec3d spreadDir = applySpread(
                    baseDir,
                    (random.nextDouble() - 0.5) * SPREAD_DEGREES,
                    (random.nextDouble() - 0.5) * SPREAD_DEGREES
            ).normalize();

            Vec3d velocity = spreadDir.multiply(SPEED);

            if (hasEnchant && target != null) {

                HomingProjectile hp = (HomingProjectile) trident;

                hp.setTarget(target);
                hp.setHomingEnabled(true);

                hp.setHomingStrength(0.08 + random.nextDouble() * 0.05);
                hp.setHomingDelay(2 + random.nextInt(4));
                hp.setHomingNoise(0.08 + random.nextDouble() * 0.08);

                hp.setMaxAttackCycles(1 + EnchantmentHelper.getLevel(ModEnchantments.TARGETING, stack));

                // 🛰️ IMPORTANT: BIGGER FORMATION RADIUS
                hp.setOrbitRadius(5.5 + random.nextDouble() * 2.5);
            }

            trident.setVelocity(velocity);
            world.spawnEntity(trident);
        }
    }

    private Entity findTarget(PlayerEntity player, double range) {

        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d look = player.getRotationVec(1.0F);
        Vec3d end = start.add(look.multiply(range));

        Box box = player.getBoundingBox()
                .stretch(look.multiply(range))
                .expand(1.0);

        EntityHitResult result = ProjectileUtil.getEntityCollision(
                player.getWorld(),
                player,
                start,
                end,
                box,
                entity -> !entity.isSpectator() && entity.isAlive() && entity != player
        );

        return result != null ? result.getEntity() : null;
    }

    private Vec3d applySpread(Vec3d dir, double yawDeg, double pitchDeg) {

        double yaw = Math.toRadians(yawDeg);
        double pitch = Math.toRadians(pitchDeg);

        double cosYaw = Math.cos(yaw);
        double sinYaw = Math.sin(yaw);

        double x1 = dir.x * cosYaw - dir.z * sinYaw;
        double z1 = dir.x * sinYaw + dir.z * cosYaw;

        double cosPitch = Math.cos(pitch);
        double sinPitch = Math.sin(pitch);

        double y2 = dir.y * cosPitch - z1 * sinPitch;
        double z2 = dir.y * sinPitch + z1 * cosPitch;

        return new Vec3d(x1, y2, z2);
    }
}