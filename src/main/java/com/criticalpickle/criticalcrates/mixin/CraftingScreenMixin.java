package com.criticalpickle.criticalcrates.mixin;

import com.criticalpickle.criticalcrates.network.RecipeBookClickHandler;
import com.criticalpickle.criticalcrates.util.CraftingGridUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingScreen.class)
public class CraftingScreenMixin {
    @Unique
    private ItemStack stackInSlot1;

    @Unique
    private ItemStack stackInSlot2;

    @Unique
    private int otherIngredientHigh = 0;

    // Get input slot item before vanilla code override
    @Inject(method = "mouseClicked", at = @At(value = "HEAD"))
    private void onRecipeBookClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        Screen screen = Minecraft.getInstance().screen;
        if (!(screen instanceof CraftingScreen craftingScreen)) return;

        CraftingMenu menu = craftingScreen.getMenu();
        Slot slot1 = menu.getSlot(1);
        Slot slot2 = menu.getSlot(2);

        if(slot1.hasItem()) {
            stackInSlot1 = slot1.getItem().copy();
        }
        else {
            stackInSlot1 = ItemStack.EMPTY;
        }

        if(slot2.hasItem()) {
            stackInSlot2 = slot2.getItem().copy();
        }
        else {
            stackInSlot2 = ItemStack.EMPTY;
        }

        otherIngredientHigh = CraftingGridUtils.getHighestSlotCountOf(stackInSlot2.getItem(), menu);
    }

    // Send recipe book packet information after vanilla code has finished
    @Inject(method = "mouseClicked", at = @At(value = "RETURN"))
    private void afterRecipeBookClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        CraftingScreen self = (CraftingScreen)(Object) this;
        RecipeBookComponent recipeBook = self.getRecipeBookComponent();
        if(recipeBook == null) return;

        RecipeBookPage page = ((RecipeBookComponentAccessor) recipeBook).getRecipeBookPage();
        if(page == null) return;

        RecipeHolder<?> lastClickedRecipe = page.getLastClickedRecipe();
        if(lastClickedRecipe == null) return;

        ResourceLocation recipeID = lastClickedRecipe.id();

        String stackInSlot2ID = "none";
        if(!stackInSlot2.isEmpty()) {
           stackInSlot2ID = stackInSlot2.getItem().getDescriptionId();
        }

        CustomData data = stackInSlot1.get(DataComponents.CUSTOM_DATA);
        boolean hasCraftingTag = false;
        CompoundTag dataTag = null;
        if(!stackInSlot1.isEmpty() && data != null) {
            dataTag = data.copyTag();

            // Sanitize custom data tags to insure no returned item is given with crafting tags
            if(dataTag.contains("crafting_item")) {
                hasCraftingTag = true;
                dataTag.remove("crafting_item");
            }

            if(dataTag.contains("player_crafting")) {
                dataTag.remove("player_crafting");
            }

            if(dataTag.isEmpty()) {
                dataTag = null;
            }
        }
        int numberInSlot1 = stackInSlot1.getCount();
        String stackInSlot1ID = stackInSlot1.getItem().getDescriptionId();

        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if(connection != null && (recipeID.toString().contains("glass_crate")
                || (stackInSlot1ID.contains("criticalcrates") && stackInSlot1ID.contains("crate")))) {
            connection.send(new RecipeBookClickHandler.RecipeBookClickPayload(recipeID, stackInSlot2ID, otherIngredientHigh,
                    hasCraftingTag, stackInSlot1ID, dataTag, numberInSlot1, Screen.hasShiftDown()));
        }
    }
}
