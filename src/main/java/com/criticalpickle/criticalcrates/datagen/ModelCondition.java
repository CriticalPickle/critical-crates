package com.criticalpickle.criticalcrates.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Arrays;

public final class ModelCondition<T extends Comparable<T>>{
    private final Property<T> property;
    private final T firstValue;
    private final T[] restValues;

    private ModelCondition(Property<T> property, T[] values) {
        this.property = property;
        this.firstValue = values[0];
        this.restValues = Arrays.copyOfRange(values, 1, values.length);
    }

    @SafeVarargs
    public static <T extends Comparable<T>> ModelCondition<T> of(Property<T> property, T... values) {
        return new ModelCondition<>(property, values);
    }

    public Condition toCondition() {
        return BlockModelGenerators.condition().term(property, firstValue, restValues).build();
    }
}
