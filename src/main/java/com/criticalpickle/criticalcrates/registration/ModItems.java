package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
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
    public static final DeferredItem<Item> SOAP = ITEMS.registerSimpleItem("soap");

    // Get the items associated with crates
    public static Item[] getCrateItems() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof CrateBlockItem).toArray(Item[]::new);
    }

    // Get an item associated with a crate dependent on the time of registration
    public static Item getCrateItems(int index) {
        Item[] temp = ITEMS.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof CrateBlockItem).toArray(Item[]::new);
        return temp[index];
    }

    // Get items associated with wooden crates
    public static Item[] getWoodCrateItems() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get).filter(item -> (item instanceof CrateBlockItem && !(((CrateBlockItem) item).getBlock() instanceof GlassCrateBlock))).toArray(Item[]::new);
    }

    // Get an item associated with a wooden crate dependent on the time of registration
    public static Item getWoodCrateItems(int index) {
        Item[] temp = ITEMS.getEntries().stream().map(DeferredHolder::get).filter(item -> (item instanceof CrateBlockItem && !(((CrateBlockItem) item).getBlock() instanceof GlassCrateBlock))).toArray(Item[]::new);
        return temp[index];
    }

    // Get items associated with glass crates
    public static Item[] getGlassCrateItems() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get).filter(item -> (item instanceof CrateBlockItem && ((CrateBlockItem) item).getBlock() instanceof GlassCrateBlock)).toArray(Item[]::new);
    }

    // Get an item associated with a glass crate dependent on the time of registration
    public static Item getGlassCrateItems(int index) {
        Item[] temp = ITEMS.getEntries().stream().map(DeferredHolder::get).filter(item -> (item instanceof CrateBlockItem && ((CrateBlockItem) item).getBlock() instanceof GlassCrateBlock)).toArray(Item[]::new);
        return temp[index];
    }

    // Get the items associated with crates
    public static Item[] getCrateUpgrades() {
        return ITEMS.getEntries().stream().map(DeferredHolder::get).filter(item -> !(item instanceof CrateBlockItem) && !(item instanceof PliersItem) && !(item.getDescriptionId().contains("soap"))).toArray(Item[]::new);
    }

    // Get an item associated with a crate dependent on the time of registration
    public static Item getCrateUpgrades(int index) {
        Item[] temp = ITEMS.getEntries().stream().map(DeferredHolder::get).filter(item -> !(item instanceof CrateBlockItem)  && !(item instanceof PliersItem) && !(item.getDescriptionId().contains("soap"))).toArray(Item[]::new);
        return temp[index];
    }

    // Get the item associated with this crate id
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
