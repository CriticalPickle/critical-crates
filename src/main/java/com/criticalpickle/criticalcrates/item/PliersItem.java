package com.criticalpickle.criticalcrates.item;

import com.criticalpickle.criticalcrates.Config;
import com.criticalpickle.criticalcrates.util.EnchantmentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.*;

import java.util.List;

public class PliersItem extends Item {
    public PliersItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.IRON_INGOT) || super.isValidRepairItem(stack, repairCandidate);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copiedStack = stack.copy();
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(copiedStack);
        int unbreakingLvl = enchants.getLevel(EnchantmentUtils.getEnchantmentHolder(Enchantments.UNBREAKING));
        boolean causeDamage = true;

        if(data != null && data.copyTag().getBoolean("broken")) {
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if(Config.ADDONS_REMOVABLE.getAsBoolean()) {
            if(Screen.hasShiftDown()) {
                tooltipComponents.add(Component.literal("Right click on a crate to remove upgrade!"));
            } else {
                tooltipComponents.add(Component.literal("Press SHIFT to view more info").withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
