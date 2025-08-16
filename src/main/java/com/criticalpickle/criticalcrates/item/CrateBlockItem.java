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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class CrateBlockItem extends BlockItem {
    public CrateBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        checkTags(stack);
    }

    private void checkTags(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag dataTag = new CompoundTag();

        if(data == null) {
            dataTag.putBoolean("explosion_resistant", false);
            dataTag.putBoolean("lamp_upgrade", false);
            dataTag.putBoolean("fireproof", false);
        }
        else if(!data.contains("explosion_resistant") || !data.contains("lamp_upgrade") || !data.contains("fireproof")) {
            if(!data.contains("explosion_resistant")) {
                dataTag.putBoolean("explosion_resistant", false);
            }

            if(!data.contains("lamp_upgrade")) {
                dataTag.putBoolean("lamp_upgrade", false);
            }

            if(!data.contains("fireproof")) {
                dataTag.putBoolean("fireproof", false);
            }
         }

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        boolean resistant = false, lamp = false, fire = false;

        // Get value (if any)
        if(!stack.isEmpty() && data != null) {
            if(data.contains("explosion_resistant")) {
                resistant = data.copyTag().getBoolean("explosion_resistant");
            }

            if(data.contains("lamp_upgrade")) {
                lamp = data.copyTag().getBoolean("lamp_upgrade");
            }

            if(data.contains("fireproof")) {
                fire = data.copyTag().getBoolean("fireproof");
            }
        }

        // Set tooltip for appropriate property
        if(resistant) {
            tooltipComponents.add(Component.translatable("tooltip.crate.obsidian_reinforcement_upgraded").withStyle(ChatFormatting.RED));
        }
        else if(lamp) {
            tooltipComponents.add(Component.translatable("tooltip.crate.lamp_simulator_upgraded").withStyle(ChatFormatting.YELLOW));
        }
        else if(fire) {
            tooltipComponents.add(Component.translatable("tooltip.crate.fireproofing_upgraded").withStyle(ChatFormatting.GOLD));
        }
    }
}
