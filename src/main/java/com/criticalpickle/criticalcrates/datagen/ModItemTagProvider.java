package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.registration.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
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
                .addElement(Identifier.fromNamespaceAndPath("criticalcrates", ModItems.PLIERS_ITEM.get().getName().getString()));

        this.getOrCreateRawBuilder(ItemTags.VANISHING_ENCHANTABLE)
                .addElement(Identifier.fromNamespaceAndPath("criticalcrates", ModItems.PLIERS_ITEM.get().getName().getString()));

        this.getOrCreateRawBuilder(Tags.Items.TOOLS)
                .addElement(Identifier.fromNamespaceAndPath("criticalcrates", ModItems.PLIERS_ITEM.get().getName().getString()));

        this.getOrCreateRawBuilder(Tags.Items.ENCHANTABLES)
                .addElement(Identifier.fromNamespaceAndPath("criticalcrates", ModItems.PLIERS_ITEM.get().getName().getString()));

//        this.getOrCreateRawBuilder(ModTags.Items.WOODEN_CRATES)
//                .addElement(ModItems.getWoodCrateItems());
//
//        this.getOrCreateRawBuilder(ModTags.Items.GLASS_CRATES)
//                .addElement(ModItems.getGlassCrateItems());
//
//        this.getOrCreateRawBuilder(ModTags.Items.CRATES)
//                .addElement(ModItems.getCrateItems());
//
//        this.getOrCreateRawBuilder(ModTags.Items.CRATE_UPGRADES)
//                .addElement(ModItems.getCrateUpgrades());
    }
}
