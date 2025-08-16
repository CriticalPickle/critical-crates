package com.criticalpickle.criticalcrates;

import com.criticalpickle.criticalcrates.item.CrateBlockItem;
import com.criticalpickle.criticalcrates.network.RecipeBookClickHandler;
import com.criticalpickle.criticalcrates.registration.ModBlockEntities;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.registration.ModRegistration;
import com.criticalpickle.criticalcrates.util.HolderUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
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
        modEventBus.addListener(this::onRegisterPayloads);

        ModRegistration.init(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        // Register the items to existing creative tabs
        modEventBus.addListener(ModRegistration::addCreative);

        // Register NeoForge's ModConfigSpec so that FML can create and load the config file
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Thanks for downloading CriticalCrates! Setting up...");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("CriticalCrates is now being loaded on server!");
        HolderLookup.Provider holderLookUpProvider = event.getServer().registryAccess();
        HolderUtils.setHolderLookup(holderLookUpProvider);
    }

    // Crate external item handler registration
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.CRATE_BE.get(), (blockEntity, side) -> blockEntity.getInventorySide(side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.GLASS_CRATE_BE.get(), (blockEntity, side) -> blockEntity.getInventorySide(side));
    }

    // Crafting recipe book payload
    public void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playBidirectional(RecipeBookClickHandler.RecipeBookClickPayload.TYPE, RecipeBookClickHandler.RecipeBookClickPayload.STREAM_CODEC, (payload, context) -> {
            if(context.player() instanceof ServerPlayer serverPlayer) {
                RecipeBookClickHandler.handle(payload, serverPlayer);
            }
        });
    }

    // Ensure no "ghost" crafting item spill
    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        if(event.getLevel().isClientSide()) return;
        if(!(event.getEntity() instanceof ItemEntity itemEntity)) return;
        if(!(itemEntity.getItem().getItem() instanceof CrateBlockItem)) return;
        ItemStack stack = itemEntity.getItem();
        if(!stack.isEmpty() && stack.get(DataComponents.CUSTOM_DATA) != null
                && (stack.get(DataComponents.CUSTOM_DATA).contains("crafting_item")
                || stack.get(DataComponents.CUSTOM_DATA).contains("player_crafting"))) {
           event.getLevel().getServer().execute(() -> {
                itemEntity.remove(Entity.RemovalReason.DISCARDED);
            });
        }
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
