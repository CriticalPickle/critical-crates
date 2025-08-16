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
        boolean changedRecipeType = false, changedValidRecipe, giveItemBack, isCrateRecipe = (payload.prevInputItemID().contains("criticalcrates")
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
            int otherIngredientMin;
            Map<String, Object> otherIngredientMap = findRecipeIngredients(recipeReq, recipeID, itemsNeeded);
            Inventory inventory = player.getInventory();
            ItemStack firstValidItemStack;

            otherIngredient = (Item) otherIngredientMap.get("ingredient");
            otherIngredientMin = (Integer) otherIngredientMap.get("amount");

            // If different recipe make sure to return previous crate stack
            changedValidRecipe = changedRecipeType || (!otherIngredient.getDescriptionId().equals(payload.prevOtherIngredientID())
                    && !payload.prevOtherIngredientID().equals("none") && payload.prevInputItemID().contains("criticalcrates")
                    && payload.prevInputItemID().contains("crate") && payload.prevInputItemCount() >= 1);
            giveItemBack = crateItem != null && changedValidRecipe;

            if(giveItemBack) {
                returnCraftingItem(crateItem, payload.prevInputItemCount(), dataTag, player);
            }

            firstValidItemStack = PlayerInventoryUtils.getStackWithIDListAndTag(itemsNeeded, inventory);

            int otherIngredientCount = 0;
            otherIngredientCount += PlayerInventoryUtils.getItemAmount(otherIngredient, inventory);
            otherIngredientCount += CraftingGridUtils.getItemAmount(otherIngredient, player);

            if(!firstValidItemStack.isEmpty() && (otherIngredientCount >= otherIngredientMin)) {
                setCraftingSlotToStack(player, changedValidRecipe, isCrateRecipe, firstValidItemStack, 1, 2,
                        otherIngredientMin, payload.prevOtherIngredientHigh(), payload.prevInputItemHasCraftingTag(),
                        payload.prevInputItemCount(), payload.shiftDown());
            }
        }
        else {
            // If not a recipe requiring crates, make sure crate stack is returned to player
            if(crateItem != null) {
                returnCraftingItem(crateItem, payload.prevInputItemCount(), dataTag, player);
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

    private static void returnCraftingItem(Item crateItem, int count, CompoundTag dataTag, ServerPlayer player) {
        ItemStack returnedStack = new ItemStack(crateItem);
        returnedStack.setCount(count);
        if(dataTag != null) {
            returnedStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
            if(dataTag.contains("fireproof") && dataTag.getBoolean("fireproof")) {
                returnedStack.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
            }
        }
        player.getInventory().add(returnedStack);
    }

    private static void setCraftingSlotToStack(ServerPlayer player, boolean changedRecipe, boolean pastCrateRecipe, ItemStack inventoryStack,
                                               int inSlot, int otherIngredientSlot, int minOtherIngredientCount, int prevOtherIngredientHigh,
                                               boolean hasCraftingTag, int currentNumberInput, boolean shiftDown) {
        CraftingMenu menu = (CraftingMenu) player.containerMenu;
        ItemStack movedStack = inventoryStack.copy(), otherIngredientStack = menu.getSlot(otherIngredientSlot).getItem();
        CompoundTag dataTag;
        int otherIngredientCount = otherIngredientStack.getCount(),
                otherIngredientLow = CraftingGridUtils.getLowestSlotCountOf(otherIngredientStack.getItem(), player),
                numOtherIngredientInInventory = PlayerInventoryUtils.getItemAmount(otherIngredientStack.getItem(), player.getInventory()),
                numOtherIngredientInGrid = CraftingGridUtils.getItemAmount(otherIngredientStack.getItem(), player),
                maxAvailableOtherIngredient = (numOtherIngredientInInventory + numOtherIngredientInGrid) / minOtherIngredientCount,
                craftingDifference, currentNumberInputItem = currentNumberInput;

        dataTag = ItemStackUtils.findStackCustomDataTag(movedStack);
        dataTag.putBoolean("crafting_item", true);
        movedStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));

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
                        inventoryStack.shrink(inventoryStackCount);

                        countToRemove = PlayerInventoryUtils.removeStacksOfID(countToRemove, movedStack.getItem().getDescriptionId(),
                                player.getInventory());

                        if(countToRemove == 0) {
                            movedStack.setCount(otherIngredientCount);
                        }
                        else {
                            movedStack.setCount(currentNumberInputItem);
                        }
                    }
                    else {
                        inventoryStack.shrink(otherIngredientCount);
                        movedStack.setCount(otherIngredientCount);
                    }
                }
                else {
                    inventoryStack.shrink(1);
                    movedStack.setCount(1);
                }

                menu.getSlot(inSlot).set(movedStack);
            }
            else if(currentNumberInputItem < otherIngredientLow && currentNumberInputItem < maxAvailableOtherIngredient) {
                // Crafting tag means player has not clicked there at all and this is server generated (yes even half stacks don't count)
                if(hasCraftingTag) {
                    if(shiftDown) {
                        int inventoryStackCount = inventoryStack.getCount(), countToRemove = craftingDifference - inventoryStackCount;

                        if(inventoryStackCount + currentNumberInputItem < otherIngredientCount) {
                            inventoryStack.shrink(inventoryStackCount);

                            countToRemove = PlayerInventoryUtils.removeStacksOfID(countToRemove, movedStack.getItem().getDescriptionId(),
                                    player.getInventory());

                            if(countToRemove == 0) {
                                movedStack.setCount(otherIngredientCount);
                            }
                            else {
                                movedStack.setCount(currentNumberInputItem);
                            }
                        }
                        else {
                            inventoryStack.shrink(craftingDifference);
                            movedStack.setCount(otherIngredientCount);
                        }
                    }
                    else {
                        inventoryStack.shrink(1);
                        movedStack.setCount(otherIngredientLow);
                    }

                    menu.getSlot(inSlot).set(movedStack);
                }
                else {
                    if(shiftDown) {
                        int inventoryStackCount = inventoryStack.getCount(), countToRemove = craftingDifference - inventoryStackCount;
                        if(inventoryStackCount < otherIngredientCount) {
                            inventoryStack.shrink(inventoryStackCount);

                            countToRemove = PlayerInventoryUtils.removeStacksOfID(countToRemove, movedStack.getItem().getDescriptionId(),
                                    player.getInventory());

                            if(countToRemove == 0) {
                                movedStack.setCount(otherIngredientCount);
                            }
                            else {
                                movedStack.setCount(currentNumberInputItem);
                            }
                        }
                        else {
                            inventoryStack.shrink(craftingDifference);
                            movedStack.setCount(otherIngredientCount);
                        }
                    }
                    else {
                        inventoryStack.shrink(craftingDifference);
                        movedStack.setCount(otherIngredientCount);
                    }
                }

                menu.getSlot(inSlot).set(movedStack);
            }
            else if(currentNumberInputItem > otherIngredientLow && currentNumberInputItem < maxAvailableOtherIngredient) {
                if(shiftDown) {
                    int inventoryStackCount = inventoryStack.getCount(), countToRemove = otherIngredientCount - inventoryStackCount + currentNumberInputItem;
                    if(inventoryStackCount + currentNumberInputItem < otherIngredientCount) {
                        inventoryStack.shrink(inventoryStackCount);

                        countToRemove = PlayerInventoryUtils.removeStacksOfID(countToRemove, movedStack.getItem().getDescriptionId(),
                                player.getInventory());

                        if(countToRemove == 0) {
                            movedStack.setCount(otherIngredientCount);
                        }
                        else {
                            movedStack.setCount(currentNumberInputItem);
                        }
                    }
                    else {
                        inventoryStack.shrink(craftingDifference);
                        movedStack.setCount(otherIngredientCount);
                    }
                }
                else {
                    inventoryStack.shrink(otherIngredientLow - currentNumberInputItem);
                    movedStack.setCount(otherIngredientLow);
                }

                menu.getSlot(inSlot).set(movedStack);
            }
            else if(currentNumberInputItem == otherIngredientLow && currentNumberInputItem < maxAvailableOtherIngredient) {
                if(shiftDown) {
                    int inventoryStackCount = inventoryStack.getCount(), countToRemove = otherIngredientCount - inventoryStackCount + currentNumberInputItem;
                    if(inventoryStackCount + currentNumberInputItem < otherIngredientCount) {
                        inventoryStack.shrink(inventoryStackCount);

                        countToRemove = PlayerInventoryUtils.removeStacksOfID(countToRemove, movedStack.getItem().getDescriptionId(),
                                player.getInventory());

                        if(countToRemove == 0) {
                            movedStack.setCount(otherIngredientCount);
                        }
                        else {
                            movedStack.setCount(currentNumberInputItem);
                        }
                    }
                    else {
                        inventoryStack.shrink(craftingDifference);
                        movedStack.setCount(otherIngredientCount);
                    }
                }
                else {
                    movedStack.setCount(currentNumberInputItem);
                    menu.getSlot(inSlot).set(movedStack);
                }

                menu.getSlot(inSlot).set(movedStack);
            }
        }

        player.containerMenu.broadcastChanges();
    }
}
