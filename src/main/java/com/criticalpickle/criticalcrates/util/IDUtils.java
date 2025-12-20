package com.criticalpickle.criticalcrates.util;

import net.minecraft.world.item.Item;

public class IDUtils {
    // Gets actual item id from description id of an item.
    // There might already be a class available for this, but I could not find one.
    public static String getItemID(Item item) {
        if(item.getDescriptionId().contains("criticalcrates")) {
            return item.getDescriptionId().substring(item.getDescriptionId().indexOf("s.") + 2);
        }
        throw new IllegalArgumentException("Item must be of namespace 'criticalcrates' to use getItemID");
    }
}
