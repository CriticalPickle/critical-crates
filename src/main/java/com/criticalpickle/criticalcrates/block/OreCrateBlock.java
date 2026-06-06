package com.criticalpickle.criticalcrates.block;

import com.criticalpickle.criticalcrates.block.entity.OreCrateBlockEntity;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModTags;
import com.criticalpickle.criticalcrates.util.CacheSwitchInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
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

public class OreCrateBlock extends CrateBlock {
    protected String oreType, crateType;
    private final int inventorySize;
    public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE;

    public OreCrateBlock(Properties properties, String oreType, String crateType) {
        super(!crateType.equalsIgnoreCase("soil") ? properties : properties.randomTicks());
        this.oreType = oreType.toLowerCase();
        this.crateType = crateType.toLowerCase();
        if (this.oreType.equals("iron")) {
            this.inventorySize = 54;
        }
        else {
            this.inventorySize = 27;
        }

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
            OreCrateBlockEntity crateBlockEntity = new OreCrateBlockEntity(blockPos, blockState, this.inventorySize);
            crateBlockEntity.copyInventory(CacheSwitchInventory.getCache());
            return crateBlockEntity;
        }
        return new OreCrateBlockEntity(blockPos, blockState, this.inventorySize);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.isCrateType("soil")) {
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
        return state.getBlock() instanceof OreCrateBlock oreCrate && oreCrate.isCrateType("wood");
    }

    @Override
    protected boolean hasUpgrades(BlockState state) {
        return super.hasUpgrades(state)
                || (state.getBlock() instanceof OreCrateBlock oreCrate && !oreCrate.isCrateType("ore"));
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return this.isCrateType("glass") ? 1.0f : 0.0f;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return this.isCrateType("glass");
    }

    @Override
    public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
        return this.isCrateType("soil") ? TriState.TRUE : TriState.FALSE;
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
        return this.isCrateType("soil") && state.getValue(FarmBlock.MOISTURE) > 0;
    }

    @Override
    protected SoundEvent getInventoryOpenSound() {
        if(this.isCrateType("wood")) {
            return SoundEvents.BARREL_OPEN;
        }
        else if(this.isCrateType("glass")) {
            return SoundEvents.COPPER_BULB_PLACE;
        }
        else if(this.isCrateType("soil")) {
            return SoundEvents.ROOTED_DIRT_HIT;
        }
        return SoundEvents.IRON_TRAPDOOR_OPEN;
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return state.getValue(EXPLOSION_RESIST) ? 1200f : 6f;
    }

    public String getOreType() {
        return oreType;
    }

    public String getCrateType() {
        return crateType;
    }

    /// Returns if ore crate ore type is equal to parameter type.
    public boolean isOreType(String type) {
        return this.oreType.equals(type);
    }

    /// Returns if ore crate type is equal to parameter type.
    public boolean isCrateType(String type) {
        return this.crateType.equals(type);
    }
}
