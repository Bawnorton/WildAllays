package com.bawnorton.wildallays.model;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BiomeAllayEyeLayer extends EyesFeatureRenderer<BiomeAllay, BiomeAllayModel> {
    private static final RenderLayer EYES = RenderLayer.getEyes(new Identifier(WildAllays.MODID, "textures/entity/biome_allay/biome_allay_eyes.png"));

    public BiomeAllayEyeLayer(FeatureRendererContext<BiomeAllay, BiomeAllayModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, BiomeAllay entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.getEyesTexture());
        this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 0);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return EYES;
    }
}
