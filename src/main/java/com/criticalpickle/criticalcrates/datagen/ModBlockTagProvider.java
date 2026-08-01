package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, CriticalCrates.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        ModBlocks.getWoodCrateKeys().forEach(key -> this.tag(BlockTags.MINEABLE_WITH_AXE).add(key));
        ModBlocks.getWoodCrateKeys().forEach(key -> this.tag(ModTags.Blocks.WOODEN_CRATES).add(key));

        ModBlocks.getGlassCrateKeys().forEach(key -> this.tag(ModTags.Blocks.GLASS_CRATES).add(key));

        ModBlocks.getOreCrateKeys().forEach(key -> this.tag(ModTags.Blocks.ORE_CRATES).add(key));

        ModBlocks.getOreUpgradedCrateKeys().forEach(key -> this.tag(ModTags.Blocks.ORE_UPGRADED_CRATES).add(key));

        ModBlocks.getSoilCrateKeys().forEach(key -> this.tag(ModTags.Blocks.SOIL_CRATES).add(key));

        ModBlocks.getCrateKeys().forEach(key -> this.tag(ModTags.Blocks.CRATES).add(key));
    }
}
