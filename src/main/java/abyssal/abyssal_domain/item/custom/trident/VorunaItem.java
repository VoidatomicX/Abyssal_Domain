package abyssal.abyssal_domain.item.custom.trident;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;

public class VorunaItem extends AbyssalTridentsItem {

    private static final int MAX_CHARGE = 40;
    private static final int DURATION = 200;

    public VorunaItem(Settings settings) {
        super(settings, 100, true, false);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return MAX_CHARGE;
    }

    @Override
    protected void onChargeRelease(World world, PlayerEntity player, ItemStack stack, int chargeTime) {
        if (world.isClient) return;

        float charge = Math.min(1f, chargeTime / (float) MAX_CHARGE);

        int blades = Math.max(3, (int)(charge * 10));
        double radius = 0.8 + charge * 1.2;

        stack.setNbt(stack.getOrCreateNbt());
        stack.getNbt().putInt("VorunaBlades", blades);
        stack.getNbt().putInt("VorunaRadius", (int)(radius * 10));
        stack.getNbt().putInt("VorunaTick", DURATION);
        stack.getNbt().putDouble("VorunaCenterX", player.getX());
        stack.getNbt().putDouble("VorunaCenterY", player.getY() + 1.2);
        stack.getNbt().putDouble("VorunaCenterZ", player.getZ());
    }

    public static void tickVoruna(World world, PlayerEntity player, ItemStack stack) {
        if (world.isClient) return;
        if (!(world instanceof ServerWorld serverWorld)) return;
        if (!stack.hasNbt()) return;

        var nbt = stack.getNbt();
        if (!nbt.contains("VorunaTick")) return;

        int tick = nbt.getInt("VorunaTick");
        tick--;

        if (tick <= 0) {
            nbt.remove("VorunaTick");
            nbt.remove("VorunaBlades");
            nbt.remove("VorunaRadius");
            return;
        }

        nbt.putInt("VorunaTick", tick);

        int blades = nbt.getInt("VorunaBlades");
        double radius = nbt.getInt("VorunaRadius") / 10.0;
        double centerX = nbt.getDouble("VorunaCenterX");
        double centerY = nbt.getDouble("VorunaCenterY");
        double centerZ = nbt.getDouble("VorunaCenterZ");

        double angle = (DURATION - tick) * 0.18;

        for (int i = 0; i < blades; i++) {
            double bladeAngle = angle + (Math.PI * 2 * i / blades);
            double x = centerX + Math.cos(bladeAngle) * radius;
            double z = centerZ + Math.sin(bladeAngle) * radius;
            double y = centerY + Math.sin(angle * 2 + i) * 0.25;

            serverWorld.spawnParticles(ParticleTypes.ENTITY_EFFECT,
                    x, y, z,
                    1, 0, 0, 0, 0);

            serverWorld.spawnParticles(ParticleTypes.WITCH,
                    x + (world.random.nextDouble() - 0.5) * 0.2,
                    y + (world.random.nextDouble() - 0.5) * 0.2,
                    z + (world.random.nextDouble() - 0.5) * 0.2,
                    0, 0, 0, 0, 0);
        }

        for (LivingEntity target : world.getEntitiesByClass(
                LivingEntity.class,
                player.getBoundingBox().expand(radius + 1.0),
                e -> e != player && e.isAlive()
        )) {
            double dist = target.getPos().distanceTo(player.getPos());
            if (dist < radius + 1.0) {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 60, 0));
                target.damage(world.getDamageSources().magic(), 2.0f);
            }
        }
    }
}