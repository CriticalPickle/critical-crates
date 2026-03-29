package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
import com.criticalpickle.criticalcrates.block.OreCrateBlock;
import com.criticalpickle.criticalcrates.item.CrateBlockItem;
import com.criticalpickle.criticalcrates.item.PliersItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CriticalCrates.MODID);

    public static final DeferredItem<PliersItem> PLIERS_ITEM = ITEMS.register("pliers", () -> new PliersItem(new Item.Properties().durability(250)));

    public static final DeferredItem<Item> OBSIDIAN_REINFORCEMENT_ITEM = ITEMS.registerSimpleItem("obsidian_reinforcement");
    public static final DeferredItem<Item> LAMP_SIMULATOR_ITEM = ITEMS.registerSimpleItem("lamp_simulator");
    public static final DeferredItem<Item> FIREPROOFING_ITEM = ITEMS.registerSimpleItem("fireproofing");
    public static final DeferredItem<Item> SLIMY_FRAMING_ITEM = ITEMS.registerSimpleItem("slimy_framing");
    public static final DeferredItem<Item> IRON_SUPPORTS_ITEM = ITEMS.registerSimpleItem("iron_supports");
    public static final DeferredItem<Item> SOAP = ITEMS.registerSimpleItem("soap");

    public static final DeferredItem<Item> OAK_FOUNDATION_ITEM = ITEMS.registerSimpleItem("oak_foundation");
    public static final DeferredItem<Item> SPRUCE_FOUNDATION_ITEM = ITEMS.registerSimpleItem("spruce_foundation");
    public static final DeferredItem<Item> BIRCH_FOUNDATION_ITEM = ITEMS.registerSimpleItem("birch_foundation");
    public static final DeferredItem<Item> JUNGLE_FOUNDATION_ITEM = ITEMS.registerSimpleItem("jungle_foundation");
    public static final DeferredItem<Item> ACACIA_FOUNDATION_ITEM = ITEMS.registerSimpleItem("acacia_foundation");
    public static final DeferredItem<Item> DARK_OAK_FOUNDATION_ITEM = ITEMS.registerSimpleItem("dark_oak_foundation");
    public static final DeferredItem<Item> MANGROVE_FOUNDATION_ITEM = ITEMS.registerSimpleItem("mangrove_foundation");
    public static final DeferredItem<Item> CHERRY_FOUNDATION_ITEM = ITEMS.registerSimpleItem("cherry_foundation");
    public static final DeferredItem<Item> BAMBOO_FOUNDATION_ITEM = ITEMS.registerSimpleItem("bamboo_foundation");
    public static final DeferredItem<Item> CRIMSON_FOUNDATION_ITEM = ITEMS.registerSimpleItem("crimson_foundation");
    public static final DeferredItem<Item> WARPED_FOUNDATION_ITEM = ITEMS.registerSimpleItem("warped_foundation");

    public static final DeferredItem<Item> GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("glass_foundation");
    public static final DeferredItem<Item> WHITE_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("white_stained_glass_foundation");
    public static final DeferredItem<Item> LIGHT_GRAY_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("light_gray_stained_glass_foundation");
    public static final DeferredItem<Item> GRAY_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("gray_stained_glass_foundation");
    public static final DeferredItem<Item> BLACK_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("black_stained_glass_foundation");
    public static final DeferredItem<Item> BROWN_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("brown_stained_glass_foundation");
    public static final DeferredItem<Item> RED_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("red_stained_glass_foundation");
    public static final DeferredItem<Item> ORANGE_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("orange_stained_glass_foundation");
    public static final DeferredItem<Item> YELLOW_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("yellow_stained_glass_foundation");
    public static final DeferredItem<Item> LIME_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("lime_stained_glass_foundation");
    public static final DeferredItem<Item> GREEN_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("green_stained_glass_foundation");
    public static final DeferredItem<Item> CYAN_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("cyan_stained_glass_foundation");
    public static final DeferredItem<Item> LIGHT_BLUE_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("light_blue_stained_glass_foundation");
    public static final DeferredItem<Item> BLUE_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("blue_stained_glass_foundation");
    public static final DeferredItem<Item> PURPLE_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("purple_stained_glass_foundation");
    public static final DeferredItem<Item> MAGENTA_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("magenta_stained_glass_foundation");
    public static final DeferredItem<Item> PINK_STAINED_GLASS_FOUNDATION_ITEM = ITEMS.registerSimpleItem("pink_stained_glass_foundation");

    public static final DeferredItem<Item> IRON_FOUNDATION_ITEM = ITEMS.registerSimpleItem("iron_foundation");

    /// Get the items associated with crates
    public static Item[] getCrateItems() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get)
                .filter(item -> item instanceof CrateBlockItem).toArray(Item[]::new);
    }

    /// Get an item associated with a crate dependent on the time of registration
    public static Item getCrateItems(int index) {
        Item[] temp = getCrateItems();
        return temp[index];
    }

    /// Get items associated with wooden crates
    public static Item[] getWoodCrateItems() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get)
                .filter(item ->
                        (item instanceof CrateBlockItem && !(((CrateBlockItem) item).getBlock() instanceof GlassCrateBlock
                                || ((CrateBlockItem) item).getBlock() instanceof OreCrateBlock))
                ).toArray(Item[]::new);
    }

    /// Get an item associated with a wooden crate dependent on the time of registration
    public static Item getWoodCrateItems(int index) {
        Item[] temp = getWoodCrateItems();
        return temp[index];
    }

    /// Get items associated with glass crates
    public static Item[] getGlassCrateItems() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get)
                .filter(item ->
                        (item instanceof CrateBlockItem && ((CrateBlockItem) item).getBlock() instanceof GlassCrateBlock)
                ).toArray(Item[]::new);
    }

    /// Get an item associated with a glass crate dependent on the time of registration
    public static Item getGlassCrateItems(int index) {
        Item[] temp = getGlassCrateItems();
        return temp[index];
    }

    /// Get items associated with ore crates
    public static Item[] getOreCrateItems() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get)
                .filter(item ->
                        (item instanceof CrateBlockItem && ((CrateBlockItem) item).getBlock() instanceof OreCrateBlock block)
                                && block.equals(ModBlocks.IRON_CRATE.get())
                ).toArray(Item[]::new);
    }

    /// Get an item associated with an ore crate dependent on the time of registration
    public static Item getOreCrateItems(int index) {
        Item[] temp = getOreCrateItems();
        return temp[index];
    }

    /// Get items associated with ore upgraded crates
    public static Item[] getOreUpgradedCrateItems() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get)
                .filter(item ->
                        (item instanceof CrateBlockItem && ((CrateBlockItem) item).getBlock() instanceof OreCrateBlock block)
                                && !block.equals(ModBlocks.IRON_CRATE.get())
                ).toArray(Item[]::new);
    }

    /// Get an item associated with an ore upgraded crate dependent on the time of registration
    public static Item getOreUpgradedCrateItems(int index) {
        Item[] temp = getOreUpgradedCrateItems();
        return temp[index];
    }

    /// Get the upgrade items associated with crates
    public static Item[] getCrateUpgrades() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get)
                .filter(item ->
                        !(item instanceof CrateBlockItem) && !(item instanceof PliersItem)
                                && !(item.getDescriptionId().contains("soap"))
                                && !(item.getDescriptionId().contains("foundation"))
                ).toArray(Item[]::new);
    }

    /// Get an upgrade item associated with a crate dependent on the time of registration
    public static Item getCrateUpgrades(int index) {
        Item[] temp = getCrateUpgrades();
        return temp[index];
    }

    /// Get the foundation items associated with crates
    public static Item[] getCrateFoundations() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get)
                .filter(item ->
                        !(item instanceof CrateBlockItem) && !(item instanceof PliersItem)
                                && !(item.getDescriptionId().contains("soap"))
                                && item.getDescriptionId().contains("foundation")
                ).toArray(Item[]::new);
    }

    /// Get a foundation item associated with a crate dependent on the time of registration
    public static Item getCrateFoundations(int index) {
        Item[] temp = getCrateFoundations();
        return temp[index];
    }

    /// Get the item associated with this crate id
    public static Item findCrateItemByID(String ID) {
        String crateID;
        for(int i = 0; i < ModItems.getCrateItems().length; i++) {
            crateID = ModItems.getCrateItems(i).getDescriptionId();
            if(crateID.equals(ID)) {
                return ModItems.getCrateItems(i);
            }
        }
        return null;
    }
}
