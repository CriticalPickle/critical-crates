package com.criticalpickle.criticalcrates;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ADDONS_REMOVABLE = BUILDER
            .comment(" Whether to allow removal of addons from crates.\n Default is 'addonsRemovable = true'.")
            .define("addonsRemovable", true);

    public static final ModConfigSpec.BooleanValue GLASS_CRATES_DYEABLE = BUILDER
            .comment(" ---\n Whether to allow universal dyeing of glass crates.\n Default is 'universalDyeing = true'.")
            .define("universalDyeing", true);

    public static final ModConfigSpec.BooleanValue STAINED_COLOR_REMOVABLE = BUILDER
            .comment(" ---\n Whether to allow the removal of color from stained crates.\n Default is 'colorRemovable = true'.")
            .define("colorRemovable", true);

    public static final ModConfigSpec.BooleanValue GLASS_CHANGE_GLASS_CRATE = BUILDER
            .comment(" ---\n Whether to allow glass crates to be changed with glass like wooden ones.\n Default is 'glassChangeGlassCrate = false'.")
            .define("glassChangeGlassCrate", false);

    static final ModConfigSpec SPEC = BUILDER.build();
}
