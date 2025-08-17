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
    public record RecipeBookClickPayload(ResourceLocation recipeID, String prevOtherIngredientID, int prevOtherIngredientHigh,
                                         boolean prevInputItemHasCraftingTag, String prevInputItemID, CompoundTag prevInputItemDataTag,
                                         int prevInputItemCount, boolean shiftDown) implements CustomPacketPayload {
        public static final Type<RecipeBookClickPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CriticalCrates.MODID, "recipe_book_click"));

        public static final StreamCodec<FriendlyByteBuf, RecipeBookClickPayload> STREAM_CODEC = CustomPacketPayload.codec(
                (payload, buf) -> {
                    buf.writeResourceLocation(payload.recipeID);
                    buf.writeUtf(payload.prevOtherIngredientID);
                    buf.writeInt(payload.prevOtherIngredientHigh);
                    buf.writeBoolean(payload.prevInputItemHasCraftingTag);
                    buf.writeUtf(payload.prevInputItemID);
                    buf.writeNbt(payload.prevInputItemDataTag);
                    buf.writeInt(payload.prevInputItemCount);
                    buf.writeBoolean(payload.shiftDown);
                },
                buf -> new RecipeBookClickPayload(buf.readResourceLocation(), buf.readUtf(), buf.readInt(), buf.readBoolean(), buf.readUtf(), buf.readNbt(), buf.readInt(), buf.readBoolean())
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void handle(RecipeBookClickPayload payload, ServerPlayer player) {
        String recipeID = payload.recipeID().toString(), recipeReq = "none";
        Item crateItem = null;
        boolean changedRecipeType = false, noMoreItemsOfTag = false, invalidComponentItemAmount = false,
                changedValidRecipe, giveItemBack, isCrateRecipe = (payload.prevInputItemID().contains("criticalcrates")
                && payload.prevInputItemID().contains("crate")) || payload.prevInputItemID().equals("none");
        CompoundTag dataTag = payload.prevInputItemDataTag();

        if(isCrateRecipe) {
            crateItem = ModItems.findCrateItemByID(payload.prevInputItemID());
        }

        // What is needed for recipe? Is recipe different from before?
        if(recipeID.contains("glass_crate") && !recipeID.contains("dye")) {
            recipeReq = "wood_crate";
            changedRecipeType = crateItem instanceof CrateBlockItem crateBlockItem && crateBlockItem.getBlock() instanceof GlassCrateBlock;
        }
        else if(recipeID.contains("dye") && recipeID.contains("glass_crate")){
            recipeReq = "glass_crate";
            changedRecipeType = crateItem instanceof CrateBlockItem crateBlockItem && !(crateBlockItem.getBlock() instanceof GlassCrateBlock);
        }

        if(!recipeReq.equals("none")) {
            List<String> itemsNeeded = new ArrayList<>(List.of());
            Item otherIngredient;
            Map<String, Object> otherIngredientMap = findRecipeIngredients(recipeReq, recipeID, itemsNeeded);
            Inventory inventory = player.getInventory();
            ItemStack firstValidItemStack;

            int otherIngredientMin;
            otherIngredient = (Item) otherIngredientMap.get("ingredient");
            otherIngredientMin = (Integer) otherIngredientMap.get("amount");

            // Find valid item stack depending on if the first one has dataTag or not
            int validInventoryItemCount = 0, validGridItemCount = 0;
            if(dataTag != null) {
                firstValidItemStack = PlayerInventoryUtils.getStackWithIDListAndTag(itemsNeeded, dataTag, inventory);
            }
            else {
                firstValidItemStack = PlayerInventoryUtils.getStackWithIDList(itemsNeeded, inventory);

                // Set valid item count here in case something that has null data needs to trigger slot changes
                validInventoryItemCount = PlayerInventoryUtils.getItemCount(firstValidItemStack.getItem(), player.getInventory());
                validGridItemCount = CraftingGridUtils.getItemCount(firstValidItemStack.getItem(), (CraftingMenu) player.containerMenu);
            }

            // Two different cases for data components in the stacks (second is needed for "empty" slot calculations)
            if(dataTag != null) {
                validInventoryItemCount = PlayerInventoryUtils.getItemIDWithDataTagCount(payload.prevInputItemID(), dataTag, player.getInventory());
                validGridItemCount = CraftingGridUtils.getItemIDWithDataTagCount(payload.prevInputItemID(), dataTag, (CraftingMenu) player.containerMenu);
                noMoreItemsOfTag = payload.prevInputItemID().contains("criticalcrates") && payload.prevInputItemID().contains("crate")
                        && validInventoryItemCount <= 0;
            }
            else if(!firstValidItemStack.isEmpty() && firstValidItemStack.get(DataComponents.CUSTOM_DATA) != null) {
                CompoundTag validStackTag = firstValidItemStack.get(DataComponents.CUSTOM_DATA).copyTag();
                validInventoryItemCount = PlayerInventoryUtils.getItemIDWithDataTagCount(firstValidItemStack.getItem().getDescriptionId(), validStackTag, player.getInventory());
                validGridItemCount = CraftingGridUtils.getItemIDWithDataTagCount(firstValidItemStack.getItem().getDescriptionId(), validStackTag, (CraftingMenu) player.containerMenu);
            }

            // If different recipe, or no more items of this stack in inventory, make sure to return previous crate stack
            changedValidRecipe = changedRecipeType || (!otherIngredient.getDescriptionId().equals(payload.prevOtherIngredientID())
                    && !payload.prevOtherIngredientID().equals("none") && payload.prevInputItemID().contains("criticalcrates")
                    && payload.prevInputItemID().contains("crate") && payload.prevInputItemCount() >= 1);
            giveItemBack = crateItem != null && (changedValidRecipe || noMoreItemsOfTag);

            if(giveItemBack) {
                CraftingGridUtils.returnCraftingItem(crateItem, payload.prevInputItemCount(), dataTag, player.getInventory());
            }

            int otherIngredientCount = 0, maxAvailableOtherIngredient, validItemCount;
            otherIngredientCount += PlayerInventoryUtils.getItemCount(otherIngredient, inventory);
            otherIngredientCount += CraftingGridUtils.getItemCount(otherIngredient, (CraftingMenu) player.containerMenu);

            maxAvailableOtherIngredient = otherIngredientCount / otherIngredientMin;
            validItemCount = validInventoryItemCount + validGridItemCount;
            invalidComponentItemAmount = (validItemCount < 64 && validItemCount < maxAvailableOtherIngredient && payload.shiftDown());

            // Variable input slot index for easier editing
            int inSlot = 1;

            // Determine if slot logic needs special component case or not
            if(!firstValidItemStack.isEmpty() && !invalidComponentItemAmount && otherIngredientCount >= otherIngredientMin) {
                setCraftingSlotToStack(player, changedValidRecipe, isCrateRecipe, firstValidItemStack, inSlot, 2,
                        otherIngredientMin, payload.prevOtherIngredientHigh(), payload.prevInputItemHasCraftingTag(),
                        payload.prevInputItemCount(), payload.shiftDown());
            }
            else if(invalidComponentItemAmount || noMoreItemsOfTag) {
                CraftingMenu craftingMenu = (CraftingMenu) player.containerMenu;
                CompoundTag tag;
                int moveCount;

                // If all valid stacks are gone from inventory check again (as it should be returned then put back into the grid)
                if(firstValidItemStack.isEmpty()) {
                    firstValidItemStack = PlayerInventoryUtils.getStackWithIDListAndTag(itemsNeeded, dataTag, inventory);
                }

                // Add crafting item tag
                tag = ItemStackUtils.findStackCustomDataTag(firstValidItemStack);
                tag.putBoolean("crafting_item", true);
                ItemStack moveStack = firstValidItemStack.copy();
                moveStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

                // Exception crafting slot handling
                if(invalidComponentItemAmount) {
                    if(payload.prevInputItemHasCraftingTag() && validItemCount <= 0) {
                        moveCount = payload.prevInputItemCount();
                    }
                    else if(payload.prevInputItemCount() <= 0) {
                        moveCount = firstValidItemStack.getCount();
                    }
                    else {
                        moveCount = payload.prevInputItemCount() + firstValidItemStack.getCount();
                    }

                    moveStack.setCount(moveCount);
                    firstValidItemStack.shrink(firstValidItemStack.getCount());
                    craftingMenu.getSlot(inSlot).set(moveStack);

                    // Make sure refund bounds are correct
                    if(maxAvailableOtherIngredient < firstValidItemStack.getItem().getDefaultMaxStackSize()) {
                        CraftingGridUtils.removeAndRefund(otherIngredient.getDescriptionId(), craftingMenu, player.getInventory(),
                                (maxAvailableOtherIngredient - moveCount) * otherIngredientMin);
                    }
                    else {
                        CraftingGridUtils.removeAndRefund(otherIngredient.getDescriptionId(), craftingMenu, player.getInventory(),
                                (firstValidItemStack.getItem().getDefaultMaxStackSize() - moveCount) * otherIngredientMin);
                    }
                }
                else {
                    CraftingGridUtils.removeAndRefund(otherIngredient.getDescriptionId(), craftingMenu, player.getInventory(), otherIngredientMin);
                    craftingMenu.getSlot(inSlot).set(moveStack);
                    firstValidItemStack.shrink(firstValidItemStack.getCount());
                }

                craftingMenu.broadcastChanges();
            }
        }
        else {
            // If not a recipe requiring crates, make sure crate stack is returned to player
            if(crateItem != null) {
                CraftingGridUtils.returnCraftingItem(crateItem, payload.prevInputItemCount(), dataTag, player.getInventory());
            }
        }
    }

    // Find both the correct crate and "other" ingredients
    private static Map<String, Object> findRecipeIngredients(String recipeReq, String recipeID, List<String> itemIDs) {
        int otherIngredientAmount = 0;
        Item otherIngredient = null;
        if(recipeReq.equals("wood_crate")) {
            String crateName = recipeID.substring(recipeID.indexOf(":") + 1);
            String color = "none";
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

            if(!crateName.equals("glass_crate")) {
                color = crateName.substring(0, crateName.indexOf("_stained"));
                for (Item pane : panes) {
                    if (pane.getDescriptionId().contains(color)) {
                        otherIngredient = pane;
                    }
                }
            }
            else {
                otherIngredient = Items.GLASS_PANE;
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

    private static void setCraftingSlotToStack(ServerPlayer player, boolean changedRecipe, boolean pastCrateRecipe, ItemStack inventoryStack,
                                               int inSlot, int otherIngredientSlot, int minOtherIngredientCount, int prevOtherIngredientHigh,
                                               boolean hasCraftingTag, int currentNumberInput, boolean shiftDown) {
        CraftingMenu menu = (CraftingMenu) player.containerMenu;
        ItemStack moveStack = inventoryStack.copy(), otherIngredientStack = menu.getSlot(otherIngredientSlot).getItem();
        CompoundTag dataTag;
        CustomData data;
        int otherIngredientCount = otherIngredientStack.getCount(),
                otherIngredientLow = CraftingGridUtils.getLowestSlotCountOf(otherIngredientStack.getItem(), player),
                numOtherIngredientInInventory = PlayerInventoryUtils.getItemCount(otherIngredientStack.getItem(), player.getInventory()),
                numOtherIngredientInGrid = CraftingGridUtils.getItemCount(otherIngredientStack.getItem(), (CraftingMenu) player.containerMenu),
                maxAvailableOtherIngredient = (numOtherIngredientInInventory + numOtherIngredientInGrid) / minOtherIngredientCount,
                craftingDifference, currentNumberInputItem = currentNumberInput;

        dataTag = ItemStackUtils.findStackCustomDataTag(moveStack);
        dataTag.putBoolean("crafting_item", true);
        moveStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));

        /* Crafting grid logic to determine input inventoryStack count change and inventory crate count
            remember in this case "current" is previous, as crate has not been moved yet */
        if(prevOtherIngredientHigh != maxAvailableOtherIngredient || changedRecipe) {
            // If past recipe was something unrelated discard the count
            if(!pastCrateRecipe) {
                currentNumberInputItem = 0;
            }

            // If recipe has been changed (i.e. crate related) make sure to reset difference calculation
            if(!changedRecipe) {
                craftingDifference = otherIngredientCount - currentNumberInputItem;
            }
            else {
                craftingDifference = otherIngredientCount;
            }

            if(currentNumberInputItem <= 0 || changedRecipe || !pastCrateRecipe) {
                if(shiftDown) {
                    int inventoryStackCount = inventoryStack.getCount(), countToRemove = otherIngredientCount - inventoryStackCount;
                    if(inventoryStackCount < otherIngredientCount) {
                        data = inventoryStack.get(DataComponents.CUSTOM_DATA);
                        inventoryStack.shrink(inventoryStackCount);

                        countToRemove = PlayerInventoryUtils.removeStacksOfIDAndData(countToRemove, moveStack.getItem().getDescriptionId(),
                                data, player.getInventory());

                        if(countToRemove == 0) {
                            moveStack.setCount(otherIngredientCount);
                        }
                        else {
                            moveStack.setCount(currentNumberInputItem);
                        }
                    }
                    else {
                        inventoryStack.shrink(otherIngredientCount);
                        moveStack.setCount(otherIngredientCount);
                    }
                }
                else {
                    inventoryStack.shrink(1);
                    moveStack.setCount(1);
                }

                menu.getSlot(inSlot).set(moveStack);
            }
            else if(currentNumberInputItem < otherIngredientLow && currentNumberInputItem < maxAvailableOtherIngredient) {
                // Crafting tag means player has not clicked there at all and this is server generated (yes even half stacks don't count)
                if(hasCraftingTag) {
                    if(shiftDown) {
                        int inventoryStackCount = inventoryStack.getCount(), countToRemove = craftingDifference - inventoryStackCount;
                        if(inventoryStackCount + currentNumberInputItem < otherIngredientCount) {
                            data = inventoryStack.get(DataComponents.CUSTOM_DATA);
                            inventoryStack.shrink(inventoryStackCount);

                            countToRemove = PlayerInventoryUtils.removeStacksOfIDAndData(countToRemove, moveStack.getItem().getDescriptionId(),
                                    data, player.getInventory());

                            if(countToRemove == 0) {
                                moveStack.setCount(otherIngredientCount);
                            }
                            else {
                                moveStack.setCount(currentNumberInputItem);
                            }
                        }
                        else {
                            inventoryStack.shrink(craftingDifference);
                            moveStack.setCount(otherIngredientCount);
                        }
                    }
                    else {
                        inventoryStack.shrink(1);
                        moveStack.setCount(otherIngredientLow);
                    }

                    menu.getSlot(inSlot).set(moveStack);
                }
                else {
                    if(shiftDown) {
                        int inventoryStackCount = inventoryStack.getCount(), countToRemove = craftingDifference - inventoryStackCount;
                        if(inventoryStackCount < otherIngredientCount) {
                            data = inventoryStack.get(DataComponents.CUSTOM_DATA);
                            inventoryStack.shrink(inventoryStackCount);

                            countToRemove = PlayerInventoryUtils.removeStacksOfIDAndData(countToRemove, moveStack.getItem().getDescriptionId(),
                                     data, player.getInventory());

                            if(countToRemove == 0) {
                                moveStack.setCount(otherIngredientCount);
                            }
                            else {
                                moveStack.setCount(currentNumberInputItem);
                            }
                        }
                        else {
                            inventoryStack.shrink(craftingDifference);
                            moveStack.setCount(otherIngredientCount);
                        }
                    }
                    else {
                        inventoryStack.shrink(craftingDifference);
                        moveStack.setCount(otherIngredientCount);
                    }
                }

                menu.getSlot(inSlot).set(moveStack);
            }
            else if(currentNumberInputItem > otherIngredientLow && currentNumberInputItem < maxAvailableOtherIngredient) {
                if(shiftDown) {
                    int inventoryStackCount = inventoryStack.getCount(), countToRemove = otherIngredientCount - inventoryStackCount + currentNumberInputItem;
                    if(inventoryStackCount + currentNumberInputItem < otherIngredientCount) {
                        data = inventoryStack.get(DataComponents.CUSTOM_DATA);
                        inventoryStack.shrink(inventoryStackCount);

                        countToRemove = PlayerInventoryUtils.removeStacksOfIDAndData(countToRemove, moveStack.getItem().getDescriptionId(),
                                data, player.getInventory());

                        if(countToRemove == 0) {
                            moveStack.setCount(otherIngredientCount);
                        }
                        else {
                            moveStack.setCount(currentNumberInputItem);
                        }
                    }
                    else {
                        inventoryStack.shrink(craftingDifference);
                        moveStack.setCount(otherIngredientCount);
                    }
                }
                else {
                    inventoryStack.shrink(otherIngredientLow - currentNumberInputItem);
                    moveStack.setCount(otherIngredientLow);
                }

                menu.getSlot(inSlot).set(moveStack);
            }
            else if(currentNumberInputItem == otherIngredientLow && currentNumberInputItem < maxAvailableOtherIngredient) {
                if(shiftDown) {
                    int inventoryStackCount = inventoryStack.getCount(), countToRemove = otherIngredientCount - inventoryStackCount + currentNumberInputItem;
                    if(inventoryStackCount + currentNumberInputItem < otherIngredientCount) {
                        data = inventoryStack.get(DataComponents.CUSTOM_DATA);
                        inventoryStack.shrink(inventoryStackCount);

                        countToRemove = PlayerInventoryUtils.removeStacksOfIDAndData(countToRemove, moveStack.getItem().getDescriptionId(),
                                data, player.getInventory());

                        if(countToRemove == 0) {
                            moveStack.setCount(otherIngredientCount);
                        }
                        else {
                            moveStack.setCount(currentNumberInputItem);
                        }
                    }
                    else {
                        inventoryStack.shrink(craftingDifference);
                        moveStack.setCount(otherIngredientCount);
                    }
                }
                else {
                    moveStack.setCount(currentNumberInputItem);
                    menu.getSlot(inSlot).set(moveStack);
                }

                menu.getSlot(inSlot).set(moveStack);
            }
        }

        player.containerMenu.broadcastChanges();
    }
}
