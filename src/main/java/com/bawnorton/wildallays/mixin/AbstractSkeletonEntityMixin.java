package com.bawnorton.wildallays.mixin;

import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin {
    @Inject(method = "initGoals", at=@At("RETURN"))
    private void addAllayTarget(CallbackInfo ci) {
        AbstractSkeletonEntity instance = (AbstractSkeletonEntity) (Object) this;
        ((MobEntityAccessor)instance).getTargetSelector().add(3, new ActiveTargetGoal<>(instance, BiomeAllay.class, true));
    }

}
