package com.bawnorton.wildallays.model;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.util.Identifier;

public class BiomeAllayEyeLayer extends EyesFeatureRenderer<BiomeAllay, BiomeAllayModel> {
    private static final RenderLayer EYES = RenderLayer.getEyes(new Identifier(WildAllays.MODID, "textures/entity/biome_allay/biome_allay_eyes.png"));

    public BiomeAllayEyeLayer(FeatureRendererContext<BiomeAllay, BiomeAllayModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return EYES;
    }
}
