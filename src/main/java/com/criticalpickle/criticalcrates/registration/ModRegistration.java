package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
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

    // Register components for items as json predicates
    public static void registerItemProperties() {
        ResourceLocation resistanceResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "resistant");
        ResourceLocation lampResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "lamp");
        ResourceLocation fireResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "fire");
        ResourceLocation slimyResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "slimy");

        if(resistanceResLoc != null && lampResLoc != null && fireResLoc != null && slimyResLoc != null) {
            for(int i = 0; i < ModItems.getCrateItems().length; i++){
                ItemProperties.register(ModItems.getCrateItems(i), resistanceResLoc, (stack, world, entity, seed) -> {
                    if (stack.get(DataComponents.CUSTOM_DATA) != null && stack.get(DataComponents.CUSTOM_DATA).contains("explosion_resistant")) {
                        boolean resistant = stack.get(DataComponents.CUSTOM_DATA).copyTag().getBoolean("explosion_resistant");
                        return resistant ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });

                ItemProperties.register(ModItems.getCrateItems(i), lampResLoc, (stack, world, entity, seed) -> {
                    if (stack.get(DataComponents.CUSTOM_DATA) != null && stack.get(DataComponents.CUSTOM_DATA).contains("lamp_upgrade")) {
                        boolean lamp = stack.get(DataComponents.CUSTOM_DATA).copyTag().getBoolean("lamp_upgrade");
                        return lamp ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });

                ItemProperties.register(ModItems.getCrateItems(i), fireResLoc, (stack, world, entity, seed) -> {
                    if (stack.get(DataComponents.CUSTOM_DATA) != null && stack.get(DataComponents.CUSTOM_DATA).contains("fireproof")) {
                        boolean fire = stack.get(DataComponents.CUSTOM_DATA).copyTag().getBoolean("fireproof");
                        return fire ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });

                ItemProperties.register(ModItems.getCrateItems(i), slimyResLoc, (stack, world, entity, seed) -> {
                    if (stack.get(DataComponents.CUSTOM_DATA) != null && stack.get(DataComponents.CUSTOM_DATA).contains("slimy")) {
                        boolean slimy = stack.get(DataComponents.CUSTOM_DATA).copyTag().getBoolean("slimy");
                        return slimy ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });
            }
        }
    }
}
