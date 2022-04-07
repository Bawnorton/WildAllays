package com.bawnorton.wildallays.renderer;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.client.WildAllaysClient;
import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.model.BiomeAllayModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BiomeAllayRenderer extends MobEntityRenderer<BiomeAllay, BiomeAllayModel> {
    public BiomeAllayRenderer(EntityRendererFactory.Context context) {
        super(context, new BiomeAllayModel(context.getPart(WildAllaysClient.MODEL_BIOME_ALLAY_LAYER)), 0.4F);
    }

    @Override
    public Identifier getTexture(BiomeAllay entity) {
        return new Identifier(WildAllays.MODID, "textures/entity/biome_allay/biome_allay.png");
    }
}
