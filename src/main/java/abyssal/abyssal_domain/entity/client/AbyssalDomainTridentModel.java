package abyssal.abyssal_domain.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class AbyssalDomainTridentModel extends EntityModel<Entity> {
	private final ModelPart bone3;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart bb_main;
	public AbyssalDomainTridentModel(ModelPart root) {
		this.bone3 = root.getChild("bone3");
		this.bone = root.getChild("bone");
		this.bone2 = root.getChild("bone2");
		this.bb_main = root.getChild("bb_main");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bone3 = modelPartData.addChild("bone3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.85F, 0.0F));

		ModelPartData cube_r1 = bone3.addChild("cube_r1", ModelPartBuilder.create().uv(24, 23).cuboid(-4.0F, 4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, -2.4871F));

		ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(1.125F, -7.375F, 0.0F));

		ModelPartData cube_r2 = bone.addChild("cube_r2", ModelPartBuilder.create().uv(24, 34).cuboid(3.3F, -1.375F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.1762F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		ModelPartData cube_r3 = bone.addChild("cube_r3", ModelPartBuilder.create().uv(16, 28).cuboid(3.0F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(-1.025F, 3.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		ModelPartData cube_r4 = bone.addChild("cube_r4", ModelPartBuilder.create().uv(4, 34).cuboid(3.125F, -2.175F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.of(-1.025F, 3.0F, 0.0F, 0.0F, 0.0F, 0.5061F));

		ModelPartData cube_r5 = bone.addChild("cube_r5", ModelPartBuilder.create().uv(12, 33).cuboid(3.0F, -3.2F, 0.0F, 1.0F, 4.0F, 1.0F, new Dilation(-0.277F))
		.uv(32, 21).cuboid(3.0F, -1.675F, 0.0F, 1.0F, 4.0F, 1.0F, new Dilation(-0.002F)), ModelTransform.of(-0.25F, -1.475F, -0.5F, 0.0F, 0.0F, -0.3054F));

		ModelPartData cube_r6 = bone.addChild("cube_r6", ModelPartBuilder.create().uv(22, 28).cuboid(3.0F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(-0.1762F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		ModelPartData bone2 = modelPartData.addChild("bone2", ModelPartBuilder.create(), ModelTransform.of(-1.15F, -7.375F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r7 = bone2.addChild("cube_r7", ModelPartBuilder.create().uv(0, 34).cuboid(0.8F, -3.8F, 0.0F, 1.0F, 4.0F, 1.0F, new Dilation(-0.277F)), ModelTransform.of(2.025F, -1.475F, -0.5F, 0.0F, 0.0F, -0.3054F));

		ModelPartData cube_r8 = bone2.addChild("cube_r8", ModelPartBuilder.create().uv(20, 34).cuboid(3.3F, -1.375F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.1762F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		ModelPartData cube_r9 = bone2.addChild("cube_r9", ModelPartBuilder.create().uv(32, 3).cuboid(3.0F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(-1.025F, 3.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		ModelPartData cube_r10 = bone2.addChild("cube_r10", ModelPartBuilder.create().uv(16, 34).cuboid(3.125F, -2.175F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.001F)), ModelTransform.of(-1.025F, 3.0F, 0.0F, 0.0F, 0.0F, 0.5061F));

		ModelPartData cube_r11 = bone2.addChild("cube_r11", ModelPartBuilder.create().uv(8, 33).cuboid(3.0F, -1.675F, 0.0F, 1.0F, 4.0F, 1.0F, new Dilation(-0.002F)), ModelTransform.of(-0.25F, -1.475F, -0.5F, 0.0F, 0.0F, -0.3054F));

		ModelPartData cube_r12 = bone2.addChild("cube_r12", ModelPartBuilder.create().uv(28, 28).cuboid(3.0F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(-0.1762F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -25.0F, -1.0F, 2.0F, 32.0F, 2.0F, new Dilation(0.0F))
		.uv(32, 13).cuboid(2.0F, -27.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(8, 18).cuboid(-2.0F, -21.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(8, 0).cuboid(-2.0F, -22.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(-0.45F))
		.uv(8, 6).cuboid(-2.0F, -21.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(-0.45F))
		.uv(8, 23).cuboid(-2.0F, -24.7F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(-0.2F))
		.uv(24, 0).cuboid(-3.0F, -26.625F, -0.5F, 6.0F, 2.0F, 1.0F, new Dilation(-0.175F))
		.uv(34, 26).cuboid(1.0F, -26.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(32, 17).cuboid(-3.0F, -27.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(32, 9).cuboid(-1.0F, -27.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(28, 34).cuboid(-2.0F, -26.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(34, 29).cuboid(1.7F, -26.575F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(-0.001F))
		.uv(34, 32).cuboid(0.325F, -26.575F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(-0.001F))
		.uv(34, 35).cuboid(-1.3F, -26.575F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(-0.001F))
		.uv(36, 21).cuboid(-2.675F, -26.575F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(-0.001F))
		.uv(24, 17).cuboid(-1.0F, -30.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(-0.25F))
		.uv(24, 3).cuboid(-1.0F, -34.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(-0.4F))
		.uv(24, 10).cuboid(-1.0F, -37.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(-0.65F))
		.uv(8, 18).cuboid(-2.0F, -24.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r13 = bb_main.addChild("cube_r13", ModelPartBuilder.create().uv(8, 28).cuboid(-4.0F, 4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(-0.0011F)), ModelTransform.of(0.0F, 0.85F, 0.0F, 0.0F, 0.0F, -0.6545F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		bone3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		bone2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}