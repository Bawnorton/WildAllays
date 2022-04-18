package com.bawnorton.wildallays.item.material;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class AllayGoldMaterial implements AllayArmorMaterial {

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD ? 1 : 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_GOLD;
    }

    @Override
    public String getName() {
        return "allay_gold";
    }
}
