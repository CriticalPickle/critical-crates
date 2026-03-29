package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.CrateBlock;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
import com.criticalpickle.criticalcrates.block.OreCrateBlock;
import com.criticalpickle.criticalcrates.item.CrateBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CriticalCrates.MODID);

    public static final DeferredBlock<Block> OAK_CRATE = registerCrateBlockWithItem("oak_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "oak_crate")))));
    public static final DeferredBlock<Block> IRON_OAK_CRATE = registerCrateBlockWithItem("iron_oak_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_oak_crate"))), "iron", 54));
    public static final DeferredBlock<Block> SPRUCE_CRATE = registerCrateBlockWithItem("spruce_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "spruce_crate")))));
    public static final DeferredBlock<Block> IRON_SPRUCE_CRATE = registerCrateBlockWithItem("iron_spruce_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_spruce_crate"))), "iron", 54));
    public static final DeferredBlock<Block> BIRCH_CRATE = registerCrateBlockWithItem("birch_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "birch_crate")))));
    public static final DeferredBlock<Block> IRON_BIRCH_CRATE = registerCrateBlockWithItem("iron_birch_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_birch_crate"))), "iron", 54));
    public static final DeferredBlock<Block> JUNGLE_CRATE = registerCrateBlockWithItem("jungle_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "jungle_crate")))));
    public static final DeferredBlock<Block> IRON_JUNGLE_CRATE = registerCrateBlockWithItem("iron_jungle_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_jungle_crate"))), "iron", 54));
    public static final DeferredBlock<Block> ACACIA_CRATE = registerCrateBlockWithItem("acacia_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "acacia_crate")))));
    public static final DeferredBlock<Block> IRON_ACACIA_CRATE = registerCrateBlockWithItem("iron_acacia_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_acacia_crate"))), "iron", 54));
    public static final DeferredBlock<Block> DARK_OAK_CRATE = registerCrateBlockWithItem("dark_oak_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "dark_oak_crate")))));
    public static final DeferredBlock<Block> IRON_DARK_OAK_CRATE = registerCrateBlockWithItem("iron_dark_oak_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_dark_oak_crate"))), "iron", 54));
    public static final DeferredBlock<Block> MANGROVE_CRATE = registerCrateBlockWithItem("mangrove_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "mangrove_crate")))));
    public static final DeferredBlock<Block> IRON_MANGROVE_CRATE = registerCrateBlockWithItem("iron_mangrove_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_mangrove_crate"))), "iron", 54));
    public static final DeferredBlock<Block> CHERRY_CRATE = registerCrateBlockWithItem("cherry_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "cherry_crate")))));
    public static final DeferredBlock<Block> IRON_CHERRY_CRATE = registerCrateBlockWithItem("iron_cherry_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_cherry_crate"))), "iron", 54));
    public static final DeferredBlock<Block> BAMBOO_CRATE = registerCrateBlockWithItem("bamboo_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "bamboo_crate")))));
    public static final DeferredBlock<Block> IRON_BAMBOO_CRATE = registerCrateBlockWithItem("iron_bamboo_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_bamboo_crate"))), "iron", 54));
    public static final DeferredBlock<Block> CRIMSON_CRATE = registerCrateBlockWithItem("crimson_crate", () -> new CrateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "crimson_crate")))));
    public static final DeferredBlock<Block> IRON_CRIMSON_CRATE = registerCrateBlockWithItem("iron_crimson_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_crimson_crate"))), "iron", 54));
    public static final DeferredBlock<Block> WARPED_CRATE = registerCrateBlockWithItem("warped_crate", () -> new CrateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "warped_crate")))));
    public static final DeferredBlock<Block> IRON_WARPED_CRATE = registerCrateBlockWithItem("iron_warped_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_warped_crate"))), "iron", 54));

    public static final DeferredBlock<Block> GLASS_CRATE = registerCrateBlockWithItem("glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "glass_crate")))));
    public static final DeferredBlock<Block> IRON_GLASS_CRATE = registerCrateBlockWithItem("iron_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> WHITE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("white_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "white_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_WHITE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_white_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_white_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> LIGHT_GRAY_STAINED_GLASS_CRATE = registerCrateBlockWithItem("light_gray_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "light_gray_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_LIGHT_GRAY_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_light_gray_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_light_gray_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> GRAY_STAINED_GLASS_CRATE = registerCrateBlockWithItem("gray_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "gray_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_GRAY_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_gray_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_gray_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> BLACK_STAINED_GLASS_CRATE = registerCrateBlockWithItem("black_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "black_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_BLACK_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_black_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_black_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> BROWN_STAINED_GLASS_CRATE = registerCrateBlockWithItem("brown_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "brown_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_BROWN_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_brown_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_brown_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> RED_STAINED_GLASS_CRATE = registerCrateBlockWithItem("red_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "red_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_RED_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_red_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_red_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> ORANGE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("orange_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "orange_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_ORANGE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_orange_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_orange_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> YELLOW_STAINED_GLASS_CRATE = registerCrateBlockWithItem("yellow_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "yellow_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_YELLOW_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_yellow_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_yellow_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> LIME_STAINED_GLASS_CRATE = registerCrateBlockWithItem("lime_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "lime_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_LIME_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_lime_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_lime_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> GREEN_STAINED_GLASS_CRATE = registerCrateBlockWithItem("green_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "green_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_GREEN_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_green_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_green_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> CYAN_STAINED_GLASS_CRATE = registerCrateBlockWithItem("cyan_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "cyan_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_CYAN_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_cyan_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_cyan_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> LIGHT_BLUE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("light_blue_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "light_blue_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_LIGHT_BLUE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_light_blue_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_light_blue_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> BLUE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("blue_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "blue_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_BLUE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_blue_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_blue_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> PURPLE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("purple_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "purple_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_PURPLE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_purple_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_purple_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> MAGENTA_STAINED_GLASS_CRATE = registerCrateBlockWithItem("magenta_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "magenta_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_MAGENTA_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_magenta_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_magenta_stained_glass_crate"))), "iron", 54));
    public static final DeferredBlock<Block> PINK_STAINED_GLASS_CRATE = registerCrateBlockWithItem("pink_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "pink_stained_glass_crate")))));
    public static final DeferredBlock<Block> IRON_PINK_STAINED_GLASS_CRATE = registerCrateBlockWithItem("iron_pink_stained_glass_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_STAINED_GLASS).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_pink_stained_glass_crate"))), "iron", 54));

    public static final DeferredBlock<Block> IRON_CRATE = registerCrateBlockWithItem("iron_crate", () -> new OreCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "iron_crate"))), "iron", 54));

    private static  <T extends Block> DeferredBlock<T> registerCrateBlockWithItem(String name, Supplier<T> block) {
        DeferredBlock<T> REGISTERED_BLOCK = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new CrateBlockItem(REGISTERED_BLOCK.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("criticalcrates", name)))));
        return REGISTERED_BLOCK;
    }

    /// Get all crate blocks
    public static Block[] getCrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get)
                .filter(block -> block instanceof CrateBlock).toArray(Block[]::new);
    }

    /// Get a crate block at an index dependent on the time of registration
    public static Block getCrates(int index) {
        Block[] temp = getCrates();
        return temp[index];
    }

    /// Get all wooden crate blocks
    public static Block[] getWoodCrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get)
                .filter(block ->
                        block instanceof CrateBlock && !(block instanceof GlassCrateBlock)
                                && (!(block instanceof OreCrateBlock oreCrate) || (!oreCrate.getDescriptionId().contains("glass")
                                && !oreCrate.getDescriptionId().contains("iron_crate")))
                ).toArray(Block[]::new);
    }

    /// Get a wooden crate block at an index dependent on the time of registration
    public static Block getWoodCrates(int index) {
        Block[] temp = getWoodCrates();
        return temp[index];
    }

    /// Get all wooden crate block entity blocks
    public static Block[] getWoodBECrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get)
                .filter(block ->
                        block instanceof CrateBlock && !(block instanceof GlassCrateBlock || block instanceof OreCrateBlock)
                ).toArray(Block[]::new);
    }

    /// Get all glass crate blocks
    public static Block[] getGlassCrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get)
                .filter(block ->
                        block instanceof GlassCrateBlock || (block instanceof OreCrateBlock oreCrate
                                && oreCrate.getDescriptionId().contains("glass"))
                ).toArray(Block[]::new);
    }

    /// Get a glass crate block at an index dependent on the time of registration
    public static Block getGlassCrates(int index) {
        Block[] temp = getGlassCrates();
        return temp[index];
    }

    /// Get all glass crate block entity blocks
    public static Block[] getGlassBECrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get)
                .filter(block -> block instanceof GlassCrateBlock).toArray(Block[]::new);
    }

    /// Get all ore crate blocks
    public static Block[] getOreCrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get)
                .filter(block ->
                        block instanceof OreCrateBlock && block.equals(ModBlocks.IRON_CRATE.get())
                ).toArray(Block[]::new);
    }

    /// Get an ore crate block at an index dependent on the time of registration
    public static Block getOreCrates(int index) {
        Block[] temp = getOreCrates();
        return temp[index];
    }

    /// Get all ore upgraded crate blocks
    public static Block[] getOreUpgradedCrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get)
                .filter(block ->
                        block instanceof OreCrateBlock && !block.equals(ModBlocks.IRON_CRATE.get())
                ).toArray(Block[]::new);
    }

    /// Get an ore upgraded crate block at an index dependent on the time of registration
    public static Block getOreUpgradedCrates(int index) {
        Block[] temp = getOreUpgradedCrates();
        return temp[index];
    }

    /// Get all ore block entity crate blocks
    public static Block[] getOreBECrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get)
                .filter(block -> block instanceof OreCrateBlock).toArray(Block[]::new);
    }
}
