package abyssal.abyssal_domain.mixin;

import abyssal.abyssal_domain.effect.ModEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "travel", at = @At("HEAD"))
    private void stunTravel(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (player.hasStatusEffect(ModEffects.STUN)) {

            Vec3d velocity = player.getVelocity();

            // Stop player control (X/Z only)
            player.setVelocity(0.0D, velocity.y, 0.0D);

            player.sidewaysSpeed = 0.0F;
            player.forwardSpeed = 0.0F;
            player.setJumping(false);

        }
    }


}