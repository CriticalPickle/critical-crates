package com.criticalpickle.criticalcrates.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class ItemModelPropertyUtils {
    public record CrateDataValue() implements SelectItemModelProperty<String> {
        public static final SelectItemModelProperty.Type<CrateDataValue, String> TYPE = SelectItemModelProperty.Type.create(
                MapCodec.unit(new CrateDataValue()),
                Codec.STRING
        );

        @Override
        public @Nullable String get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
            if(itemStack.getComponents().get(DataComponents.CUSTOM_DATA) != null) {
                CompoundTag dataTag = itemStack.getComponents().get(DataComponents.CUSTOM_DATA).copyTag();
                if(dataTag.getBooleanOr("explosion_resistant", false)) {
                    return "resistant";
                }
                else if(dataTag.getBooleanOr("lamp_upgrade", false)) {
                    return "lamp";
                }
                else if(dataTag.getBooleanOr("fireproof", false)) {
                    return "fireproof";
                }
            }
            return "";
        }

        @Override
        public Codec<String> valueCodec() {
            return Codec.STRING;
        }

        @Override
        public Type<? extends SelectItemModelProperty<String>, String> type() {
            return TYPE;
        }
    }

    // --- UNREGISTERED CONDITIONAL CODECS FOR POSSIBLE FUTURE DATA GEN USE ---

    public record ResistantProperty() implements ConditionalItemModelProperty {
        public static final MapCodec<ResistantProperty> MAP_CODEC = MapCodec.unit(new ResistantProperty());

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }

        @Override
        public boolean get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
            if(itemStack.getComponents().get(DataComponents.CUSTOM_DATA) != null) {
                CompoundTag dataTag = itemStack.getComponents().get(DataComponents.CUSTOM_DATA).copyTag();
                return dataTag.getBooleanOr("explosion_resistant", false);
            }
            return false;
        }
    }

    public record LampProperty() implements ConditionalItemModelProperty {
        public static final MapCodec<LampProperty> MAP_CODEC = MapCodec.unit(new LampProperty());

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }

        @Override
        public boolean get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
            if(itemStack.getComponents().get(DataComponents.CUSTOM_DATA) != null) {
                CompoundTag dataTag = itemStack.getComponents().get(DataComponents.CUSTOM_DATA).copyTag();
                return dataTag.getBooleanOr("lamp_upgrade", false);
            }
            return false;
        }
    }

    public record FireproofProperty() implements ConditionalItemModelProperty {
        public static final MapCodec<FireproofProperty> MAP_CODEC = MapCodec.unit(new FireproofProperty());

        @Override
        public MapCodec<? extends ConditionalItemModelProperty> type() {
            return MAP_CODEC;
        }

        @Override
        public boolean get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
            if(itemStack.getComponents().get(DataComponents.CUSTOM_DATA) != null) {
                CompoundTag dataTag = itemStack.getComponents().get(DataComponents.CUSTOM_DATA).copyTag();
                return dataTag.getBooleanOr("fireproof", false);
            }
            return false;
        }
    }
}
