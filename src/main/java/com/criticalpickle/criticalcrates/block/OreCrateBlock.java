package com.criticalpickle.criticalcrates.block;

import com.criticalpickle.criticalcrates.block.entity.OreCrateBlockEntity;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModTags;
import com.criticalpickle.criticalcrates.util.CacheSwitchInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class OreCrateBlock extends CrateBlock {
    protected String type;
    private final int size;

    public OreCrateBlock(Properties properties, String oreType, int inventorySize) {
        super(properties);
        this.type = oreType;
        this.size = inventorySize;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if(blockState.getValue(SWITCH)) {
            OreCrateBlockEntity crateBlockEntity = new OreCrateBlockEntity(blockPos, blockState, this.size);
            crateBlockEntity.copyInventory(CacheSwitchInventory.getCache());
            return crateBlockEntity;
        }
        return new OreCrateBlockEntity(blockPos, blockState, this.size);
    }

    @Override
    protected boolean hasFireEffect(BlockState state) {
        return !state.is(ModBlocks.IRON_CRIMSON_CRATE.get()) && !state.is(ModBlocks.IRON_WARPED_CRATE.get())
                && !state.is(ModTags.Blocks.ORE_CRATES) && !state.getBlock().getDescriptionId().contains("glass");
    }

    @Override
    protected boolean hasUpgrades(BlockState state) {
        return super.hasUpgrades(state) || (!state.is(ModTags.Blocks.ORE_CRATES) && state.getBlock().getDescriptionId().contains("iron"));
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock().getDescriptionId().contains("glass") ? 1.0f : 0.0f;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock().getDescriptionId().contains("glass");
    }

    @Override
    protected SoundEvent getInventoryOpenSound() {
        if(this.equals(ModBlocks.IRON_CRATE.get())) {
            return SoundEvents.IRON_TRAPDOOR_OPEN;
        }
        else if(this.getDescriptionId().contains("glass")) {
            return SoundEvents.COPPER_BULB_PLACE;
        }
        else if(this.type.equals("iron")) {
            return SoundEvents.BARREL_OPEN;
        }
        return SoundEvents.IRON_TRAPDOOR_OPEN;
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return state.getValue(EXPLOSION_RESIST) ? 1200f : 6f;
    }

    public String getType() {
        return type;
    }
}
