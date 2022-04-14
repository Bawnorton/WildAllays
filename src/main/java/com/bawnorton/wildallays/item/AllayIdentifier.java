package com.bawnorton.wildallays.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Hashtable;

public class AllayIdentifier extends Item {
    private static final Hashtable<PlayerEntity, Boolean> playersHolding = new Hashtable<>();

    public AllayIdentifier(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(entity instanceof PlayerEntity player) {
            playersHolding.put(player, selected);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public static boolean heldByPlayer(PlayerEntity player) {
        try {
            return playersHolding.get(player);
        } catch (NullPointerException e) {
            return false;
        }
    }
}
