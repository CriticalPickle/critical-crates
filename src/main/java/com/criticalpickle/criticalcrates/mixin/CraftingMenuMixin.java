package com.criticalpickle.criticalcrates.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {
    @Shadow
    private Player player;

    // Ensure no "crafting_item" tagged items escape crafting grid upon closing table
    @Inject(method = "removed", at = @At("HEAD"))
    private void onRemoved(Player player, CallbackInfo ci) {
        if (!(player.containerMenu instanceof CraftingMenu craftingMenu)) return;
        Inventory inventory = player.getInventory();
        ItemStack stack;
        CompoundTag dataTag;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);

            if(stack.get(DataComponents.CUSTOM_DATA) != null) {
                dataTag = stack.get(DataComponents.CUSTOM_DATA).copyTag();

                if(stack.get(DataComponents.CUSTOM_DATA).contains("crafting_item")) {
                    dataTag.remove("crafting_item");
                }

                if(stack.get(DataComponents.CUSTOM_DATA).contains("player_crafting")) {
                    dataTag.remove("player_crafting");
                }

                if(!dataTag.isEmpty()) {
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                }
                else {
                    stack.remove(DataComponents.CUSTOM_DATA);
                }
            }
        }

        for(int i = 1; i <= 9; i++) {
            stack = craftingMenu.getSlot(i).getItem();
            if(stack.get(DataComponents.CUSTOM_DATA) != null) {
                dataTag = stack.get(DataComponents.CUSTOM_DATA).copyTag();

                if(stack.get(DataComponents.CUSTOM_DATA).contains("crafting_item")) {
                    dataTag.remove("crafting_item");
                }

                if(stack.get(DataComponents.CUSTOM_DATA).contains("player_crafting")) {
                    dataTag.remove("player_crafting");
                }

                if(!dataTag.isEmpty()) {
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                }
                else {
                    stack.remove(DataComponents.CUSTOM_DATA);
                }
            }
        }
    }

    // Make sure that "ghost" items tagged with "crafting_item" are removed (not sure why they were here in the first place)
    @Inject(method = "slotsChanged", at = @At("RETURN"))
    private void onSlotsChanged(CallbackInfo ci) {
        Inventory inventory = player.getInventory();
        CraftingMenu craftingMenu = (CraftingMenu) player.containerMenu;
        ItemStack stack;
        String stackItemID;
        CustomData data;
        CompoundTag dataTag;

        for(int i = 1; i <= 9; i++) {
            stack = craftingMenu.getSlot(i).getItem();
            stackItemID = stack.getItem().getDescriptionId();
            data = stack.get(DataComponents.CUSTOM_DATA);

            if(!stack.isEmpty() && stackItemID.contains("criticalcrates") && stackItemID.contains("crate") && data != null
                    && !data.contains("crafting_item") && !data.contains("player_crafting")) {
                dataTag = data.copyTag();
                dataTag.putBoolean("player_crafting", true);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                break;
            }
        }

        for(int i = 0; i < inventory.getContainerSize(); i++) {
            stack = inventory.getItem(i);
            stackItemID = stack.getItem().getDescriptionId();
            data = stack.get(DataComponents.CUSTOM_DATA);

            if(!player.level().isClientSide() && !stack.isEmpty() && stackItemID.contains("criticalcrates") && stackItemID.contains("crate") && data != null
                    && (data.contains("crafting_item") || data.contains("player_crafting"))) {
                inventory.setItem(i, ItemStack.EMPTY);
            }
        }
    }
}
