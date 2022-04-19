package com.bawnorton.wildallays.item.armor;

import com.bawnorton.wildallays.registry.Materials;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AllayDiamondHelmet extends AllayArmorItem {
    public AllayDiamondHelmet(Settings settings) {
        super(Materials.ALLAY_DIAMOND_ARMOR_MATERIAL, settings);
    }

    @Override
    public ItemStack toVanillaItem(ItemStack item) {
        ItemStack armorItem = new ItemStack(Items.DIAMOND_HELMET, 1);
        armorItem.setNbt(item.getNbt());
        return armorItem;
    }
}
