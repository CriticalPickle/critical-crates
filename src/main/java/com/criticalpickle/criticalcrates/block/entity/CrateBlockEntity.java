package com.criticalpickle.criticalcrates.block.entity;

import com.criticalpickle.criticalcrates.registration.ModBlockEntities;
import com.criticalpickle.criticalcrates.screen.CrateMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import static com.criticalpickle.criticalcrates.block.CrateBlock.SWITCH;

public class CrateBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler inventory = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private record SideHandler(ItemStackHandler handler, boolean isInsert, boolean isExtract) implements IItemHandler {
        @Override
        public int getSlots() {
            return handler.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int i) {
            return handler.getStackInSlot(i);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return isInsert ? handler.insertItem(slot, stack, simulate) : stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return isExtract ? handler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int i) {
            return handler.getSlotLimit(i);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return isInsert && handler.isItemValid(slot, stack);
        }
    }

    public CrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CRATE_BE.get(), pos, blockState);
    }

    public CrateBlockEntity(BlockEntityType entity, BlockPos pos, BlockState blockState) {
        super(entity, pos, blockState);
    }

    public void drop() {
        SimpleContainer containerInv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            containerInv.setItem(i, inventory.getStackInSlot(i));
        }

        if(this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, containerInv);
        }
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public void copyInventory(ItemStackHandler oldInventory) {
        if(oldInventory != null) {
            for(int i = 0; i < this.inventory.getSlots(); i++) {
                this.inventory.setStackInSlot(i, oldInventory.getStackInSlot(i));
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(this.getLevel() != null && !this.getLevel().isClientSide() && this.getBlockState().getValue(SWITCH)) {
            this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(SWITCH, false));
        }
    }

    public IItemHandler getInventorySide(Direction side) {
        if(side == Direction.DOWN) {
            return new SideHandler(inventory, false, true);
        }
        return new SideHandler(inventory, true, false);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.criticalcrates.crate");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new CrateMenu(id, inventory, this);
    }
}
