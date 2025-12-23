package com.criticalpickle.criticalcrates.util;

import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public final class CacheSwitchInventory {
    private static ItemStacksResourceHandler cachedInventory = null;

    public static void cache(ItemStacksResourceHandler inventory) {
        cachedInventory = inventory;
    }

    public static ItemStacksResourceHandler getCache() {
        ItemStacksResourceHandler inventory = cachedInventory;
        cachedInventory = null;
        return inventory;
    }
}
