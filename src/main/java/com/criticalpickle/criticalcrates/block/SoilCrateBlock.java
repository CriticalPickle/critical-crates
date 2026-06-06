package com.criticalpickle.criticalcrates.block;

import com.criticalpickle.criticalcrates.block.entity.SoilCrateBlockEntity;
import com.criticalpickle.criticalcrates.util.CacheSwitchInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;

public class SoilCrateBlock extends CrateBlock {
    public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE;

    public SoilCrateBlock(Properties properties) {
        super(properties.randomTicks());
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y).setValue(SWITCH, false)
                .setValue(EXPLOSION_RESIST, false).setValue(LAMP_UPGRADE, false).setValue(LIT, false)
                .setValue(POWERED, false).setValue(FIREPROOF, false).setValue(SLIMY, false)
                .setValue(MOISTURE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MOISTURE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if(blockState.getValue(SWITCH)) {
            SoilCrateBlockEntity crateBlockEntity = new SoilCrateBlockEntity(blockPos, blockState);
            crateBlockEntity.copyInventory(CacheSwitchInventory.getCache());
            return crateBlockEntity;
        }
        return new SoilCrateBlockEntity(blockPos, blockState);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int moisture = state.getValue(MOISTURE);

        // Check if water is nearby (vanilla helper method)
        if (!isNearWater(level, pos) && !level.isRainingAt(pos.above())) {
            if (moisture > 0) {
                level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
            }
        } else if (moisture < 7) {
            level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        }
    }

    /// Logic to check for water in a 4-block radius
    private static boolean isNearWater(LevelReader level, BlockPos pos) {
        for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
            if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean hasFireEffect(BlockState state) {
        return false;
    }

    @Override
    protected SoundEvent getInventoryOpenSound() {
        return SoundEvents.ROOTED_DIRT_HIT;
    }

    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
        return TriState.TRUE;
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(FarmBlock.MOISTURE) > 0;
    }
}