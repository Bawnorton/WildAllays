package com.bawnorton.wildallays.mixin;

import com.bawnorton.wildallays.client.WildAllaysClient;
import com.bawnorton.wildallays.model.BiomeAllayModel;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityModels.class)
public class EntityModelsMixin {
    @Redirect(method = "getModels", at=@At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;"))
    private static ImmutableMap.Builder<EntityModelLayer, TexturedModelData> addAllayArmorModel() {
        ImmutableMap.Builder<EntityModelLayer, TexturedModelData> builder = ImmutableMap.builder();
        builder.put(WildAllaysClient.BIOME_ALLAY_OUTER_LAYER, TexturedModelData.of(BiomeAllayModel.getModelData(new Dilation(0.4F)), 32, 32));
        return builder;
    }
}
