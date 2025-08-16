package com.criticalpickle.criticalcrates.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CraftingGridUtils {
    public static int getItemAmount(Item item, ServerPlayer player) {
        CraftingMenu craftingMenu = (CraftingMenu) player.containerMenu;
        ItemStack slotStack;
        int itemCount = 0;

        for(int i = 1; i <= 9; i++) {
            slotStack = craftingMenu.getSlot(i).getItem();
            if(slotStack.getItem() == item) {
                itemCount += slotStack.getCount();
            }
        }

        return itemCount;
    }

    public static int getLowestSlotCountOf(Item item, ServerPlayer player) {
        CraftingMenu craftingMenu = (CraftingMenu) player.containerMenu;
        ItemStack slotStack;
        int lowestCount = 64;

        for(int i = 1; i <= 9; i++) {
            slotStack = craftingMenu.getSlot(i).getItem();
            if(!slotStack.isEmpty() && slotStack.getCount() < lowestCount
                    && slotStack.getItem().getDescriptionId().equals(item.getDescriptionId())) {
                lowestCount = slotStack.getCount();
            }
        }

        return lowestCount;
    }

    public static int getHighestSlotCountOf(Item item, CraftingMenu craftingMenu) {
        ItemStack slotStack;
        int highestCount = 0;

        for(int i = 1; i <= 9; i++) {
            slotStack = craftingMenu.getSlot(i).getItem();
            if(!slotStack.isEmpty() && slotStack.getCount() > highestCount
                    && slotStack.getItem().getDescriptionId().equals(item.getDescriptionId())) {
                highestCount = slotStack.getCount();
            }
        }

        return highestCount;
    }
}
