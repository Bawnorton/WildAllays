package com.bawnorton.wildallays.model;

import com.bawnorton.wildallays.config.ConfigManager;
import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.entity.allay.LostAllay;
import com.bawnorton.wildallays.util.Colour;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class BiomeAllayModel<T extends BiomeAllay> extends SinglePartEntityModel<T> implements ModelWithArms {
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart rightArm;
    public final ModelPart leftArm;
    public final ModelPart rightWing;
    public final ModelPart leftWing;

    private BiomeAllay allay = null;

    public BiomeAllayModel(ModelPart head) {
        this.head = head.getChild("root");
        this.body = this.head.getChild("body");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
    }

    public ModelPart getPart() {
        return this.head;
    }

    public static TexturedModelData getTexturedModelData() {
        return TexturedModelData.of(getModelData(Dilation.NONE), 32, 32);
    }

    public static ModelData getModelData(Dilation dilation) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData2 = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 23.5F, 0.0F));
        modelPartData2.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, dilation), ModelTransform.pivot(0.0F, -3.99F, 0.0F));
        ModelPartData modelPartData3 = modelPartData2.addChild("body", ModelPartBuilder.create().uv(0, 10).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, dilation).uv(0, 16).cuboid(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 2.0F, dilation.add(-0.02F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));
        modelPartData3.addChild("right_arm", ModelPartBuilder.create().uv(23, 0).cuboid(-0.75F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, dilation.add(-0.01F)), ModelTransform.pivot(-1.75F, 0.5F, 0.0F));
        modelPartData3.addChild("left_arm", ModelPartBuilder.create().uv(23, 6).cuboid(-0.25F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, dilation.add(-0.01F)), ModelTransform.pivot(1.75F, 0.5F, 0.0F));
        modelPartData3.addChild("right_wing", ModelPartBuilder.create().uv(16, 14).cuboid(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, dilation), ModelTransform.pivot(-0.5F, 0.0F, 0.65F));
        modelPartData3.addChild("left_wing", ModelPartBuilder.create().uv(16, 14).cuboid(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, dilation), ModelTransform.pivot(0.5F, 0.0F, 0.65F));
        return modelData;
    }

    public void setAngles(BiomeAllay allayEntity, float f, float g, float h, float i, float j) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        float k = h * 20.0F * 0.017453292F + g;
        float l = MathHelper.cos(k) * 3.1415927F * 0.15F;
        float m = h - (float)allayEntity.age;
        float n = h * 9.0F * 0.017453292F;
        float o = Math.min(g / 0.3F, 1.0F);
        float p = 1.0F - o;
        float q = allayEntity.method_43397(m);
        this.rightWing.pitch = 0.43633232F;
        this.rightWing.yaw = -0.61086524F + l;
        this.leftWing.pitch = 0.43633232F;
        this.leftWing.yaw = 0.61086524F - l;
        float r = o * 0.6981317F;
        this.body.pitch = r;
        float s = MathHelper.lerp(q, r, MathHelper.lerp(o, -1.0471976F, -0.7853982F));
        this.head.pivotY += (float)Math.cos(n) * 0.25F * p;
        this.rightArm.pitch = s;
        this.leftArm.pitch = s;
        float t = p * (1.0F - q);
        float u = 0.43633232F - MathHelper.cos(n + 4.712389F) * 3.1415927F * 0.075F * t;
        this.leftArm.roll = -u;
        this.rightArm.roll = u;
        this.rightArm.yaw = 0.27925268F * q;
        this.leftArm.yaw = -0.27925268F * q;
    }

    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        if(allay instanceof LostAllay && alpha > 0.01) {
            if (!ConfigManager.get("lost_allay_camoflages", Boolean.class)) {
                this.head.render(matrices, vertices, light, overlay);
            } else {
                Colour c = allay.getColor();
                this.head.render(matrices, vertices, light, overlay, c.r(), c.g(), c.b(), alpha);
            }
        } else {
            this.head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }

    public void animateModel(BiomeAllay allayEntity, float f, float g, float h) {
        this.allay = allayEntity;
        this.rightArm.pitch = 0.0F;
        this.rightArm.yaw = 0.0F;
        this.rightArm.roll = 0.3927F;
        this.leftArm.pitch = 0.0F;
        this.leftArm.yaw = 0.0F;
        this.leftArm.roll = -0.3927F;
    }


    public void setAttributes(BiomeAllayModel<T> model) {
        super.copyStateTo(model);
        model.head.copyTransform(this.head);
        model.body.copyTransform(this.body);
        model.rightArm.copyTransform(this.rightArm);
        model.leftArm.copyTransform(this.leftArm);
        model.rightWing.copyTransform(this.rightWing);
        model.leftWing.copyTransform(this.leftWing);
    }

    public void setVisible(boolean visible) {
        this.head.visible = visible;
        this.body.visible = visible;
        this.rightArm.visible = visible;
        this.leftArm.visible = visible;
        this.rightWing.visible = visible;
        this.leftWing.visible = visible;
    }

    public void setArmAngle(Arm arm, MatrixStack matrices) {
        this.head.rotate(matrices);
        this.body.rotate(matrices);
        matrices.translate(0.0D, -0.09375D, 0.09375D);
        matrices.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(this.rightArm.pitch + 0.43633232F));
        matrices.scale(0.7F, 0.7F, 0.7F);
        matrices.translate(0.0625D, 0.0D, 0.0D);
    }
}
