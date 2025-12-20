package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.registration.ModTags;
import com.criticalpickle.criticalcrates.util.IDUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends TagsProvider<Item> {


    protected ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Registries.ITEM, registries, CriticalCrates.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registries) {
        this.getOrCreateRawBuilder(ItemTags.DURABILITY_ENCHANTABLE)
                .addElement(Identifier.fromNamespaceAndPath("criticalcrates", IDUtils.getItemID(ModItems.PLIERS_ITEM.get())));

        this.getOrCreateRawBuilder(ItemTags.VANISHING_ENCHANTABLE)
                .addElement(Identifier.fromNamespaceAndPath("criticalcrates", IDUtils.getItemID(ModItems.PLIERS_ITEM.get())));

        this.getOrCreateRawBuilder(Tags.Items.TOOLS)
                .addElement(Identifier.fromNamespaceAndPath("criticalcrates", IDUtils.getItemID(ModItems.PLIERS_ITEM.get())));

        this.getOrCreateRawBuilder(Tags.Items.ENCHANTABLES)
                .addElement(Identifier.fromNamespaceAndPath("criticalcrates", IDUtils.getItemID(ModItems.PLIERS_ITEM.get())));

        TagBuilder builder = this.getOrCreateRawBuilder(ModTags.Items.WOODEN_CRATES);
        for(int i = 0; i < ModItems.getWoodCrateItems().length; i++) {
            builder.addElement(Identifier.fromNamespaceAndPath("criticalcrates", IDUtils.getItemID(ModItems.getWoodCrateItems(i))));
        }

        builder = this.getOrCreateRawBuilder(ModTags.Items.GLASS_CRATES);
        for(int i = 0; i < ModItems.getGlassCrateItems().length; i++) {
            builder.addElement(Identifier.fromNamespaceAndPath("criticalcrates", IDUtils.getItemID(ModItems.getGlassCrateItems(i))));
        }

        builder = this.getOrCreateRawBuilder(ModTags.Items.CRATES);
        for(int i = 0; i < ModItems.getCrateItems().length; i++) {
            builder.addElement(Identifier.fromNamespaceAndPath("criticalcrates", IDUtils.getItemID(ModItems.getCrateItems(i))));
        }

        builder = this.getOrCreateRawBuilder(ModTags.Items.CRATE_UPGRADES);
        for(int i = 0; i < ModItems.getCrateUpgrades().length; i++) {
            builder.addElement(Identifier.fromNamespaceAndPath("criticalcrates", IDUtils.getItemID(ModItems.getCrateUpgrades(i))));
        }
    }
}
