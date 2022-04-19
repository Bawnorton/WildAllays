package com.bawnorton.wildallays.item.material;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class AllayIronMaterial implements AllayArmorMaterial {

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD ? 6 : 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
    }

    @Override
    public String getName() {
        return "allay_iron";
    }
}
