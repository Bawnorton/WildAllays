package com.bawnorton.wildallays.client;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.model.BiomeAllayModel;
import com.bawnorton.wildallays.renderer.BiomeAllayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class WildAllaysClient implements ClientModInitializer {
    public static final EntityModelLayer BIOME_ALLAY = new EntityModelLayer(new Identifier(WildAllays.MODID, "biome_allay"), "main");
    public static final EntityModelLayer BIOME_ALLAY_OUTER_LAYER = new EntityModelLayer(new Identifier(WildAllays.MODID, "biome_allay"), "outer_armor");


    @Override
    public void onInitializeClient() {
        registerEntityRenderers();
    }

    private void registerEntityRenderers() {
        for(BiomeAllay.Type allay: BiomeAllay.Type.values()) {
            EntityRendererRegistry.register(allay.entityType, BiomeAllayRenderer::new);
        }
        EntityModelLayerRegistry.registerModelLayer(BIOME_ALLAY, BiomeAllayModel::getTexturedModelData);
    }
}
