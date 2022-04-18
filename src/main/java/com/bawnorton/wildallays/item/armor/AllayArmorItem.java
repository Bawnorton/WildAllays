package com.bawnorton.wildallays.item.armor;

import com.bawnorton.wildallays.item.material.AllayArmorMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public abstract class AllayArmorItem extends ArmorItem {
    public AllayArmorItem(AllayArmorMaterial material, Settings settings) {
        super(material, EquipmentSlot.HEAD, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    public ItemStack fromVanillaItem(ItemStack item) {
        ItemStack armorItem = new ItemStack(this, 1);
        armorItem.setNbt(item.getNbt());
        return armorItem;
    }

    public abstract ItemStack toVanillaItem(ItemStack item);
}
