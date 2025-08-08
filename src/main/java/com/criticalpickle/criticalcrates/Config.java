package com.criticalpickle.criticalcrates;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ADDONS_REMOVABLE = BUILDER
            .comment(" Whether to allow removal the addons to crates.\n Default is 'addonsRemovable = true'.")
            .define("addonsRemovable", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
