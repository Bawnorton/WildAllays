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

public class BiomeAllayRenderer extends MobEntityRenderer<BiomeAllay, BiomeAllayModel> {
    public BiomeAllayRenderer(EntityRendererFactory.Context context) {
        super(context, new BiomeAllayModel(context.getPart(WildAllaysClient.MODEL_BIOME_ALLAY_LAYER)), 0.4F);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.method_43338()));
        this.addFeature(new BiomeAllayEyeLayer(this));
    }

    @Override
    public Identifier getTexture(BiomeAllay entity) {
        if(entity instanceof LostAllay && !ConfigManager.get("lost_allay_camoflages", Boolean.class)) {
            return new Identifier(WildAllays.MODID, "textures/entity/biome_allay/lost_allay.png");
        }
        return new Identifier(WildAllays.MODID, "textures/entity/biome_allay/biome_allay_template.png");
    }

    @Override
    protected int getBlockLight(BiomeAllay entity, BlockPos pos) {
        if(ConfigManager.get("allay_glow", Boolean.class)) {
            long l = entity.getWorld().getTime() + (long) Math.abs(entity.getUuid().hashCode());
            float f = (float) Math.abs(l % 120L - 60L);
            float g = f / 60.0F;
            return (int) MathHelper.lerp(g, 5.0F, 15.0F);
        }
        return super.getBlockLight(entity, pos);
    }
}
