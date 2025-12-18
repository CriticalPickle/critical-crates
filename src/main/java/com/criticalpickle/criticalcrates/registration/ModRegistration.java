package com.criticalpickle.criticalcrates.registration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class ModRegistration {
    // Helper function to initialize basic registries
    public static void init(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);
        ModTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        // EX: if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
        //  event.accept(ITEM);
        // }
    }
}
