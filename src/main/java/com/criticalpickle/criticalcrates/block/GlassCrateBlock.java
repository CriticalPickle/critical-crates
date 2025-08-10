package com.criticalpickle.criticalcrates.block;

import com.criticalpickle.criticalcrates.block.entity.GlassCrateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GlassCrateBlock extends CrateBlock {
    public GlassCrateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GlassCrateBlockEntity(blockPos, blockState);
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0f;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    protected boolean hasFireEffect(BlockState state) {
        return false;
    }

    @Override
    protected SoundEvent getInventoryOpenSound() {
        return SoundEvents.COPPER_BULB_PLACE;
    }
}