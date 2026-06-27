package abyssal.abyssal_domain.mixin;

import abyssal.abyssal_domain.entity.custom.TerminusShieldEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void abyssal$creeperExplosionLogic(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        LivingEntity entity = (LivingEntity)(Object) this;

        if (entity instanceof CreeperEntity creeper) {
            if (!source.isOf(DamageTypes.EXPLOSION) && !source.isOf(DamageTypes.PLAYER_EXPLOSION)) {
                return;
            }
            cir.setReturnValue(false);
            if (!creeper.getWorld().isClient()) {
                if (!creeper.isIgnited()) {
                    creeper.ignite();
                }
            }
            return;
        }

        if (entity instanceof PlayerEntity player && !player.getWorld().isClient()) {
            for (Entity e : player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(4.0))) {
                if (e instanceof TerminusShieldEntity shield && shield.getOwnerUuid() != null && shield.getOwnerUuid().equals(player.getUuid())) {
                    if (shield.tryBlockDamage(amount)) {
                        cir.setReturnValue(false);
                        shield.getWorld().playSound(null, shield.getBlockPos(), net.minecraft.sound.SoundEvents.BLOCK_ANVIL_PLACE, net.minecraft.sound.SoundCategory.PLAYERS, 0.5f, 1.5f);
                        return;
                    }
                }
            }
        }
    }
}
