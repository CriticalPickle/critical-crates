package com.criticalpickle.criticalcrates.util;

import net.minecraft.core.HolderLookup;

public class HolderUtils {
    private static HolderLookup.Provider holderLookup = null;

    public static void setHolderLookup(HolderLookup.Provider provider) {
        holderLookup = provider;
    }

    public static HolderLookup.Provider getHolderLookup() {
        return holderLookup;
    }

    public static boolean isHolderLookupValid() {
        return holderLookup != null;
    }
}
