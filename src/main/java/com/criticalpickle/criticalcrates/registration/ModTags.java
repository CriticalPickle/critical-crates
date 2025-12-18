package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> CRATES = createTag("crates");
        public static final TagKey<Block> WOODEN_CRATES = createTag("wooden_crates");
        public static final TagKey<Block> GLASS_CRATES = createTag("glass_crates");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(Identifier.fromNamespaceAndPath(CriticalCrates.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CRATES = createTag("crates");
        public static final TagKey<Item> WOODEN_CRATES = createTag("wooden_crates");
        public static final TagKey<Item> GLASS_CRATES = createTag("glass_crates");
        public static final TagKey<Item> CRATE_UPGRADES = createTag("crate_upgrades");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(CriticalCrates.MODID, name));
        }
    }
}
