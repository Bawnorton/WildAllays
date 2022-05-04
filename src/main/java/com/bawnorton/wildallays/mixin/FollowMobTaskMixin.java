package com.bawnorton.wildallays.mixin;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.FollowMobTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FollowMobTask.class)
public class FollowMobTaskMixin {
    @Shadow @Final private float maxDistanceSquared;

    @Inject(method = "shouldRun", at = @At("HEAD"))
    private void test(ServerWorld world, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof BiomeAllay allay) {
            LivingEntity target = allay.getTarget();
            if(target != null && !target.isDead()) {
                double dist = MathHelper.floor(target.squaredDistanceTo(allay));
                WildAllays.LOGGER.info("FollowMobTask: %s[%s] is %s units away. max (%s)".formatted(
                        target.getClass().getSimpleName(), target.getBlockPos(), dist, maxDistanceSquared
                ));
            }
        }
    }

    @Inject(method = "run", at = @At("HEAD"))
    private void test2(ServerWorld world, LivingEntity entity, long time, CallbackInfo ci) {
        WildAllays.LOGGER.info("FollowMobTask: running");
    }

}
