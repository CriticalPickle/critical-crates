package com.criticalpickle.criticalcrates.screen;

import com.criticalpickle.criticalcrates.registration.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LargeCrateMenu extends CrateMenu {
    public LargeCrateMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public LargeCrateMenu(int containerId, Inventory inventory, BlockEntity blockEntity) {
        super(ModMenuTypes.LARGE_CRATE_MENU.get(), containerId, inventory, blockEntity);
    }

    @Override
    protected int GET_TE_INVENTORY_SLOT_COUNT() {
        return 54;
    }

    @Override
    protected int getPlayerInventoryY() {
        return 103 + ((getRows() - 4) * 18);
    }

    @Override
    protected int getPlayerHotbarY() {
        return 161 + ((getRows() - 4) * 18);
    }

    @Override
    protected int getCrateY() {
        return 18;
    }
}
