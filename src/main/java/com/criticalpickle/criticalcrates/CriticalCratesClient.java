package com.criticalpickle.criticalcrates;

import com.criticalpickle.criticalcrates.block.entity.renderer.GlassCrateBlockEntityRenderer;
import com.criticalpickle.criticalcrates.registration.ModBlockEntities;
import com.criticalpickle.criticalcrates.registration.ModMenuTypes;
import com.criticalpickle.criticalcrates.registration.ModRegistration;
import com.criticalpickle.criticalcrates.screen.CrateScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = CriticalCrates.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = CriticalCrates.MODID, value = Dist.CLIENT)
public class CriticalCratesClient {
    public CriticalCratesClient(ModContainer container) {
        // Create a config screen for this mod's configs built in to NeoForge "mods" tab.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        CriticalCrates.LOGGER.info("CriticalCrates is now being loaded on client!");
        ModRegistration.registerItemProperties();
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.CRATE_MENU.get(), CrateScreen::new);
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GLASS_CRATE_BE.get(), GlassCrateBlockEntityRenderer::new);
    }
}
