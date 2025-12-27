package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CriticalCrates.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for(int i = 0; i < ModItems.getCrateItems().length; i++) {
            blockItemWithOverrides(ModItems.getCrateItems(i));
        }
        basicItem(ModItems.PLIERS_ITEM.get());
        basicItem(ModItems.OBSIDIAN_REINFORCEMENT_ITEM.get());
        basicItem(ModItems.LAMP_SIMULATOR_ITEM.get());
        basicItem(ModItems.FIREPROOFING_ITEM.get());
        basicItem(ModItems.SLIMY_FRAMING_ITEM.get());
        basicItem(ModItems.SOAP.get());
    }

    private void blockItemWithOverrides(Item item) {
        String itemName = item.getDescriptionId().substring(item.getDescriptionId().indexOf("s.") + 2);
        ItemModelBuilder builder = withExistingParent(itemName, modLoc("block/" + itemName));
        ResourceLocation resistanceResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "resistant");
        ResourceLocation lampResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "lamp");
        ResourceLocation fireResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "fire");
        ResourceLocation slimyResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "slimy");

        // Generate other variations
        withExistingParent(itemName + "_resistant", modLoc("block/" + itemName + "_resistant"));
        withExistingParent(itemName + "_lamp", modLoc("block/" + itemName + "_lamp"));
        withExistingParent(itemName + "_fireproof", modLoc("block/" + itemName + "_fireproof"));
        withExistingParent(itemName + "_slimy", modLoc("block/" + itemName + "_slimy"));

        // Generate base variation with appropriate references to overrides
        builder.override()
                .predicate(resistanceResLoc, 1)
                .model(getExistingFile(modLoc("block/" + itemName + "_resistant")))
                .end()
                .override()
                .predicate(lampResLoc, 1)
                .model(getExistingFile(modLoc("block/" + itemName + "_lamp")))
                .end()
                .override()
                .predicate(fireResLoc, 1)
                .model(getExistingFile(modLoc("block/" + itemName + "_fireproof")))
                .end()
                .override()
                .predicate(slimyResLoc, 1)
                .model(getExistingFile(modLoc("block/" + itemName + "_slimy")))
                .end();
    }
}
