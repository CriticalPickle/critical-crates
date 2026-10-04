package com.criticalpickle.criticalcrates.item;

import com.criticalpickle.criticalcrates.Config;
import com.criticalpickle.criticalcrates.util.EnchantmentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class PliersItem extends Item {
    public PliersItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isDamageable(@NonNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean supportsEnchantment(@NonNull ItemStack stack, @NonNull Holder<Enchantment> enchantment) {
        return true;
    }

    /// Copies components from source component patch into builder.
    private static <T> void copyComponents(
            DataComponentPatch.Builder builder, DataComponentPatch src, DataComponentType<T> key
    ) {
        Objects.requireNonNull(src.getPatch(key)).ifPresent(value -> builder.set(key, value));
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        final CustomData data = instance.get(DataComponents.CUSTOM_DATA);
        final boolean isBroken = data != null && data.copyTag().getBoolean("broken").isPresent()
                && data.copyTag().getBoolean("broken").get();

        if(isBroken) return null;

        final ItemEnchantments enchants = instance.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        final int unbreakingLvl = enchants.getLevel(EnchantmentUtils.getEnchantmentHolder(Enchantments.UNBREAKING));
        boolean causeDamage = true;

        if(unbreakingLvl > 0) causeDamage = RandomSource.create().nextInt(1 + unbreakingLvl) == 0;

        final DataComponentPatch oldComponents = switch (instance) {
            case ItemStack stack -> stack.getComponentsPatch();
            case ItemStackTemplate template -> template.components();
            default -> DataComponentPatch.EMPTY;
        };
        final DataComponentPatch.Builder patchBuilder = DataComponentPatch.builder();

        // Add old components to new patch builder
        oldComponents.entrySet().forEach(entry -> copyComponents(patchBuilder, oldComponents, entry.getKey()));

        if(causeDamage) {
            final int currentDamage = instance.getOrDefault(DataComponents.DAMAGE, 0);
            final int maxDamage = instance.getOrDefault(DataComponents.MAX_DAMAGE, 0);

            patchBuilder.set(DataComponents.DAMAGE, currentDamage + 1);

            if(currentDamage >= maxDamage) {
                // Reset for unbreaking to make sure it doesn't show
                // empty bar when "broken" due to unbreaking desync.
                patchBuilder.set(DataComponents.MAX_DAMAGE, currentDamage - 1);

                CompoundTag dataTag = new CompoundTag();
                dataTag.putBoolean("broken", true);
                patchBuilder.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
            }
        }

        return new ItemStackTemplate(instance.typeHolder(), patchBuilder.build());
    }

    @Override
    public void appendHoverText(
            @NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay tooltipDisplay,
            @NonNull Consumer<Component> tooltipAdder, @NonNull TooltipFlag flag
    ) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        if(Config.ADDONS_REMOVABLE.getAsBoolean()) {
            if(flag.hasShiftDown()) {
                tooltipAdder.accept(Component.translatable("tooltip.pliers.shift"));
            } else {
                tooltipAdder.accept(Component.translatable("tooltip.pliers").withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
