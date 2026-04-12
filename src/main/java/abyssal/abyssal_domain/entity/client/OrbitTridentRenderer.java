package abyssal.abyssal_domain.entity.client;

import abyssal.abyssal_domain.entity.custom.OrbitTridentEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class OrbitTridentRenderer extends EntityRenderer<OrbitTridentEntity> {

    public static final EntityModelLayer ORBIT_TRIDENT_LAYER =
            new EntityModelLayer(new Identifier("abyssal", "orbit_trident"), "main");

    public static final Identifier TEXTURE =
            new Identifier("abyssal_domain", "textures/entity/2.png");

    private final AbyssalDomainTridentModel model;

    public OrbitTridentRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new AbyssalDomainTridentModel(
                context.getPart(ORBIT_TRIDENT_LAYER)
        );
    }

    @Override
    public void render(OrbitTridentEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light) {

        matrices.push();

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(
                MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F
        ));

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(
                MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 90.0F
        ));

        // 🔥 LIGHT BLUE + TRANSPARENCY
        RenderLayer layer = RenderLayer.getEntityTranslucent(TEXTURE);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(layer);

        this.model.render(
                matrices,
                vertexConsumer,
                light,
                OverlayTexture.DEFAULT_UV,
                1F, // R
                1F, // G
                1F,  // B (light cyan-blue)
                1F  // alpha (semi transparent)
        );

        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(OrbitTridentEntity entity) {
        return TEXTURE;
    }
}