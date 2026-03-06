package abyssal.abyssal_domain.entity.client;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.entity.custom.GoobichthysEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class GoobichthysRenderer extends MobEntityRenderer<GoobichthysEntity, GoobichthyModel<GoobichthysEntity>> {
    private static final Identifier Texture = new Identifier(Abyssal_domain.MOD_ID, "textures/entity/goobichthys");

    public GoobichthysRenderer(EntityRendererFactory.Context context) {
        super(context, new GoobichthyModel<>(context.getPart(ModModelLayers.Goobichthys)),0.6f);
    }

    @Override
    public Identifier getTexture(GoobichthysEntity entity) {
        return Texture;
    }

    @Override
    public void render(GoobichthysEntity mobEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }


        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
