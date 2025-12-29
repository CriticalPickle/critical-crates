package com.criticalpickle.criticalcrates.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class CrateBlockItem extends BlockItem {
    public CrateBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Player player) {
        super.onCraftedBy(stack, player);
        checkTags(stack);
    }

    private void checkTags(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag dataTag = new CompoundTag();

        if(data == null) {
            dataTag.putBoolean("explosion_resistant", false);
            dataTag.putBoolean("lamp_upgrade", false);
            dataTag.putBoolean("fireproof", false);
            dataTag.putBoolean("slimy", false);
        }
        else if(!data.contains("explosion_resistant") || !data.contains("lamp_upgrade") || !data.contains("fireproof") || !data.contains("slimy")) {
            if(!data.contains("explosion_resistant")) {
                dataTag.putBoolean("explosion_resistant", false);
            }

            if(!data.contains("lamp_upgrade")) {
                dataTag.putBoolean("lamp_upgrade", false);
            }

            if(!data.contains("fireproof")) {
                dataTag.putBoolean("fireproof", false);
            }

            if(!data.contains("slimy")) {
                dataTag.putBoolean("slimy", false);
            }
         }

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        boolean resistant = false, lamp = false, fire = false, slimy = false;

        // Get value (if any)
        if(!stack.isEmpty() && data != null) {
            if(data.contains("explosion_resistant")) {
                resistant = data.copyTag().getBoolean("explosion_resistant").get();
            }

            if(data.contains("lamp_upgrade")) {
                lamp = data.copyTag().getBoolean("lamp_upgrade").get();
            }

            if(data.contains("fireproof")) {
                fire = data.copyTag().getBoolean("fireproof").get();
            }

            if(data.contains("slimy")) {
                slimy = data.copyTag().getBoolean("slimy").get();
            }
        }

        // Set tooltip for appropriate property
        if(resistant) {
            tooltipAdder.accept(Component.translatable("tooltip.crate.obsidian_reinforcement_upgraded").withStyle(ChatFormatting.RED));
        }
        else if(lamp) {
            tooltipAdder.accept(Component.translatable("tooltip.crate.lamp_simulator_upgraded").withStyle(ChatFormatting.YELLOW));
        }
        else if(fire) {
            tooltipAdder.accept(Component.translatable("tooltip.crate.fireproofing_upgraded").withStyle(ChatFormatting.GOLD));
        }
        else if(slimy) {
            tooltipAdder.accept(Component.translatable("tooltip.crate.slimy_framing_upgraded").withStyle(ChatFormatting.GREEN));
        }
    }
}
