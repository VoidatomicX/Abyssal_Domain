package abyssal.abyssal_domain.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;

public class StunEffect extends StatusEffect {

    protected StunEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        // Stop movement
        entity.setVelocity(0.0D, entity.getVelocity().y, 0.0D);

        // Stop mob AI navigation
        if (entity instanceof MobEntity mob) {
            mob.getNavigation().stop();
            mob.setTarget(null);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}