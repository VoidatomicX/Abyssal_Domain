// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package abyssal.abyssal_domain.entity.client;

import abyssal.abyssal_domain.entity.custom.GoobichthysEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class GoobichthyModel<T extends GoobichthysEntity> extends SinglePartEntityModel<T> {
	private final ModelPart body;

	public GoobichthyModel(ModelPart root) {
		this.body = root.getChild("body");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 19).cuboid(-2.5F, -2.0F, -4.5F, 5.0F, 4.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 22.0F, 2.5F));

		ModelPartData leftmouth = body.addChild("leftmouth", ModelPartBuilder.create().uv(26, 0).cuboid(-1.0F, -2.0F, -5.0F, 3.0F, 4.0F, 5.0F, new Dilation(0.0F))
		.uv(32, 13).cuboid(-1.0F, 0.0F, -5.0F, 3.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 0.0F, -4.5F));

		ModelPartData rightmouth = body.addChild("rightmouth", ModelPartBuilder.create().uv(0, 32).mirrored().cuboid(-2.0F, -2.0F, -5.0F, 3.0F, 4.0F, 5.0F, new Dilation(0.0F)).mirrored(false)
		.uv(6, 32).mirrored().cuboid(-2.0F, 0.0F, -5.0F, 3.0F, 0.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-0.5F, 0.0F, -4.5F));

		ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(28, 13).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 5.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(0.0F, -4.0F, 0.0F, 0.0F, 6.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 4.5F));

		ModelPartData leftfin = body.addChild("leftfin", ModelPartBuilder.create().uv(26, 9).cuboid(0.0F, 0.0F, -2.5F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 1.75F, -1.0F));

		ModelPartData rightfin = body.addChild("rightfin", ModelPartBuilder.create().uv(26, 9).mirrored().cuboid(-6.0F, 0.0F, -2.5F, 6.0F, 0.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.5F, 1.75F, -1.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(GoobichthysEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return body;
	}
}