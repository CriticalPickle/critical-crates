package com.criticalpickle.criticalcrates.util;

import net.neoforged.neoforge.items.ItemStackHandler;

public final class CacheSwitchInventory {
    private static ItemStackHandler cachedInventory = null;

    public static void cache(ItemStackHandler inventory) {
        cachedInventory = inventory;
    }

    public static ItemStackHandler getCache() {
        ItemStackHandler inventory = cachedInventory;
        cachedInventory = null;
        return inventory;
    }
}
