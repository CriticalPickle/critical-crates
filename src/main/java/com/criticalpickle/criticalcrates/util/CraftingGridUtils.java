package com.criticalpickle.criticalcrates.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class CraftingGridUtils {
    public static int getItemCount(Item item, CraftingMenu craftingMenu) {
        ItemStack stack;
        int itemCount = 0;
        for(int i = 1; i <= 9; i++) {
            stack = craftingMenu.getSlot(i).getItem();
            if(stack.getItem() == item) {
                itemCount += stack.getCount();
            }
        }
        return itemCount;
    }

    public static int getItemIDWithDataTagCount(String itemID, CompoundTag dataTag, CraftingMenu craftingMenu) {
        ItemStack stack;
        int itemCount = 0;
        for(int i = 1; i <= 9; i++) {
            stack = craftingMenu.getSlot(i).getItem();

            if(!stack.isEmpty() && stack.getItem().getDescriptionId().equals(itemID)
                    && DataComponentUtils.doesStackCustomDataEqualCustomData(stack, CustomData.of(dataTag))) {
                itemCount += stack.getCount();
            }
        }
        return itemCount;
    }

    public static void returnCraftingItem(Item item, int count, CompoundTag dataTag, Inventory inventory) {
        ItemStack returnedStack = new ItemStack(item);
        returnedStack.setCount(count);
        if(dataTag != null) {
            returnedStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
            if(dataTag.contains("fireproof") && dataTag.getBoolean("fireproof")) {
                returnedStack.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
            }
        }
        inventory.add(returnedStack);
    }

    public static int getLowestSlotCountOf(Item item, ServerPlayer player) {
        CraftingMenu craftingMenu = (CraftingMenu) player.containerMenu;
        ItemStack stack;
        int lowestCount = 64;

        for(int i = 1; i <= 9; i++) {
            stack = craftingMenu.getSlot(i).getItem();
            if(!stack.isEmpty() && stack.getCount() < lowestCount
                    && stack.getItem().getDescriptionId().equals(item.getDescriptionId())) {
                lowestCount = stack.getCount();
            }
        }

        return lowestCount;
    }

    public static int getHighestSlotCountOf(Item item, CraftingMenu craftingMenu) {
        ItemStack stack;
        int highestCount = 0;

        for(int i = 1; i <= 9; i++) {
            stack = craftingMenu.getSlot(i).getItem();
            if(!stack.isEmpty() && stack.getCount() > highestCount
                    && stack.getItem().getDescriptionId().equals(item.getDescriptionId())) {
                highestCount = stack.getCount();
            }
        }

        return highestCount;
    }

    public static boolean removeAndRefund(String itemID, CraftingMenu removalMenu, Inventory refundInventory, int limit) {
        ItemStack stack, copiedStack = null;
        int validIngredientCount = 0, removeFromEach;
        boolean[] validSlot = new boolean[9];

        for(int i = 1; i <= 9; i++) {
            stack = removalMenu.getSlot(i).getItem();

            if(!stack.isEmpty() && stack.getItem().getDescriptionId().equals(itemID)) {
                // Increase count for amount of slots filled with this ID and mark valid slots via array
                validIngredientCount++;
                validSlot[i] = true;
            }
        }

        if(limit % validIngredientCount != 0) {
            return false;
        }
        removeFromEach = limit / validIngredientCount;
        if(removeFromEach > 63) return false;

        for(int i = 0; i < validSlot.length; i++) {
            if(validSlot[i]) {
                stack = removalMenu.getSlot(i).getItem();
                copiedStack = stack.copy();
                stack.shrink(removeFromEach);
            }
        }

        if(copiedStack == null) return false;
        returnCraftingItem(copiedStack.getItem(), removeFromEach * validIngredientCount, null, refundInventory);
        return true;
    }
}
