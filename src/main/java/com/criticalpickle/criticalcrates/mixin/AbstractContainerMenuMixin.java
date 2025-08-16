package com.criticalpickle.criticalcrates.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    // Remove crafting tag identifier if item is taken out of crafting grid by player
    @Inject(method = "clicked", at = @At("HEAD"))
    private void onClicked(int slotId, int button, ClickType clickType, Player player, CallbackInfo cir) {
        if(!((Object) this instanceof CraftingMenu craftingMenu)) return;  // check first
        ItemStack stack = ItemStack.EMPTY;
        if(slotId >= 0 && slotId < craftingMenu.slots.size()) {
            stack = craftingMenu.getSlot(slotId).getItem();
        }
        CustomData data = null;
        String stackItemID = null;
        if(!stack.isEmpty()) {
            data = stack.get(DataComponents.CUSTOM_DATA);
            stackItemID = stack.getItem().getDescriptionId();
        }
        boolean isValidCrateItem = data != null && stackItemID.contains("criticalcrates") && stackItemID.contains("crate");
        CompoundTag dataTag;

        // Crafting grid
        if(!stack.isEmpty() && slotId >= 1 && slotId <= 9) {
            if(isValidCrateItem) {
                dataTag = data.copyTag();

                if(data.contains("crafting_item")) {
                    dataTag.remove("crafting_item");
                }

                if(dataTag.contains("player_crafting")) {
                    dataTag.remove("player_crafting");
                }

                if(!dataTag.isEmpty()) {
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                }
                else {
                    stack.remove(DataComponents.CUSTOM_DATA);
                }
            }
        }

        // Player's inventory
        if(!stack.isEmpty() && slotId > 9 && slotId <= 45) {
            if(isValidCrateItem) {
                dataTag = data.copyTag();

                if(dataTag.contains("player_crafting")) {
                    dataTag.remove("player_crafting");
                }

                if(!dataTag.isEmpty()) {
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                }
                else {
                    stack.remove(DataComponents.CUSTOM_DATA);
                }
            }
        }
    }
}