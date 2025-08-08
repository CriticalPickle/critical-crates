package com.criticalpickle.criticalcrates.block.entity;

import com.criticalpickle.criticalcrates.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GlassCrateBlockEntity extends CrateBlockEntity {
    private float rotation;

    public GlassCrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.GLASS_CRATE_BE.get(), pos, blockState);
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if(rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }
}
