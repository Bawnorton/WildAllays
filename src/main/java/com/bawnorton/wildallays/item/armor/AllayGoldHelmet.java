package com.bawnorton.wildallays.item.armor;


import com.bawnorton.wildallays.registry.Materials;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AllayGoldHelmet extends AllayArmorItem {
    public AllayGoldHelmet(Settings settings) {
        super(Materials.ALLAY_GOLD_ARMOR_MATERIAL, settings);
    }

    @Override
    public ItemStack toVanillaItem(ItemStack item) {
        ItemStack armorItem = new ItemStack(Items.GOLDEN_HELMET, 1);
        armorItem.setNbt(item.getNbt());
        return armorItem;
    }
}
