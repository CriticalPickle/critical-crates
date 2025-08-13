package com.criticalpickle.criticalcrates.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackUtils {
    public static CompoundTag findStackCustomDataTag(ItemStack stack) {
        CompoundTag dataTag;
        if(stack.get(DataComponents.CUSTOM_DATA) != null) {
            dataTag = stack.get(DataComponents.CUSTOM_DATA).copyTag();
        }
        else {
            dataTag = new CompoundTag();
        }
        return dataTag;
    }
}
