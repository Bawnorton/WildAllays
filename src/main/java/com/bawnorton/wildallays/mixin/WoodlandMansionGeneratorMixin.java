package com.bawnorton.wildallays.mixin;

import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.structure.WoodlandMansionGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.AbstractRandom;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(WoodlandMansionGenerator.Piece.class)
public class WoodlandMansionGeneratorMixin {

    private ServerWorldAccess world;

    @Inject(method="handleMetadata", at=@At("HEAD"))
    protected void saveWorld(String metadata, BlockPos pos, ServerWorldAccess world, AbstractRandom abstractRandom, BlockBox boundingBox, CallbackInfo ci) {
        this.world = world;
    }

    @SuppressWarnings("unchecked")
    @ModifyArg(method="handleMetadata", at=@At(value="INVOKE", target="Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2))
    public <E> E setAllayType(E e) {
       return (E) BiomeAllay.Type.LOST.entityType.create(world.toServerWorld());
    }
}
