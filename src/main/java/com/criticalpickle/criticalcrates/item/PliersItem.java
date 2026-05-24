package com.criticalpickle.criticalcrates.item;

import com.criticalpickle.criticalcrates.Config;
import com.criticalpickle.criticalcrates.util.EnchantmentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class PliersItem extends Item {
    public PliersItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return true;
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        CustomData data = instance.get(DataComponents.CUSTOM_DATA);
        ItemEnchantments enchants = instance.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        int unbreakingLvl = enchants.getLevel(EnchantmentUtils.getEnchantmentHolder(Enchantments.UNBREAKING));
        boolean causeDamage = true, isBroken = data != null && data.copyTag().getBoolean("broken").isPresent()
                && data.copyTag().getBoolean("broken").get();

        if(isBroken) {
            return null;
        }

        DataComponentPatch.Builder patchBuilder = DataComponentPatch.builder();
        if(unbreakingLvl > 0) {
            int chanceOfDamage = RandomSource.create().nextInt(1 + unbreakingLvl);
            if(chanceOfDamage != 0) {
                causeDamage = false;
            }
        }

        if(causeDamage) {
            int currentDamage = instance.getOrDefault(DataComponents.DAMAGE, 0);
            int maxDamage = instance.getOrDefault(DataComponents.MAX_DAMAGE, 0);

            patchBuilder.set(DataComponents.DAMAGE, currentDamage + 1);

            if(currentDamage >= maxDamage) {
                // Reset for unbreaking to make sure it doesn't show empty bar when "broken" due to unbreaking desync
                patchBuilder.set(DataComponents.MAX_DAMAGE, currentDamage - 1);

                CompoundTag dataTag = new CompoundTag();
                dataTag.putBoolean("broken", true);
                patchBuilder.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
            }
        }

        return new ItemStackTemplate(instance.typeHolder(), patchBuilder.build());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
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
