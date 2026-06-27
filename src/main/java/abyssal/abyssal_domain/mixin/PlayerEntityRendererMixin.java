package abyssal.abyssal_domain.mixin;

import abyssal.abyssal_domain.item.ModItems;
import abyssal.abyssal_domain.item.custom.GreatSwordItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @Inject(method = "setModelPose", at = @At("TAIL"))
    private void abyssal$greatswordPose(AbstractClientPlayerEntity player, CallbackInfo ci) {
        PlayerEntityRenderer renderer = (PlayerEntityRenderer)(Object) this;
        PlayerEntityModel<AbstractClientPlayerEntity> model = renderer.getModel();

        if (player.getMainHandStack().getItem() instanceof GreatSwordItem) {
            model.rightArmPose = BipedEntityModel.ArmPose.CROSSBOW_HOLD;
            model.leftArmPose = BipedEntityModel.ArmPose.CROSSBOW_HOLD;
        }
    }
}