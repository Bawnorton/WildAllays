package com.bawnorton.wildallays.mixin;

import com.bawnorton.wildallays.entity.enums.Allay;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public abstract class SpawnEggMixin {

    @Shadow @Final private EntityType<?> type;

    @Inject(method="useOnBlock", at=@At("HEAD"), cancellable = true)
    public void setAllayType(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(isAllay(type)) {
            cir.setReturnValue(Allay.egg.useOnBlock(context));
        }
    }

    private boolean isAllay(EntityType<?> type) {
        return type.equals(EntityType.ALLAY);
    }
}
