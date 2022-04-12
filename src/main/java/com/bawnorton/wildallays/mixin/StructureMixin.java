package com.bawnorton.wildallays.mixin;

import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.Structure;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(Structure.class)
public class StructureMixin {
    @Redirect(method="getEntity", at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;getEntityFromNbt(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/world/World;)Ljava/util/Optional;"))
    private static Optional<Entity> setAllay(NbtCompound nbt, World world) {
        Optional<Entity> optionalEntity = EntityType.getEntityFromNbt(nbt, world);
        if(optionalEntity.isPresent()) {
            Entity entity = optionalEntity.get();
            if(entity instanceof AllayEntity) {
                return Optional.ofNullable(BiomeAllay.Type.LOST.entityType.create(world));
            }
        }
        return optionalEntity;
    }
}
