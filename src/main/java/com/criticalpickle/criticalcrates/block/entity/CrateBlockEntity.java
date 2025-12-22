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
import net.minecraft.world.ContainerHelper;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jetbrains.annotations.Nullable;

import static com.criticalpickle.criticalcrates.block.CrateBlock.SWITCH;

public class CrateBlockEntity extends BlockEntity implements MenuProvider {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);

    public CrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CRATE_BE.get(), pos, blockState);
    }

    public CrateBlockEntity(BlockEntityType entity, BlockPos pos, BlockState blockState) {
        super(entity, pos, blockState);
    }

    public void drop() {
        SimpleContainer containerInv = new SimpleContainer(inventory.size());
        for(int i = 0; i < inventory.size(); i++) {
            containerInv.setItem(i, inventory.get(i));
        }

        if(this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, containerInv);
        }
    }

    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    public void copyInventory(NonNullList<ItemStack> oldInventory) {
        if(oldInventory != null) {
            for(int i = 0; i < this.inventory.size(); i++) {
                this.inventory.set(i, oldInventory.get(i));
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

    public ResourceHandler<ItemResource> getItemHandler() {
        return new ItemStacksResourceHandler(inventory);
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
    protected void saveAdditional(ValueOutput input) {
        super.saveAdditional(input);
        ContainerHelper.saveAllItems(input, this.inventory);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.inventory = NonNullList.withSize(27, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.inventory);
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
