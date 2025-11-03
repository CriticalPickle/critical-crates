package com.criticalpickle.criticalcrates;

import com.criticalpickle.criticalcrates.registration.ModBlockEntities;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.registration.ModRegistration;
import com.criticalpickle.criticalcrates.util.HolderUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// Coded by CriticalPickle

@Mod(CriticalCrates.MODID)
public class CriticalCrates {
    public static final String MODID = "criticalcrates";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CriticalCrates(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onRegisterCapabilities);

        ModRegistration.init(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        // Register the items to existing creative tabs
        modEventBus.addListener(ModRegistration::addCreative);

        // Register NeoForge's ModConfigSpec so that FML can create and load the config file
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Thanks for downloading Critical Crates! Setting up...");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Critical Crates is now being loaded on the server!");
        HolderLookup.Provider holderLookUpProvider = event.getServer().registryAccess();
        HolderUtils.setHolderLookup(holderLookUpProvider);
    }

    // Crate external item handler registration
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.CRATE_BE.get(), (blockEntity, side) -> blockEntity.getInventorySide(side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.GLASS_CRATE_BE.get(), (blockEntity, side) -> blockEntity.getInventorySide(side));
    }

    // Add sound event to pliers item breaking in crafting table
    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack stack = null;
        CustomData data = null;

        for(int i = 0; i < event.getInventory().getContainerSize(); i++) {
            stack = event.getInventory().getItem(i);
            data = stack.get(DataComponents.CUSTOM_DATA);

            if(!stack.isEmpty() && stack.getItem() == ModItems.PLIERS_ITEM.get() && data != null && data.copyTag().getBoolean("broken")) {
                event.getEntity().level().playSound(null, event.getEntity().blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1f, 1f);
                break;
            }
        }
    }
}
