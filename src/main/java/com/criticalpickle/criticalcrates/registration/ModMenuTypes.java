package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.screen.CrateMenu;
import com.criticalpickle.criticalcrates.screen.LargeCrateMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, CriticalCrates.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CrateMenu>> CRATE_MENU = MENUS.register("crate_menu", () -> IMenuTypeExtension.create(CrateMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<LargeCrateMenu>> LARGE_CRATE_MENU = MENUS.register("large_crate_menu", () -> IMenuTypeExtension.create(LargeCrateMenu::new));
}
