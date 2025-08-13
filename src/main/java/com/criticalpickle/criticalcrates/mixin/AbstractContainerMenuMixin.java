package com.criticalpickle.criticalcrates.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    // Remove "crafting_item" tag identifier if item is taken out of crafting grid by player
    @Inject(method = "clicked", at = @At("HEAD"))
    private void onClicked(int slotId, int button, ClickType clickType, Player player, CallbackInfo cir) {
        if(!((Object)this instanceof CraftingMenu)) return;
        CraftingMenu craftingMenu = (CraftingMenu)(Object)this;

        if(slotId >= 1 && slotId <= 9) {
            ItemStack stack;
            CompoundTag dataTag;

            stack = player.containerMenu.getSlot(slotId).getItem();
            if(stack.get(DataComponents.CUSTOM_DATA) != null && stack.get(DataComponents.CUSTOM_DATA).contains("crafting_item")) {
                dataTag = stack.get(DataComponents.CUSTOM_DATA).copyTag();
                dataTag.remove("crafting_item");

                if(!dataTag.isEmpty()) {
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                }
                else {
                    stack.remove(DataComponents.CUSTOM_DATA);
                }
            }
        }
    }
}
