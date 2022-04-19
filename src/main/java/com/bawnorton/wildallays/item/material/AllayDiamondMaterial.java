package com.bawnorton.wildallays.item.material;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class AllayDiamondMaterial implements AllayArmorMaterial {

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD ? 8 : 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
    }

    @Override
    public String getName() {
        return "allay_diamond";
    }
}
