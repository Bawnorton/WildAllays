package com.bawnorton.wildallays.item.material;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class AllayChainMaterial implements AllayArmorMaterial {

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD ? 5 : 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_CHAIN;
    }

    @Override
    public String getName() {
        return "allay_chain";
    }
}
