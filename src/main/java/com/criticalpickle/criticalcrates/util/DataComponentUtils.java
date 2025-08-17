package com.criticalpickle.criticalcrates.util;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public class DataComponentUtils {
    public static void addBlockEntityDataTag(BlockEntity blockEntity, CompoundTag dataTag) {
        DataComponentMap.Builder builder = DataComponentMap.builder();

        builder.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
        blockEntity.setComponents(builder.build());
    }

    public static void addBlockEntityDataComponent(BlockEntity blockEntity, DataComponentType<Unit> dataComponent) {
        DataComponentMap.Builder builder = DataComponentMap.builder();

        builder.set(dataComponent, Unit.INSTANCE);
        blockEntity.setComponents(builder.build());
    }

    public static void addBlockEntityDataComponents(BlockEntity blockEntity, CompoundTag dataTag, DataComponentType<Unit> dataComponent) {
        DataComponentMap.Builder builder = DataComponentMap.builder();

        builder.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
        builder.set(dataComponent, Unit.INSTANCE);
        blockEntity.setComponents(builder.build());
    }

    public static boolean doesStackCustomDataEqualCustomData(ItemStack stack, @Nullable CustomData data) {
        if(stack.get(DataComponents.CUSTOM_DATA) != null && data != null) {
            return stack.get(DataComponents.CUSTOM_DATA).equals(data);
        }
        return false;
    }
}
