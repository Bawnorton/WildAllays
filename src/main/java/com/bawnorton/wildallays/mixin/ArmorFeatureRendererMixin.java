package com.bawnorton.wildallays.mixin;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.item.armor.AllayArmorItem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private <T extends LivingEntity, A extends BipedEntityModel<T>> void checkAllayArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
        ItemStack itemStack = entity.getEquippedStack(armorSlot);
        if(!(entity instanceof BiomeAllay) && itemStack.getItem() instanceof AllayArmorItem) {
            ci.cancel();
        }
    }
}
