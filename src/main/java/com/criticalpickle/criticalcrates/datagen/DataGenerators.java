package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = CriticalCrates.MODID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput packOutput = generator.getPackOutput();
        final RegistrySetBuilder builder = new RegistrySetBuilder()
                .add(Registries.LOOT_TABLE, new LootTableProvider(
                                Set.of(),
                                List.of(new LootTableProvider.SubProviderEntry(
                                        ModBlockLootTableProvider::new, LootContextParamSets.BLOCK
                                ))
                        )
                )
                .add(ModRecipeProvider.create());

        event.createBlockAndItemTags(
                ModBlockTagProvider::new,
                (output, lookup, _)
                        -> new ModItemTagProvider(output, lookup)
        );

        event.createReloadableRegistryObjects(builder);

        event.createProvider(ModModelProvider::new);
    }
}
