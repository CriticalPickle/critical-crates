package com.criticalpickle.criticalcrates.item;

import com.criticalpickle.criticalcrates.Config;
import com.criticalpickle.criticalcrates.util.EnchantmentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull ItemStack getCraftingRemainder(ItemStack stack) {
        ItemStack copiedStack = stack.copy();
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(copiedStack);
        int unbreakingLvl = enchants.getLevel(EnchantmentUtils.getEnchantmentHolder(Enchantments.UNBREAKING));
        boolean causeDamage = true;

        if(data != null && data.copyTag().getBoolean("broken").get()) {
            return ItemStack.EMPTY;
        }

        if(unbreakingLvl > 0) {
            int chanceOfDamage = RandomSource.create().nextInt(1 + unbreakingLvl);
            if(chanceOfDamage != 0) {
                causeDamage = false;
            }
        }

        if(causeDamage) {
            copiedStack.setDamageValue(copiedStack.getDamageValue() + 1);

            if(copiedStack.getDamageValue() >= copiedStack.getMaxDamage()){
                // Reset for unbreaking to make sure it doesn't show empty bar when "broken" due to unbreaking desync
                copiedStack.setDamageValue(copiedStack.getDamageValue() - 1);

                CompoundTag dataTag = new CompoundTag();
                dataTag.putBoolean("broken", true);
                copiedStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
            }
        }

        return copiedStack;
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
