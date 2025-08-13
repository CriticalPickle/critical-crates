package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.registration.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        simpleCrate(ModBlocks.OAK_CRATE.get(), Items.OAK_SLAB, Items.OAK_PLANKS, "has_oak_slab", recipeOutput);
        simpleCrate(ModBlocks.SPRUCE_CRATE.get(), Items.SPRUCE_SLAB, Items.SPRUCE_PLANKS, "has_spruce_slab", recipeOutput);
        simpleCrate(ModBlocks.BIRCH_CRATE.get(), Items.BIRCH_SLAB, Items.BIRCH_PLANKS, "has_birch_slab", recipeOutput);
        simpleCrate(ModBlocks.JUNGLE_CRATE.get(), Items.JUNGLE_SLAB, Items.JUNGLE_PLANKS, "has_jungle_slab", recipeOutput);
        simpleCrate(ModBlocks.ACACIA_CRATE.get(), Items.ACACIA_SLAB, Items.ACACIA_PLANKS, "has_acacia_slab", recipeOutput);
        simpleCrate(ModBlocks.DARK_OAK_CRATE.get(), Items.DARK_OAK_SLAB, Items.DARK_OAK_PLANKS, "has_dark_oak_slab", recipeOutput);
        simpleCrate(ModBlocks.MANGROVE_CRATE.get(), Items.MANGROVE_SLAB, Items.MANGROVE_PLANKS, "has_mangrove_slab", recipeOutput);
        simpleCrate(ModBlocks.CHERRY_CRATE.get(), Items.CHERRY_SLAB, Items.CHERRY_PLANKS, "has_cherry_slab", recipeOutput);
        simpleCrate(ModBlocks.BAMBOO_CRATE.get(), Items.BAMBOO_SLAB, Items.BAMBOO_PLANKS, "has_bamboo_slab", recipeOutput);
        simpleCrate(ModBlocks.CRIMSON_CRATE.get(), Items.CRIMSON_SLAB, Items.CRIMSON_PLANKS, "has_crimson_slab", recipeOutput);
        simpleCrate(ModBlocks.WARPED_CRATE.get(), Items.WARPED_SLAB, Items.WARPED_PLANKS, "has_warped_slab", recipeOutput);

        simpleGlassCrate(ModBlocks.GLASS_CRATE.get(), Blocks.GLASS_PANE, null, recipeOutput);
        simpleGlassCrate(ModBlocks.WHITE_STAINED_GLASS_CRATE.get(), Blocks.WHITE_STAINED_GLASS_PANE, Items.WHITE_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.LIGHT_GRAY_STAINED_GLASS_CRATE.get(), Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, Items.LIGHT_GRAY_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.GRAY_STAINED_GLASS_CRATE.get(), Blocks.GRAY_STAINED_GLASS_PANE, Items.GRAY_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.BLACK_STAINED_GLASS_CRATE.get(), Blocks.BLACK_STAINED_GLASS_PANE, Items.BLACK_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.BROWN_STAINED_GLASS_CRATE.get(), Blocks.BROWN_STAINED_GLASS_PANE, Items.BROWN_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.RED_STAINED_GLASS_CRATE.get(), Blocks.RED_STAINED_GLASS_PANE, Items.RED_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.ORANGE_STAINED_GLASS_CRATE.get(), Blocks.ORANGE_STAINED_GLASS_PANE, Items.ORANGE_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.YELLOW_STAINED_GLASS_CRATE.get(), Blocks.YELLOW_STAINED_GLASS_PANE, Items.YELLOW_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.LIME_STAINED_GLASS_CRATE.get(), Blocks.LIME_STAINED_GLASS_PANE, Items.LIME_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.GREEN_STAINED_GLASS_CRATE.get(), Blocks.GREEN_STAINED_GLASS_PANE, Items.GREEN_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.CYAN_STAINED_GLASS_CRATE.get(), Blocks.CYAN_STAINED_GLASS_PANE, Items.CYAN_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.LIGHT_BLUE_STAINED_GLASS_CRATE.get(), Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Items.LIGHT_BLUE_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.BLUE_STAINED_GLASS_CRATE.get(), Blocks.BLUE_STAINED_GLASS_PANE, Items.BLUE_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.PURPLE_STAINED_GLASS_CRATE.get(), Blocks.PURPLE_STAINED_GLASS_PANE, Items.PURPLE_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.MAGENTA_STAINED_GLASS_CRATE.get(), Blocks.MAGENTA_STAINED_GLASS_PANE, Items.MAGENTA_DYE, recipeOutput);
        simpleGlassCrate(ModBlocks.PINK_STAINED_GLASS_CRATE.get(), Blocks.PINK_STAINED_GLASS_PANE, Items.PINK_DYE, recipeOutput);

         ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.OBSIDIAN_REINFORCEMENT_ITEM.get(), 8)
                 .requires(Items.OBSIDIAN)
                 .requires(ModItems.PLIERS_ITEM.get())
                 .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                 .save(recipeOutput);

         ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LAMP_SIMULATOR_ITEM.get(), 8)
                 .requires(Items.REDSTONE_LAMP)
                 .requires(ModItems.PLIERS_ITEM.get())
                 .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                 .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FIREPROOFING_ITEM.get(), 8)
                .requires(Items.MAGMA_CREAM)
                .requires(Items.IRON_INGOT)
                .requires(ModItems.PLIERS_ITEM.get())
                .unlockedBy("has_pliers", has(ModItems.PLIERS_ITEM.get()))
                .save(recipeOutput);
    }

    // Generate the simple wooden crate recipes
    private void simpleCrate(Block block, Item recipeMaterial, Item recipeMaterial2, String unlockedString, RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block, 1)
                .pattern("---")
                .pattern("# #")
                .pattern("---")
                .define('-', recipeMaterial)
                .define('#', recipeMaterial2)
                .unlockedBy(unlockedString, has(recipeMaterial))
                .save(output);
    }

    // Generate both glass crate recipes
    private void simpleGlassCrate(Block block, Block pane, Item dye, RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block, 1)
                .pattern("-##")
                .pattern("###")
                .pattern("#  ")
                .define('-', ModTags.Items.WOODEN_CRATES)
                .define('#', pane)
                .unlockedBy("has_wood_crate", has(ModTags.Items.WOODEN_CRATES))
                .save(output);

        if(dye != null) {
            Block glassCrateVariant;
            LinkedList<Block> glassCrateVariants = new LinkedList<>();
            for(int i = 0; i < ModBlocks.getGlassCrates().length; i++) {
                glassCrateVariant = ModBlocks.getGlassCrates(i);
                if(glassCrateVariant != block) {
                    glassCrateVariants.add(glassCrateVariant);
                }
            }

            Ingredient glassCrateVariantsIngredient = Ingredient.of(glassCrateVariants.toArray(new Block[0]));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block, 1)
                    .pattern("-# ")
                    .pattern("#  ")
                    .pattern("   ")
                    .define('-', glassCrateVariantsIngredient)
                    .define('#', dye)
                    .unlockedBy("has_glass_crate", has(ModTags.Items.GLASS_CRATES))
                    .save(output, ResourceLocation.fromNamespaceAndPath(CriticalCrates.MODID, "dye_"
                            + block.getDescriptionId().substring(block.getDescriptionId().indexOf("s.") + 2)));
        }
    }
}