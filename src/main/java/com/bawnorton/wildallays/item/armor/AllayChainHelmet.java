package com.bawnorton.wildallays.item.armor;

import com.bawnorton.wildallays.registry.Materials;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AllayChainHelmet extends AllayArmorItem {
    public AllayChainHelmet(Settings settings) {
        super(Materials.ALLAY_CHAIN_ARMOR_MATERIAL, settings);
    }

    @Override
    public ItemStack toVanillaItem(ItemStack item) {
        ItemStack armorItem = new ItemStack(Items.CHAINMAIL_HELMET, 1);
        armorItem.setNbt(item.getNbt());
        return armorItem;
    }
}
