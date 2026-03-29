package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        simpleWoodenCrate(ModBlocks.OAK_CRATE.get(), Items.OAK_SLAB, Items.OAK_PLANKS, "has_oak_slab");
        simpleWoodenCrate(ModBlocks.SPRUCE_CRATE.get(), Items.SPRUCE_SLAB, Items.SPRUCE_PLANKS, "has_spruce_slab");
        simpleWoodenCrate(ModBlocks.BIRCH_CRATE.get(), Items.BIRCH_SLAB, Items.BIRCH_PLANKS, "has_birch_slab");
        simpleWoodenCrate(ModBlocks.JUNGLE_CRATE.get(), Items.JUNGLE_SLAB, Items.JUNGLE_PLANKS, "has_jungle_slab");
        simpleWoodenCrate(ModBlocks.ACACIA_CRATE.get(), Items.ACACIA_SLAB, Items.ACACIA_PLANKS, "has_acacia_slab");
        simpleWoodenCrate(ModBlocks.DARK_OAK_CRATE.get(), Items.DARK_OAK_SLAB, Items.DARK_OAK_PLANKS, "has_dark_oak_slab");
        simpleWoodenCrate(ModBlocks.MANGROVE_CRATE.get(), Items.MANGROVE_SLAB, Items.MANGROVE_PLANKS, "has_mangrove_slab");
        simpleWoodenCrate(ModBlocks.CHERRY_CRATE.get(), Items.CHERRY_SLAB, Items.CHERRY_PLANKS, "has_cherry_slab");
        simpleWoodenCrate(ModBlocks.BAMBOO_CRATE.get(), Items.BAMBOO_SLAB, Items.BAMBOO_PLANKS, "has_bamboo_slab");
        simpleWoodenCrate(ModBlocks.CRIMSON_CRATE.get(), Items.CRIMSON_SLAB, Items.CRIMSON_PLANKS, "has_crimson_slab");
        simpleWoodenCrate(ModBlocks.WARPED_CRATE.get(), Items.WARPED_SLAB, Items.WARPED_PLANKS, "has_warped_slab");

        simpleFoundation(ModItems.OAK_FOUNDATION_ITEM.get(), Blocks.OAK_PLANKS, "wood_foundation");
        simpleFoundation(ModItems.SPRUCE_FOUNDATION_ITEM.get(), Blocks.SPRUCE_PLANKS, "wood_foundation");
        simpleFoundation(ModItems.BIRCH_FOUNDATION_ITEM.get(), Blocks.BIRCH_PLANKS, "wood_foundation");
        simpleFoundation(ModItems.JUNGLE_FOUNDATION_ITEM.get(), Blocks.JUNGLE_PLANKS, "wood_foundation");
        simpleFoundation(ModItems.ACACIA_FOUNDATION_ITEM.get(), Blocks.ACACIA_PLANKS, "wood_foundation");
        simpleFoundation(ModItems.DARK_OAK_FOUNDATION_ITEM.get(), Blocks.DARK_OAK_PLANKS, "wood_foundation");
        simpleFoundation(ModItems.MANGROVE_FOUNDATION_ITEM.get(), Blocks.MANGROVE_PLANKS, "wood_foundation");
        simpleFoundation(ModItems.CHERRY_FOUNDATION_ITEM.get(), Blocks.CHERRY_PLANKS, "wood_foundation");
        simpleFoundation(ModItems.BAMBOO_FOUNDATION_ITEM.get(), Blocks.BAMBOO_PLANKS, "wood_foundation");
        simpleFoundation(ModItems.CRIMSON_FOUNDATION_ITEM.get(), Blocks.CRIMSON_PLANKS, "wood_foundation");
        simpleFoundation(ModItems.WARPED_FOUNDATION_ITEM.get(), Blocks.WARPED_PLANKS, "wood_foundation");

        simpleFoundation(ModItems.GLASS_FOUNDATION_ITEM.get(), Blocks.GLASS, "glass_foundation");
        simpleFoundation(ModItems.WHITE_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.WHITE_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.LIGHT_GRAY_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.LIGHT_GRAY_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.GRAY_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.GRAY_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.BLACK_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.BLACK_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.BROWN_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.BROWN_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.RED_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.RED_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.ORANGE_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.ORANGE_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.YELLOW_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.YELLOW_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.LIME_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.LIME_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.GREEN_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.GREEN_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.CYAN_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.CYAN_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.LIGHT_BLUE_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.LIGHT_BLUE_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.BLUE_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.BLUE_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.PURPLE_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.PURPLE_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.MAGENTA_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.MAGENTA_STAINED_GLASS, "glass_foundation");
        simpleFoundation(ModItems.PINK_STAINED_GLASS_FOUNDATION_ITEM.get(), Blocks.PINK_STAINED_GLASS, "glass_foundation");

        simpleFoundation(ModItems.IRON_FOUNDATION_ITEM.get(), Blocks.IRON_BLOCK, "ore_foundation");

        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.OBSIDIAN_REINFORCEMENT_ITEM.get(), 8)
                .requires(Items.OBSIDIAN)
                .requires(ModItems.PLIERS_ITEM.get())
                .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                .save(this.output);

        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.LAMP_SIMULATOR_ITEM.get(), 8)
                .requires(Items.REDSTONE_LAMP)
                .requires(ModItems.PLIERS_ITEM.get())
                .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                .save(this.output);

        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.FIREPROOFING_ITEM.get(), 8)
                .requires(Items.MAGMA_CREAM)
                .requires(Items.IRON_INGOT)
                .requires(ModItems.PLIERS_ITEM.get())
                .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                .save(this.output);

        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.SLIMY_FRAMING_ITEM.get(), 8)
                .requires(Items.SLIME_BALL)
                .requires(Items.IRON_INGOT)
                .requires(ModItems.PLIERS_ITEM.get())
                .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                .group("slimy_framing")
                .save(this.output);

        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.SLIMY_FRAMING_ITEM.get(), 8)
                .requires(Items.SLIME_BLOCK)
                .requires(ModItems.PLIERS_ITEM.get())
                .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                .group("slimy_framing")
                .save(this.output, CriticalCrates.MODID + ":slimy_framing_2");

        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.IRON_SUPPORTS_ITEM.get(), 8)
                .requires(Items.IRON_INGOT, 6)
                .requires(ModItems.PLIERS_ITEM.get())
                .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                .group("iron_supports")
                .save(this.output);

        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.IRON_SUPPORTS_ITEM.get(), 8)
                .requires(ModItems.IRON_FOUNDATION_ITEM.get())
                .requires(ModItems.PLIERS_ITEM.get())
                .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                .group("iron_supports")
                .save(this.output,CriticalCrates.MODID + ":iron_supports_2");

        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.SOAP.get(), 8)
                .requires(Items.WATER_BUCKET)
                .requires(Items.PORKCHOP)
                .requires(Items.SCAFFOLDING)
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
                .save(this.output);
    }

    /// Generate a simple wooden crate recipe
    private void simpleWoodenCrate(Block crateBlock, Item woodSlab, Item woodPlank, String unlockedString) {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, crateBlock, 1)
                .pattern("---")
                .pattern("# #")
                .pattern("---")
                .define('-', woodSlab)
                .define('#', woodPlank)
                .unlockedBy(unlockedString, has(woodSlab))
                .group("wooden_crates")
                .save(this.output);
    }

    /// Generate a simple foundation recipe
    private void simpleFoundation(Item foundation, Block blockType, @Nullable String groupName) {
        if(groupName != null) {
            ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, foundation, 2)
                    .requires(blockType)
                    .requires(ModItems.PLIERS_ITEM.get())
                    .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                    .group(groupName)
                    .save(output);
        }
        else {
            ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, foundation, 2)
                    .requires(blockType)
                    .requires(ModItems.PLIERS_ITEM.get())
                    .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                    .save(output);
        }
    }

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return "CriticalCrates Recipes";
        }
    }
}