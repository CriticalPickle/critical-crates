package com.criticalpickle.criticalcrates.block.entity;

import com.criticalpickle.criticalcrates.registration.ModBlockEntities;
import com.criticalpickle.criticalcrates.screen.CrateMenu;
import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jetbrains.annotations.Nullable;

import static com.criticalpickle.criticalcrates.block.CrateBlock.SWITCH;

public class CrateBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStacksResourceHandler inventory = new ItemStacksResourceHandler(27) {
        @Override
        protected void onContentsChanged(int index, ItemStack previousContents) {
            setChanged();
            if(level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

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

//    @Override
//    public ResourceHandler<ItemResource> getSideHandler(Direction side) {
//        ResourceHandler<ItemResource> handler = getItemHandler();
//        if (side == Direction.DOWN) {
//            return handler.filterInsert(res -> false)  // extract-only
//                    .filterExtract(res -> true);
//        } else {
//            return handler.filterInsert(res -> true)  // insert-only
//                    .filterExtract(res -> false);
//        }
//    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        inventory.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        if(input.child("Items").isPresent()) {
            inventory.deserialize(input);
        }
        else if(input.child("inventory").isPresent()) {
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