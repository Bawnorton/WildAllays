package com.bawnorton.wildallays.mixin;

import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnEggItem.class)
public abstract class SpawnEggMixin {

    @Redirect(method="use", at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;spawnFromItemStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"))
    public Entity useMixin(EntityType instance, ServerWorld world, ItemStack stack, PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
        if(isAllay(instance)) {
            BiomeAllay.Type allay = BiomeAllay.Type.fromBiome(world.getBiome(pos));
            return allay.entityType.spawnFromItemStack(world, stack, player, pos, spawnReason, alignPosition, invertY);
        }
        return instance.spawnFromItemStack(world, stack, player, pos, spawnReason, alignPosition, invertY);
    }

    @Redirect(method="useOnBlock", at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;spawnFromItemStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"))
    public Entity useOnBlockMixin(EntityType instance, ServerWorld world, ItemStack stack, PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
        if(isAllay(instance)) {
            BiomeAllay.Type allay = BiomeAllay.Type.fromBiome(world.getBiome(pos));
            return allay.entityType.spawnFromItemStack(world, stack, player, pos, spawnReason, alignPosition, invertY);
        }
        return instance.spawnFromItemStack(world, stack, player, pos, spawnReason, alignPosition, invertY);
    }

    private boolean isAllay(EntityType<?> type) {
        return type.equals(EntityType.ALLAY);
    }
}
