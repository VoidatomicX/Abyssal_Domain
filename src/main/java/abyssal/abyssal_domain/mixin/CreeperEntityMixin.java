package abyssal.abyssal_domain.mixin;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.registry.Registries;
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

        // Cloud settings
        cloud.setRadius(3.0F);
        cloud.setDuration(20 * 5); // 15 seconds
        cloud.setWaitTime(0);
        cloud.setRadiusOnUse(-0.5F);
        cloud.setRadiusGrowth(-cloud.getRadius() / cloud.getDuration());

        // Random effect
        cloud.addEffect(new StatusEffectInstance(
                effect,
                20 * 5, // 10 seconds effect
                0
        ));

        creeper.getWorld().spawnEntity(cloud);
    }
}