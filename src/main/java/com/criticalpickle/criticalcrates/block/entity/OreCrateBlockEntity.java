package com.criticalpickle.criticalcrates.block.entity;

import com.criticalpickle.criticalcrates.block.OreCrateBlock;
import com.criticalpickle.criticalcrates.registration.ModBlockEntities;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.screen.LargeCrateMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OreCrateBlockEntity extends CrateBlockEntity {
    public ItemStackHandler inventory;
    private float rotation;

    public OreCrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ORE_CRATE_BE.get(), pos, blockState);
        inventory = new ItemStackHandler(54) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }
        };
    }

    public OreCrateBlockEntity(BlockPos pos, BlockState blockState, int inventorySize) {
        super(ModBlockEntities.ORE_CRATE_BE.get(), pos, blockState);
        inventory = new ItemStackHandler(inventorySize) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }
        };
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public @NotNull Component getDisplayName() {
        if(this.getBlockState().is(ModBlocks.IRON_CRATE.get())) {
            return Component.translatable("container.criticalcrates.iron_crate");
        }
        else if(this.getBlockState().getBlock().getDescriptionId().contains("glass")) {
            return Component.translatable("container.criticalcrates.glass_crate");
        }
        else if(this.getBlockState().getBlock() instanceof OreCrateBlock block && block.getType().equals("iron")) {
            return Component.translatable("container.criticalcrates.crate");
        }
        return Component.translatable("container.criticalcrates.ore_crate");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        // Future use may require different menus for different ores.
        return new LargeCrateMenu(id, inventory, this);
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if(rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }
}
