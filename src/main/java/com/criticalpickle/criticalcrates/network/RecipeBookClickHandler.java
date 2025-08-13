package com.criticalpickle.criticalcrates.network;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
import com.criticalpickle.criticalcrates.item.CrateBlockItem;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.util.CraftingGridUtils;
import com.criticalpickle.criticalcrates.util.ItemStackUtils;
import com.criticalpickle.criticalcrates.util.PlayerInventoryUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeBookClickHandler {
    // Payload is required to tell what is happening before vanilla overriding code
    public record RecipeBookClickPayload(ResourceLocation recipeID, boolean inputItemHasCraftingTag, String inputItemID, CompoundTag inputItemDataTag, int inputItemCount, boolean shiftDown) implements CustomPacketPayload {
        public static final Type<RecipeBookClickPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CriticalCrates.MODID, "recipe_book_click"));

        public static final StreamCodec<FriendlyByteBuf, RecipeBookClickPayload> STREAM_CODEC = CustomPacketPayload.codec(
                (payload, buf) -> {
                    buf.writeResourceLocation(payload.recipeID);
                    buf.writeBoolean(payload.inputItemHasCraftingTag);
                    buf.writeUtf(payload.inputItemID);
                    buf.writeNbt(payload.inputItemDataTag);
                    buf.writeInt(payload.inputItemCount);
                    buf.writeBoolean(payload.shiftDown);
                },
                buf -> new RecipeBookClickPayload(buf.readResourceLocation(), buf.readBoolean(), buf.readUtf(), buf.readNbt(), buf.readInt(), buf.readBoolean())
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void handle(RecipeBookClickPayload payload, ServerPlayer player) {
        String recipeID = payload.recipeID().toString(), recipeReq = "none";
        Item crateItem = ModItems.findCrateItemByID(payload.inputItemID());
        boolean differentRecipe = false;

        // What is needed for recipe? Is recipe different from before?
        if(recipeID.contains("glass_crate") && !recipeID.contains("dye")) {
            recipeReq = "wood_crate";
            differentRecipe = crateItem instanceof CrateBlockItem crateBlockItem && crateBlockItem.getBlock() instanceof GlassCrateBlock;
        }
        else if(recipeID.contains("dye") && recipeID.contains("glass_crate")){
            recipeReq = "glass_crate";
            differentRecipe = crateItem instanceof CrateBlockItem crateBlockItem && !(crateBlockItem.getBlock() instanceof GlassCrateBlock);
        }

        // If different make sure to return previous crate stack
        if(differentRecipe) {
            ItemStack returnedStack = new ItemStack(crateItem);
            returnedStack.setCount(payload.inputItemCount());
            returnedStack.set(DataComponents.CUSTOM_DATA, CustomData.of(payload.inputItemDataTag()));
            if(payload.inputItemDataTag().contains("fireproof") && payload.inputItemDataTag().getBoolean("fireproof")) {
                returnedStack.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
            }
            player.getInventory().add(returnedStack);
        }

        if(!recipeReq.equals("none")) {
            List<String> itemsNeeded = new ArrayList<>(List.of());
            Item otherIngredient;
            int otherIngredientMin;
            Map<String, Object> otherIngredientMap = findRecipeIngredients(recipeReq, recipeID, itemsNeeded);
            Inventory inventory = player.getInventory();
            ItemStack firstValidItemStack;

            otherIngredient = (Item) otherIngredientMap.get("ingredient");
            otherIngredientMin = (Integer) otherIngredientMap.get("amount");

            firstValidItemStack = PlayerInventoryUtils.getStackWithIDListAndTagFromInventory(itemsNeeded, inventory);

            int otherIngredientCount = 0;
            otherIngredientCount += PlayerInventoryUtils.getItemAmountInInventory(otherIngredient, inventory);
            otherIngredientCount += CraftingGridUtils.getItemAmountInCraftingGrid(otherIngredient, player);

            if(!firstValidItemStack.isEmpty() && (otherIngredientCount >= otherIngredientMin)) {
                setCraftingSlotToStack(player, firstValidItemStack, 1, 2, otherIngredientMin,
                        payload.inputItemHasCraftingTag(), payload.inputItemCount(), payload.shiftDown());
            }
        }
        else {
            // If not a recipe requiring crates, make sure crate stack is returned to player
            if(crateItem != null) {
                ItemStack returnedStack = new ItemStack(crateItem);
                returnedStack.setCount(payload.inputItemCount());
                returnedStack.set(DataComponents.CUSTOM_DATA, CustomData.of(payload.inputItemDataTag()));
                if(payload.inputItemDataTag().contains("fireproof") && payload.inputItemDataTag().getBoolean("fireproof")) {
                    returnedStack.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
                }
                player.getInventory().add(returnedStack);
            }
        }
    }

    // Find both the correct crate and "other" ingredients
    private static Map<String, Object> findRecipeIngredients(String recipeReq, String recipeID, List<String> itemIDs) {
        int otherIngredientAmount = 0;
        Item otherIngredient = null;
        if(recipeReq.equals("wood_crate")) {
            String crateName = recipeID.substring(recipeID.indexOf(":") + 1);
            String color = crateName.substring(0, crateName.indexOf("_stained"));
            List<Item> panes = List.of(
                    Items.GLASS_PANE,
                    Items.WHITE_STAINED_GLASS_PANE,
                    Items.LIGHT_GRAY_STAINED_GLASS_PANE,
                    Items.GRAY_STAINED_GLASS_PANE,
                    Items.BLACK_STAINED_GLASS_PANE,
                    Items.BROWN_STAINED_GLASS_PANE,
                    Items.RED_STAINED_GLASS_PANE,
                    Items.ORANGE_STAINED_GLASS_PANE,
                    Items.YELLOW_STAINED_GLASS_PANE,
                    Items.LIME_STAINED_GLASS_PANE,
                    Items.GREEN_STAINED_GLASS_PANE,
                    Items.CYAN_STAINED_GLASS_PANE,
                    Items.LIGHT_BLUE_STAINED_GLASS_PANE,
                    Items.BLUE_STAINED_GLASS_PANE,
                    Items.PURPLE_STAINED_GLASS_PANE,
                    Items.MAGENTA_STAINED_GLASS_PANE,
                    Items.PINK_STAINED_GLASS_PANE
            );

            for(int i = 0; i < ModItems.getWoodCrateItems().length; i++) {
                itemIDs.add(ModItems.getWoodCrateItems(i).getDescriptionId());
            }

            for (Item pane : panes) {
                if (pane.getDescriptionId().contains(color)) {
                    otherIngredient = pane;
                }
            }

            otherIngredientAmount = 6;
        }
        else {
            String crateName = recipeID.substring(recipeID.indexOf("dye_") + 4);
            String color = crateName.substring(0, crateName.indexOf("_stained"));
            List<Item> dyes = List.of(
                    Items.WHITE_DYE,
                    Items.LIGHT_GRAY_DYE,
                    Items.GRAY_DYE,
                    Items.BLACK_DYE,
                    Items.BROWN_DYE,
                    Items.RED_DYE,
                    Items.ORANGE_DYE,
                    Items.YELLOW_DYE,
                    Items.LIME_DYE,
                    Items.GREEN_DYE,
                    Items.CYAN_DYE,
                    Items.LIGHT_BLUE_DYE,
                    Items.BLUE_DYE,
                    Items.PURPLE_DYE,
                    Items.MAGENTA_DYE,
                    Items.PINK_DYE
            );

            for(int i = 0; i < ModItems.getGlassCrateItems().length; i++) {
                if(!ModItems.getGlassCrateItems(i).getDescriptionId().contains(crateName)) {
                    itemIDs.add(ModItems.getGlassCrateItems(i).getDescriptionId());
                }
            }

            for (Item dye : dyes) {
                if (dye.getDescriptionId().contains(color)) {
                    otherIngredient = dye;
                }
            }

            otherIngredientAmount = 2;
        }

        Map<String, Object> tempMap = new java.util.HashMap<>(Map.of());
        tempMap.put("ingredient", otherIngredient);
        tempMap.put("amount", otherIngredientAmount);
        return tempMap;
    }

    private static void setCraftingSlotToStack(ServerPlayer player, ItemStack stack, int inSlot, int otherIngredientSlot, int minOtherIngredientCount, boolean hasCraftingTag, int numberInputItem, boolean shiftDown) {
        CraftingMenu menu = (CraftingMenu) player.containerMenu;
        ItemStack movedStack = stack.copy();
        CompoundTag dataTag;
        int otherIngredientCount = menu.getSlot(otherIngredientSlot).getItem().getCount(),
                numOtherIngredientInInventory = PlayerInventoryUtils.getItemAmountInInventory(
                        menu.getSlot(otherIngredientSlot).getItem().getItem(), player.getInventory()),
                numOtherIngredientInGrid = CraftingGridUtils.getItemAmountInCraftingGrid(
                        menu.getSlot(otherIngredientSlot).getItem().getItem(), player);

        dataTag = ItemStackUtils.findStackCustomDataTag(movedStack);
        dataTag.putBoolean("crafting_item", true);
        movedStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));

        // Crafting grid logic to determine input stack count change and inventory crate count
        if(numberInputItem != otherIngredientCount && numberInputItem < (numOtherIngredientInInventory + numOtherIngredientInGrid) / minOtherIngredientCount) {
            if(hasCraftingTag || numberInputItem <= 0) {
                if(!shiftDown) {
                    stack.shrink(1);
                    movedStack.setCount(otherIngredientCount);
                }
                else {
                    int stackCount = stack.getCount();
                    if(stackCount + numberInputItem < otherIngredientCount) {
                        stack.shrink(stackCount);

                        stack = PlayerInventoryUtils.getStackWithItemIDFromInventory(movedStack.getItem().getDescriptionId(), player.getInventory());
                        if(!stack.isEmpty()) {
                            stack.shrink(otherIngredientCount - stackCount - numberInputItem);
                        }

                        movedStack.setCount(otherIngredientCount);
                    }
                    else {
                        movedStack.setCount(otherIngredientCount);
                        stack.shrink(otherIngredientCount - numberInputItem);
                    }
                }
            }
            else if(!hasCraftingTag) {
                if(!shiftDown) {
                    stack.shrink(otherIngredientCount);
                    movedStack.setCount(otherIngredientCount);
                }
                else {
                    int stackCount = stack.getCount();
                    if(stackCount < otherIngredientCount) {
                        stack.shrink(stackCount);

                        stack = PlayerInventoryUtils.getStackWithItemIDFromInventory(movedStack.getItem().getDescriptionId(), player.getInventory());
                        if(!stack.isEmpty()) {
                            stack.shrink(otherIngredientCount - stackCount);
                        }

                        movedStack.setCount(otherIngredientCount);
                    }
                    else {
                        movedStack.setCount(otherIngredientCount);
                        stack.shrink(otherIngredientCount);
                    }
                }
            }

            menu.getSlot(inSlot).set(movedStack);
        }
        else if(numberInputItem == otherIngredientCount && numberInputItem < (numOtherIngredientInInventory + numOtherIngredientInGrid) / minOtherIngredientCount) {
            movedStack.setCount(numberInputItem);
            stack.shrink(numberInputItem);
            menu.getSlot(inSlot).set(movedStack);
        }

        player.containerMenu.broadcastChanges();
    }
}
