package com.criticalpickle.criticalcrates.block.entity;

import com.criticalpickle.criticalcrates.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SoilCrateBlockEntity extends CrateBlockEntity {
    public SoilCrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SOIL_CRATE_BE.get(), pos, blockState);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.criticalcrates.dirt_crate");
    }
}
