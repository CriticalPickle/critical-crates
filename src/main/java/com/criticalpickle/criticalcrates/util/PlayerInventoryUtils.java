package com.criticalpickle.criticalcrates.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public class PlayerInventoryUtils {
    public static ItemStack getStackWithItemID(String itemID, Inventory inventory) {
        ItemStack stack, validStack = ItemStack.EMPTY;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);

            if(!stack.isEmpty() && stack.getItem().getDescriptionId().equals(itemID)
                    && stack.get(DataComponents.CUSTOM_DATA) != null
                    && !(stack.get(DataComponents.CUSTOM_DATA).contains("crafting_item")
                    || stack.get(DataComponents.CUSTOM_DATA).contains("player_crafting"))) {
                validStack = stack;
                break;
            }
        }
        return validStack;
    }

    public static ItemStack getStackWithIDAndTag(String itemID, CompoundTag dataTag, Inventory inventory) {
        ItemStack stack, validStack = ItemStack.EMPTY;
        CompoundTag foundTag;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);

            if(!stack.isEmpty() && stack.get(DataComponents.CUSTOM_DATA) != null) {
                foundTag = stack.get(DataComponents.CUSTOM_DATA).copyTag();
                if(dataTag == foundTag && stack.getItem().getDescriptionId().equals(itemID)) {
                    validStack = stack;
                    break;
                }
            }
        }
        return validStack;
    }

    public static ItemStack getStackWithIDListAndTag(List<String> list, CompoundTag dataTag, Inventory inventory) {
        ItemStack stack, validStack = ItemStack.EMPTY;
        CustomData data;
        CompoundTag foundTag;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);
            data = stack.get(DataComponents.CUSTOM_DATA);

            if(!stack.isEmpty() && data != null && !(data.contains("crafting_item") || data.contains("player_crafting"))) {
                foundTag = data.copyTag();
                if(list.contains(stack.getItem().getDescriptionId()) && dataTag.equals(foundTag)) {
                    validStack = stack;
                    break;
                }
            }
        }
        return validStack;
    }

    public static ItemStack getStackWithIDList(List<String> list, Inventory inventory) {
        ItemStack stack, validItem = ItemStack.EMPTY;
        CustomData data;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);
            data = stack.get(DataComponents.CUSTOM_DATA);

            if(!stack.isEmpty() && list.contains(stack.getItem().getDescriptionId())
                    && data != null && !(data.contains("crafting_item") || data.contains("player_crafting"))) {
                validItem = stack;
                break;
            }
        }
        return validItem;
    }

    public static int getFirstEmptySlot(Inventory inventory) {
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            if(inventory.getItem(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public static int getItemCount(Item item, Inventory inventory) {
        ItemStack stack;
        int itemCount = 0;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);

            if(!stack.isEmpty() && stack.getItem() == item) {
                itemCount += stack.getCount();
            }
        }

        return itemCount;
    }

    public static int getItemIDWithDataTagCount(String itemID, CompoundTag dataTag, Inventory inventory) {
        ItemStack stack;
        int itemCount = 0;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);

            if(!stack.isEmpty() && stack.getItem().getDescriptionId().equals(itemID)
                    && DataComponentUtils.doesStackCustomDataEqualCustomData(stack, CustomData.of(dataTag))) {
                itemCount += stack.getCount();
            }
        }
        return itemCount;
    }

    public static int removeStacksOfIDAndData(int countToRemove, String ID, CustomData data, Inventory inventory) {
        ItemStack stack;
        CustomData stackData;
        int removed;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);
            stackData = stack.get(DataComponents.CUSTOM_DATA);

            if(!stack.isEmpty() && stack.getItem().getDescriptionId().equals(ID) && stackData != null
                    && !(stackData.contains("crafting_item") || stackData.contains("player_crafting")) &&
                    DataComponentUtils.doesStackCustomDataEqualCustomData(stack, data)) {

                if(stack.getCount() <= countToRemove) {
                    removed = stack.getCount();
                    stack.shrink(removed);
                    countToRemove -= removed;
                }
                else {
                    stack.shrink(countToRemove);
                    countToRemove = 0;
                }

                if(countToRemove <= 0) {
                    return 0;
                }
            }
        }

        return countToRemove;
    }
}
