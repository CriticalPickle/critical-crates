package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.CrateBlock;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
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
    public static final DeferredBlock<Block> SPRUCE_CRATE = registerCrateBlockWithItem("spruce_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "spruce_crate")))));
    public static final DeferredBlock<Block> BIRCH_CRATE = registerCrateBlockWithItem("birch_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "birch_crate")))));
    public static final DeferredBlock<Block> JUNGLE_CRATE = registerCrateBlockWithItem("jungle_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "jungle_crate")))));
    public static final DeferredBlock<Block> ACACIA_CRATE = registerCrateBlockWithItem("acacia_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "acacia_crate")))));
    public static final DeferredBlock<Block> DARK_OAK_CRATE = registerCrateBlockWithItem("dark_oak_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "dark_oak_crate")))));
    public static final DeferredBlock<Block> MANGROVE_CRATE = registerCrateBlockWithItem("mangrove_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "mangrove_crate")))));
    public static final DeferredBlock<Block> CHERRY_CRATE = registerCrateBlockWithItem("cherry_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "cherry_crate")))));
    public static final DeferredBlock<Block> BAMBOO_CRATE = registerCrateBlockWithItem("bamboo_crate", () -> new CrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "bamboo_crate")))));
    public static final DeferredBlock<Block> CRIMSON_CRATE = registerCrateBlockWithItem("crimson_crate", () -> new CrateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "crimson_crate")))));
    public static final DeferredBlock<Block> WARPED_CRATE = registerCrateBlockWithItem("warped_crate", () -> new CrateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("criticalcrates", "warped_crate")))));

    public static final DeferredBlock<Block> GLASS_CRATE = registerCrateBlockWithItem("glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<Block> WHITE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("white_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_STAINED_GLASS)));
    public static final DeferredBlock<Block> LIGHT_GRAY_STAINED_GLASS_CRATE = registerCrateBlockWithItem("light_gray_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_STAINED_GLASS)));
    public static final DeferredBlock<Block> GRAY_STAINED_GLASS_CRATE = registerCrateBlockWithItem("gray_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_STAINED_GLASS)));
    public static final DeferredBlock<Block> BLACK_STAINED_GLASS_CRATE = registerCrateBlockWithItem("black_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_STAINED_GLASS)));
    public static final DeferredBlock<Block> BROWN_STAINED_GLASS_CRATE = registerCrateBlockWithItem("brown_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_STAINED_GLASS)));
    public static final DeferredBlock<Block> RED_STAINED_GLASS_CRATE = registerCrateBlockWithItem("red_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_STAINED_GLASS)));
    public static final DeferredBlock<Block> ORANGE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("orange_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_STAINED_GLASS)));
    public static final DeferredBlock<Block> YELLOW_STAINED_GLASS_CRATE = registerCrateBlockWithItem("yellow_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_STAINED_GLASS)));
    public static final DeferredBlock<Block> LIME_STAINED_GLASS_CRATE = registerCrateBlockWithItem("lime_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_STAINED_GLASS)));
    public static final DeferredBlock<Block> GREEN_STAINED_GLASS_CRATE = registerCrateBlockWithItem("green_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_STAINED_GLASS)));
    public static final DeferredBlock<Block> CYAN_STAINED_GLASS_CRATE = registerCrateBlockWithItem("cyan_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_STAINED_GLASS)));
    public static final DeferredBlock<Block> LIGHT_BLUE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("light_blue_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_STAINED_GLASS)));
    public static final DeferredBlock<Block> BLUE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("blue_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_STAINED_GLASS)));
    public static final DeferredBlock<Block> PURPLE_STAINED_GLASS_CRATE = registerCrateBlockWithItem("purple_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_STAINED_GLASS)));
    public static final DeferredBlock<Block> MAGENTA_STAINED_GLASS_CRATE = registerCrateBlockWithItem("magenta_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_STAINED_GLASS)));
    public static final DeferredBlock<Block> PINK_STAINED_GLASS_CRATE = registerCrateBlockWithItem("pink_stained_glass_crate", () -> new GlassCrateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_STAINED_GLASS)));

    private static  <T extends Block> DeferredBlock<T> registerCrateBlockWithItem(String name, Supplier<T> block) {
        DeferredBlock<T> REGISTERED_BLOCK = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new CrateBlockItem(REGISTERED_BLOCK.get(), new Item.Properties()));
        return REGISTERED_BLOCK;
    }

    // Get all crate blocks
    public static Block[] getCrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof CrateBlock).toArray(Block[]::new);
    }

    // Get a crate block at an index dependent on the time of registration
    public static Block getCrates(int index) {
        Block[] temp = BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof CrateBlock).toArray(Block[]::new);
        return temp[index];
    }

    // Get all wooden crate blocks
    public static Block[] getWoodCrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> (block instanceof CrateBlock && !(block instanceof GlassCrateBlock))).toArray(Block[]::new);
    }

    // Get a wooden crate block at an index dependent on the time of registration
    public static Block getWoodCrates(int index) {
        Block[] temp = BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> (block instanceof CrateBlock && !(block instanceof GlassCrateBlock))).toArray(Block[]::new);
        return temp[index];
    }

    // Get all glass crate blocks
    public static Block[] getGlassCrates() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof GlassCrateBlock).toArray(Block[]::new);
    }

    // Get a glass crate block at an index dependent on the time of registration
    public static Block getGlassCrates(int index) {
        Block[] temp = BLOCKS.getEntries().stream().map(DeferredHolder::get).filter(block -> block instanceof GlassCrateBlock).toArray(Block[]::new);
        return temp[index];
    }
}
