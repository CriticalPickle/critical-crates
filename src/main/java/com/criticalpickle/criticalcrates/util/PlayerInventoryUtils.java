package com.criticalpickle.criticalcrates.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

    public static ItemStack getStackWithIDListAndTag(List<String> list, Inventory inventory) {
        ItemStack stack, validItem = ItemStack.EMPTY;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);

            if(!stack.isEmpty() && list.contains(stack.getItem().getDescriptionId())
                    && stack.get(DataComponents.CUSTOM_DATA) != null
                    && !(stack.get(DataComponents.CUSTOM_DATA).contains("crafting_item")
                    || stack.get(DataComponents.CUSTOM_DATA).contains("player_crafting"))) {
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

    public static int getItemAmount(Item item, Inventory inventory) {
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

    public static int removeStacksOfID(int countToRemove, String ID, Inventory inventory) {
        ItemStack stack;
        int removed;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);

            if(!stack.isEmpty() && stack.getItem().getDescriptionId().equals(ID) && stack.get(DataComponents.CUSTOM_DATA) != null
                    && !(stack.get(DataComponents.CUSTOM_DATA).contains("crafting_item")
                    || stack.get(DataComponents.CUSTOM_DATA).contains("player_crafting"))) {
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
