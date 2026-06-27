package abyssal.abyssal_domain.entity.client;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.entity.custom.TerminusShieldEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TerminusShieldEntityRenderer extends EntityRenderer<TerminusShieldEntity> {

    private static final Identifier TEXTURE = Identifier.of(Abyssal_domain.MOD_ID, "textures/item/terminus_est.png");

    public TerminusShieldEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(TerminusShieldEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(TerminusShieldEntity entity) {
        return TEXTURE;
    }
}
