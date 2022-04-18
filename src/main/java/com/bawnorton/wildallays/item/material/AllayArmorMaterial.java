package com.bawnorton.wildallays.item.material;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;

public interface AllayArmorMaterial extends ArmorMaterial {
    @Override
    default int getDurability(EquipmentSlot slot) {
        return 0;
    }

    @Override
    default int getEnchantability() {
        return 0;
    }

    @Override
    default Ingredient getRepairIngredient() {
        return null;
    }

    @Override
    default float getToughness() {
        return 0;
    }

    @Override
    default float getKnockbackResistance() {
        return 0;
    }
}
