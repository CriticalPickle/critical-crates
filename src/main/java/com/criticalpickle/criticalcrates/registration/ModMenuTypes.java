package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.screen.CrateMenu;
import com.criticalpickle.criticalcrates.screen.LargeCrateMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, CriticalCrates.MODID);

    public static final Supplier<MenuType<CrateMenu>> CRATE_MENU = MENUS.register("crate_menu", () -> IMenuTypeExtension.create(CrateMenu::new));
    public static final Supplier<MenuType<LargeCrateMenu>> LARGE_CRATE_MENU = MENUS.register("large_crate_menu", () -> IMenuTypeExtension.create(LargeCrateMenu::new));
}
