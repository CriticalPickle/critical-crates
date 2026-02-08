package com.criticalpickle.criticalcrates.screen;

import com.criticalpickle.criticalcrates.block.entity.CrateBlockEntity;
import com.criticalpickle.criticalcrates.block.entity.OreCrateBlockEntity;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CrateMenu extends AbstractContainerMenu {
    public final CrateBlockEntity blockEntity;
    protected final Level level;

    public CrateMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public CrateMenu(MenuType<?> menuType, int containerId, Inventory inventory, BlockEntity blockEntity) {
        super(menuType, containerId);
        this.blockEntity = ((CrateBlockEntity) blockEntity);
        this.level = inventory.player.level();

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        addCrateInventory();
    }

    public CrateMenu(int containerId, Inventory inventory, BlockEntity blockEntity) {
        super(ModMenuTypes.CRATE_MENU.get(), containerId);
        this.blockEntity = ((CrateBlockEntity) blockEntity);
        this.level = inventory.player.level();

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        addCrateInventory();
    }

    /// Get the TE_INVENTORY_SLOT_COUNT variable for quick moving/stacking
    protected int GET_TE_INVENTORY_SLOT_COUNT() {
        return 27;
    }

    // CREDIT FOR STACK MOVING: diesieben07 | https://github.com/diesieben07/SevenCommons
    protected static final int HOTBAR_SLOT_COUNT = 9;
    protected static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    protected static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    protected static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    protected static final int VANILLA_FIRST_SLOT_INDEX = 0;
    protected static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    protected int TE_INVENTORY_SLOT_COUNT = GET_TE_INVENTORY_SLOT_COUNT();

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }

    /// Get the number of rows for the crate menu
    protected int getRows() {
        return this.blockEntity.getInventory().getSlots() / 9;
    }

    /// Get the y coordinate for the player's inventory within the menu
    protected int getPlayerInventoryY() {
        return 84;
    }

    /// Get the y coordinate for the player's hotbar within the menu
    protected int getPlayerHotbarY() {
        return 142;
    }

    /// Get the y coordinate for the crate menu
    protected int getCrateY() {
        return 16;
    }

    ///  Add the player's inventory slots
    protected void addPlayerInventory(Inventory inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inventory, l + i * 9 + 9, 8 + l * 18, getPlayerInventoryY() + i * 18));
            }
        }
    }

    /// Add the player's hotbar slots
    protected void addPlayerHotbar(Inventory inventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, getPlayerHotbarY()));
        }
    }

    /// Add the inventory of the crate
    protected void addCrateInventory() {
        for (int i = 0; i < getRows(); ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new SlotItemHandler(this.blockEntity.getInventory(), l + i * 9, 8 + l * 18, getCrateY() + i * 18));
            }
        }
    }

    /// Get the sound to play when closing the menu
    protected SoundEvent getMenuRemoveSound() {
        if(blockEntity.getBlockState().getBlock().getDescriptionId().contains("glass")) {
            return SoundEvents.COPPER_BREAK;
        }
        else if(blockEntity instanceof OreCrateBlockEntity && blockEntity.getBlockState().is(ModBlocks.IRON_CRATE.get())) {
            return SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE;
        }
        else {
            return SoundEvents.BARREL_CLOSE;
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (!player.level().isClientSide) {
            player.level().playSound(null, blockEntity.getBlockPos(), getMenuRemoveSound(), SoundSource.BLOCKS, 0.5f, 1.0f);
        }
    }
}
