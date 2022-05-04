package com.bawnorton.wildallays.renderer;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.client.WildAllaysClient;
import com.bawnorton.wildallays.config.ConfigManager;
import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.entity.allay.LostAllay;
import com.bawnorton.wildallays.model.BiomeAllayEyeLayer;
import com.bawnorton.wildallays.model.BiomeAllayModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class BiomeAllayRenderer extends MobEntityRenderer<BiomeAllay, BiomeAllayModel<BiomeAllay>> {
    public BiomeAllayRenderer(EntityRendererFactory.Context context) {
        super(context, new BiomeAllayModel<>(context.getPart(WildAllaysClient.BIOME_ALLAY)), 0.4F);
        this.addFeature(new BiomeAllayArmourRenderer<>(this,
                new BiomeAllayModel<>(context.getPart(WildAllaysClient.BIOME_ALLAY_OUTER_LAYER))));
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new BiomeAllayEyeLayer(this));
    }

    @Override
    public Identifier getTexture(BiomeAllay entity) {
        if(entity instanceof LostAllay) {
            if(!ConfigManager.get("lost_allay_camoflages", Boolean.class)) {
                return new Identifier(WildAllays.MODID, "textures/entity/biome_allay/lost_allay.png");
            } else {
                return new Identifier(WildAllays.MODID, "textures/entity/biome_allay/lost_allay_template.png");
            }
        }
        return new Identifier(WildAllays.MODID, "textures/entity/biome_allay/%s_allay.png".formatted(BiomeAllay.Type.fromClass(entity.getClass()).name));
    }

    @Override
    protected int getBlockLight(BiomeAllay entity, BlockPos pos) {
        if(ConfigManager.get("allay_glow", Boolean.class)) {
            return 15;
        }
        return super.getBlockLight(entity, pos);
    }
}
