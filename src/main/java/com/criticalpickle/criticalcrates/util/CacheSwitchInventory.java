package com.criticalpickle.criticalcrates.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public final class CacheSwitchInventory {
    private static NonNullList<ItemStack> cachedItems = NonNullList.withSize(27, ItemStack.EMPTY);;

    public static void cache(NonNullList<ItemStack> inventory) {
        cachedItems = inventory;
    }

    public static NonNullList<ItemStack> getCache() {
        NonNullList<ItemStack> inventory = cachedItems;
        cachedItems = NonNullList.withSize(27, ItemStack.EMPTY);
        return inventory;
    }
}
