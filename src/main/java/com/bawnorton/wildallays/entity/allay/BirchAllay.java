package com.bawnorton.wildallays.entity.allay;

import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BirchAllay extends BiomeAllay {
    public BirchAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
    }

//    @Override
//    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
//        ItemStack playerStack = player.getStackInHand(hand);
//        ItemStack allayStack = this.getStackInHand(Hand.MAIN_HAND);
//        if (allayStack.isEmpty() && !playerStack.isEmpty()) {
//            ItemStack given = playerStack.copy();
//            given.setCount(1);
//            this.setStackInHand(Hand.MAIN_HAND, given);
//        }
//        return ActionResult.SUCCESS;
//    }
}
