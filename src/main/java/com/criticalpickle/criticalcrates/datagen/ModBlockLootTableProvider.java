package com.criticalpickle.criticalcrates.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    private static HolderLookup.Provider lookupProvider;

    protected ModBlockLootTableProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, HolderLookup.Provider registries) {
        super(explosionResistant, enabledFeatures, registries);
        lookupProvider = registries;
    }


    @Override
    protected void generate() {
        // Generate loot tables for all crates including enchantment requirements (for glass crates) and components
//        for(int i = 0; i < ModBlocks.getCrates().length; i++){
//            if(ModBlocks.getCrates(i) instanceof GlassCrateBlock) {
//                add(ModBlocks.getCrates(i), LootTable.lootTable()
//                        .withPool(LootPool.lootPool()
//                                .setRolls(ConstantValue.exactly(1))
//                                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasComponents(
//                                        DataComponentPredicate.builder().expect(
//                                                DataComponents.ENCHANTMENTS, EnchantmentUtils.enchantmentKeyToItemEnchantments(Enchantments.SILK_TOUCH, 1, lookupProvider)
//                                        ).build())))
//                                .add(LootItem.lootTableItem(ModBlocks.getCrates(i))
//                                        .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
//                                                .include(DataComponents.CUSTOM_DATA).include(DataComponents.FIRE_RESISTANT))
//                                )
//                        )
//                );
//            }
//            else {
//                add(ModBlocks.getCrates(i), LootTable.lootTable()
//                        .withPool(LootPool.lootPool()
//                                .setRolls(ConstantValue.exactly(1))
//                                .add(LootItem.lootTableItem(ModBlocks.getCrates(i))
//                                        .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
//                                                .include(DataComponents.CUSTOM_DATA).include(DataComponents.FIRE_RESISTANT))
//                                )
//                        )
//                );
//            }
//        }
    }

//    @Override
//    protected Iterable<Block> getKnownBlocks() {
//        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
//    }
}
