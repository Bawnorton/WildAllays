package com.bawnorton.wildallays.client;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.model.BiomeAllayModel;
import com.bawnorton.wildallays.renderer.BiomeAllayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import static com.bawnorton.wildallays.registry.EntityRegister.ENTITIES;

public class WildAllaysClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_BIOME_ALLAY_LAYER = new EntityModelLayer(new Identifier(WildAllays.MODID, "biome_allay"), "main");

    @Override
    public void onInitializeClient() {
        registerEntityRenderers();
    }

    private void registerEntityRenderers() {
        for(String key: ENTITIES.keySet()) {
            EntityRendererRegistry.register(ENTITIES.get(key).getEntityType(), BiomeAllayRenderer::new);
        }
        EntityModelLayerRegistry.registerModelLayer(MODEL_BIOME_ALLAY_LAYER, BiomeAllayModel::getTexturedModelData);
    }
}
