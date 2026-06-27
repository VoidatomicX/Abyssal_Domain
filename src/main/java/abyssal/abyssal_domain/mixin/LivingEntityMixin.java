package abyssal.abyssal_domain.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void abyssal$creeperExplosionLogic(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        LivingEntity entity = (LivingEntity)(Object) this;

        if (!(entity instanceof CreeperEntity creeper)) return;

        // only care about explosions
        if (!source.isOf(DamageTypes.EXPLOSION) &&
                !source.isOf(DamageTypes.PLAYER_EXPLOSION)) {
            return;
        }

        // 🚫 cancel damage so creeper never dies from explosions
        cir.setReturnValue(false);

        // 💥 BUT still trigger chain reaction
        if (!creeper.getWorld().isClient()) {
            if (!creeper.isIgnited()) {
                creeper.ignite(); // makes it start its fuse
            }
        }
    }
}