package com.bawnorton.wildallays.renderer;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.model.BiomeAllayModel;
import com.google.common.collect.Maps;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BiomeAllayArmourRenderer<T extends BiomeAllay, M extends BiomeAllayModel<T>, A extends BiomeAllayModel<T>> extends FeatureRenderer<T, M> {
    private static final Map<String, Identifier> ARMOUR_TEXTURE_CACHE = Maps.newHashMap();
    private final A bodyModel;

    public BiomeAllayArmourRenderer(FeatureRendererContext<T, M> context, A bodyModel) {
        super(context);
        this.bodyModel = bodyModel;
    }

    protected void setVisible(A bipedModel) {
        bipedModel.setVisible(false);
        bipedModel.head.visible = true;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        this.renderHelmet(matrices, vertexConsumers, entity, light, this.bodyModel);
    }

    private void renderHelmet(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, int light, A model) {
        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.HEAD);
        if(itemStack.getItem() instanceof ArmorItem armorItem) {
            if(armorItem.getSlotType() == EquipmentSlot.HEAD) {
                this.getContextModel().setAttributes(model);
                this.setVisible(model);
                boolean isEnchanted = itemStack.hasGlint();
                matrices.push();
                matrices.scale(0.75F, 0.75F, 0.75F);
                matrices.translate(0.0, 0.5, 0);
                matrices.pop();
                if(armorItem instanceof DyeableArmorItem dyeableArmor) {
                    int i = dyeableArmor.getColor(itemStack);
                    float f = (float)(i >> 16 & 255) / 255.0F;
                    float g = (float)(i >> 8 & 255) / 255.0F;
                    float h = (float)(i & 255) / 255.0F;
                    this.renderArmorParts(matrices, vertexConsumers, light, armorItem, isEnchanted, model, f, g, h, null);
                    this.renderArmorParts(matrices, vertexConsumers, light, armorItem, isEnchanted, model, 1.0F, 1.0F, 1.0F, "overlay");
                } else {
                    this.renderArmorParts(matrices, vertexConsumers, light, armorItem, isEnchanted, model, 1.0F, 1.0F, 1.0F, null);
                }
            }
        }
    }

    private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean usesSecondLayer, A model, float red, float green, float blue, @Nullable String overlay) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(this.getArmorTexture(item, overlay)), false, usesSecondLayer);
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
    }

    private Identifier getArmorTexture(ArmorItem item, @Nullable String overlay) {
        String materialName = item.getMaterial().getName();
        String id = "textures/models/armor/" + materialName + "_layer_" + 1 + (overlay == null ? "" : "_" + overlay) + ".png";
        return ARMOUR_TEXTURE_CACHE.computeIfAbsent(id, Identifier::new);
    }
}
