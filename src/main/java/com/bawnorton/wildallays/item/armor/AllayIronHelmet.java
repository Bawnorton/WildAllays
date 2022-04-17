package com.bawnorton.wildallays.item.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static com.bawnorton.wildallays.registry.ItemRegister.ALLAY_IRON_ARMOR_MATERIAL;

public class AllayIronHelmet extends ArmorItem {
    public AllayIronHelmet(Settings settings) {
        super(ALLAY_IRON_ARMOR_MATERIAL, EquipmentSlot.HEAD, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.fail(user.getStackInHand(hand));
    }
}
