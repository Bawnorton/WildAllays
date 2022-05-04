package com.bawnorton.wildallays.mixin;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.RangedApproachTask;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangedApproachTask.class)
public class RangedApproachTaskMixin {
    @Inject(method = "run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;J)V", at=@At("HEAD"))
    private void test(ServerWorld serverWorld, MobEntity mobEntity, long l, CallbackInfo ci) {
        if(mobEntity instanceof BiomeAllay allay) {
            LivingEntity target = allay.getTarget();
            if(target != null && !target.isDead()) {
                boolean visible = LookTargetUtil.isVisibleInMemory(mobEntity, target);
                boolean inRange = LookTargetUtil.isTargetWithinAttackRange(mobEntity, target, 1);
                WildAllays.LOGGER.info("RangedApproachTask: visible(%s) | inRange(%s)".formatted(
                        visible, inRange
                ));
            }
        }
    }
}
