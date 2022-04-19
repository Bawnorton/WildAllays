package com.bawnorton.wildallays.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AllayEntity.class)
public class AllayEntityMixin {
    @Inject(method = "interactMob", at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/Brain;forget(Lnet/minecraft/entity/ai/brain/MemoryModuleType;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void dontForget(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        AllayEntity instance = (AllayEntity) (Object) this;
        if(instance.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
            instance.getBrain().forget(MemoryModuleType.LIKED_PLAYER);
        }
        player.giveItemStack(instance.getStackInHand(Hand.MAIN_HAND));
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}
