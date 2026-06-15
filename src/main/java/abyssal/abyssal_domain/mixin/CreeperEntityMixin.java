package abyssal.abyssal_domain.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {

    @Inject(method = "explode", at = @At("TAIL"))
    private void spawnRandomEffectCloud(CallbackInfo ci) {
        CreeperEntity creeper = (CreeperEntity)(Object) this;

        if (creeper.getWorld().isClient()) {
            return;
        }

        StatusEffect[] effects = Registries.STATUS_EFFECT.stream().toArray(StatusEffect[]::new);

        StatusEffect effect =
                effects[creeper.getRandom().nextInt(effects.length)];

        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(
                creeper.getWorld(),
                creeper.getX(),
                creeper.getY(),
                creeper.getZ()
        );

        cloud.setRadius(3.0F);
        cloud.setDuration(20 * 5);
        cloud.setWaitTime(0);
        cloud.setRadiusOnUse(-0.5F);
        cloud.setRadiusGrowth(-cloud.getRadius() / cloud.getDuration());

        cloud.addEffect(new StatusEffectInstance(
                effect,
                20 * 5,
                0
        ));

        creeper.getWorld().spawnEntity(cloud);

        // Place fire if the creeper was burning
        if (creeper.isOnFire()) {
            BlockPos center = creeper.getBlockPos();

            for (BlockPos pos : BlockPos.iterate(center.add(-2, -1, -2), center.add(2, 1, 2))) {
                if (creeper.getRandom().nextFloat() < 0.3F
                        && creeper.getWorld().getBlockState(pos).isAir()
                        && Blocks.FIRE.getDefaultState().canPlaceAt(creeper.getWorld(), pos)) {

                    creeper.getWorld().setBlockState(pos, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }
}