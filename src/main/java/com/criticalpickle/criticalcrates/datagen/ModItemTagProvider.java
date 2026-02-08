package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.registration.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, CriticalCrates.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(ModItems.PLIERS_ITEM.get());

        tag(ItemTags.VANISHING_ENCHANTABLE)
                .add(ModItems.PLIERS_ITEM.get());

        tag(Tags.Items.TOOLS)
                .add(ModItems.PLIERS_ITEM.get());

        tag(Tags.Items.ENCHANTABLES)
                .add(ModItems.PLIERS_ITEM.get());

        tag(ModTags.Items.WOODEN_CRATES)
                .add(ModItems.getWoodCrateItems());

        tag(ModTags.Items.GLASS_CRATES)
                .add(ModItems.getGlassCrateItems());

        tag(ModTags.Items.ORE_CRATES)
                .add(ModItems.getOreCrateItems());

        tag(ModTags.Items.ORE_UPGRADED_CRATES)
                .add(ModItems.getOreUpgradedCrateItems());

        tag(ModTags.Items.CRATES)
                .add(ModItems.getCrateItems());

        tag(ModTags.Items.CRATE_UPGRADES)
                .add(ModItems.getCrateUpgrades());
    }
}
