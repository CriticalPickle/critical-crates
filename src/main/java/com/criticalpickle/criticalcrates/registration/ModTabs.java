package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CriticalCrates.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CRITICAL_CRATES_TAB = CREATIVE_MODE_TABS.register("critical_crates_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.criticalcrates"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(ModBlocks.OAK_CRATE.get()))
            .displayItems((parameters, output) -> {
                ItemStack crateStack = null;
                ItemStack resistantCrateStack = null;
                ItemStack lampCrateStack = null;
                ItemStack fireCrateStack = null;
                CompoundTag dataTag = new CompoundTag();

                for(int i = 0; i < ModBlocks.getCrates().length; i++){
                    crateStack = new ItemStack(ModBlocks.getCrates(i));
                    resistantCrateStack = new ItemStack(ModBlocks.getCrates(i));
                    lampCrateStack = new ItemStack(ModBlocks.getCrates(i));
                    fireCrateStack = new ItemStack(ModBlocks.getCrates(i));

                    dataTag.putBoolean("explosion_resistant", false);
                    dataTag.putBoolean("lamp_upgrade", false);
                    dataTag.putBoolean("fireproof", false);
                    crateStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                    output.accept(crateStack);

                    dataTag.putBoolean("explosion_resistant", true);
                    dataTag.putBoolean("lamp_upgrade", false);
                    dataTag.putBoolean("fireproof", false);
                    resistantCrateStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                    output.accept(resistantCrateStack);

                    dataTag.putBoolean("explosion_resistant", false);
                    dataTag.putBoolean("lamp_upgrade", true);
                    dataTag.putBoolean("fireproof", false);
                    lampCrateStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                    output.accept(lampCrateStack);

                    dataTag.putBoolean("explosion_resistant", false);
                    dataTag.putBoolean("lamp_upgrade", false);
                    dataTag.putBoolean("fireproof", true);
                    fireCrateStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                    fireCrateStack.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
                    output.accept(fireCrateStack);
                }

                output.accept(ModItems.PLIERS_ITEM.get());
                output.accept(ModItems.OBSIDIAN_REINFORCEMENT_ITEM.get());
                output.accept(ModItems.LAMP_SIMULATOR_ITEM.get());
                output.accept(ModItems.FIREPROOFING_ITEM.get());
            }).build());
}
