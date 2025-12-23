package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.util.EnchantmentUtils;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    private static HolderLookup.Provider lookupProvider;

    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        lookupProvider = registries;
    }

    @Override
    protected void generate() {
        // Generate loot tables for all crates including enchantment requirements (for glass crates) and components
        for(int i = 0; i < ModBlocks.getCrates().length; i++){
            if(ModBlocks.getCrates(i) instanceof GlassCrateBlock) {
                add(ModBlocks.getCrates(i), LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().withComponents(
                                        DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(
                                                DataComponents.ENCHANTMENTS, EnchantmentUtils.enchantmentKeyToItemEnchantments(Enchantments.SILK_TOUCH, 1, lookupProvider)
                                        )).build())))
                                .add(LootItem.lootTableItem(ModBlocks.getCrates(i))
                                        .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContext.BlockEntityTarget.BLOCK_ENTITY.contextParam())
                                                .include(DataComponents.CUSTOM_DATA).include(DataComponents.DAMAGE_RESISTANT))
                                )
                        )
                );
            }
            else {
                add(ModBlocks.getCrates(i), LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ModBlocks.getCrates(i))
                                        .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContext.BlockEntityTarget.BLOCK_ENTITY.contextParam())
                                                .include(DataComponents.CUSTOM_DATA).include(DataComponents.DAMAGE_RESISTANT))
                                )
                        )
                );
            }
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}