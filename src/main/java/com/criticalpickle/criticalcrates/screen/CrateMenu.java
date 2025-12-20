package com.criticalpickle.criticalcrates.screen;

import com.criticalpickle.criticalcrates.block.entity.CrateBlockEntity;
import com.criticalpickle.criticalcrates.block.entity.GlassCrateBlockEntity;
import com.criticalpickle.criticalcrates.registration.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CrateMenu extends AbstractContainerMenu {
    public final CrateBlockEntity blockEntity;
    private final Level level;

    public CrateMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public CrateMenu(int containerId, Inventory inventory, BlockEntity blockEntity) {
        super(ModMenuTypes.CRATE_MENU.get(), containerId);
        this.blockEntity = ((CrateBlockEntity) blockEntity);
        this.level = inventory.player.level();

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        addCrateInventory();
    }

    // CREDIT FOR STACK MOVING: diesieben07 | https://github.com/diesieben07/SevenCommons
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 27;

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

    private void addPlayerInventory(Inventory inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    private void addCrateInventory() {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
//                this.addSlot(new SlotItemHandler(this.blockEntity.inventory, l + i * 9, 8 + l * 18, 16 + i * 18));
            }
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (!player.level().isClientSide()) {
            if(blockEntity instanceof GlassCrateBlockEntity) {
                player.level().playSound(null, blockEntity.getBlockPos(), SoundEvents.COPPER_BREAK, SoundSource.BLOCKS, 0.5f, 1.0f);
            }
            else {
                player.level().playSound(null, blockEntity.getBlockPos(), SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.5f, 1.0f);
            }
        }
    }
}
