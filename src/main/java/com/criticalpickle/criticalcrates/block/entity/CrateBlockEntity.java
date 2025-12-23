package com.criticalpickle.criticalcrates.block.entity;

import com.criticalpickle.criticalcrates.registration.ModBlockEntities;
import com.criticalpickle.criticalcrates.screen.CrateMenu;
import com.criticalpickle.criticalcrates.util.CacheSwitchInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

import static com.criticalpickle.criticalcrates.block.CrateBlock.SWITCH;

public class CrateBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStacksResourceHandler inventory = new ItemStacksResourceHandler(27) {
        @Override
        protected void onContentsChanged(int index, ItemStack previousContents) {
            setChanged();
            if(level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private record SideHandler(ItemStacksResourceHandler handler, boolean isInsert, boolean isExtract) implements ResourceHandler<ItemResource> {
        @Override
        public int size() {
            return handler.size();
        }

        @Override
        public ItemResource getResource(int i) {
            return handler.getResource(i);
        }

        @Override
        public long getAmountAsLong(int i) {
            return handler.getAmountAsLong(i);
        }

        @Override
        public long getCapacityAsLong(int i, ItemResource resource) {
            return handler.getCapacityAsLong(i, resource);
        }

        @Override
        public boolean isValid(int i, ItemResource resource) {
            return handler.isValid(i, resource);
        }

        @Override
        public int insert(int i, ItemResource resource, int amount, TransactionContext transactionContext) {
            return isInsert ? handler.insert(i, resource, amount, transactionContext) : 0;
        }

        @Override
        public int extract(int i, ItemResource resource, int amount, TransactionContext transactionContext) {
            return isExtract ? handler.extract(i, resource, amount, transactionContext) : 0;
        }
    }


    public CrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CRATE_BE.get(), pos, blockState);
    }

    public CrateBlockEntity(BlockEntityType entity, BlockPos pos, BlockState blockState) {
        super(entity, pos, blockState);
    }

    public void drop() {
        SimpleContainer containerInv = new SimpleContainer(inventory.size());
        for(int i = 0; i < inventory.size(); i++) {
            containerInv.setItem(i, inventory.getResource(i).toStack());
        }

        if(this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, containerInv);
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if(state.getValue(SWITCH)) {
            CacheSwitchInventory.cache(getInventory());
        }
        else {
            drop();
        }
        super.preRemoveSideEffects(pos, state);
    }

    public ItemStacksResourceHandler getInventory() {
        return inventory;
    }

    public void copyInventory(ItemStacksResourceHandler oldInventory) {
        if(oldInventory != null) {
            for(int i = 0; i < this.inventory.size(); i++) {
                this.inventory.set(i, oldInventory.getResource(i), oldInventory.getAmountAsInt(i));
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

    public ResourceHandler<ItemResource> getInventorySide(Direction side) {
        if(side == Direction.DOWN) {
            return new SideHandler(inventory, false, true);
        }
        return new SideHandler(inventory, true, false);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        inventory.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        inventory.deserialize(input);

        if(input.child("inventory").isPresent()) {
            NonNullList<ItemStack> stacks = NonNullList.withSize(27, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(input.child("inventory").get(), stacks);
            ItemStacksResourceHandler temp = new ItemStacksResourceHandler(stacks);
            copyInventory(temp);
        }
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