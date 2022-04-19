package com.bawnorton.wildallays.item.armor;

import com.bawnorton.wildallays.registry.Materials;
import net.minecraft.item.*;

public class AllayLeatherHelmet extends AllayArmorItem implements DyeableItem {
    public AllayLeatherHelmet(Settings settings) {
        super(Materials.ALLAY_LEATHER_ARMOR_MATERIAL, settings);
    }

    @Override
    public ItemStack toVanillaItem(ItemStack item) {
        ItemStack armorItem = new ItemStack(Items.LEATHER_HELMET, 1);
        armorItem.setNbt(item.getNbt());
        return armorItem;
    }
}
