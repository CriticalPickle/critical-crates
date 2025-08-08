package com.criticalpickle.criticalcrates.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;

public class EnchantmentUtils {
    public static Holder<Enchantment> getEnchantmentHolder(ResourceKey<Enchantment> resourceKey) {
        return HolderUtils.getHolderLookup().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(resourceKey);
    }

    public static Holder<Enchantment> getEnchantmentHolder(ResourceKey<Enchantment> resourceKey, HolderLookup.Provider provider) {
        return provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(resourceKey);
    }

    // Convert when holder is given in onServerSetup
    public static ItemEnchantments enchantmentKeyToItemEnchantments(@NotNull ResourceKey<Enchantment> resourceKey, int enchantLevel) {
        Holder<Enchantment> enchantmentHolder = getEnchantmentHolder(resourceKey);
        ItemEnchantments.Mutable mutableItemEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        mutableItemEnchantments.set(enchantmentHolder, enchantLevel);
        return mutableItemEnchantments.toImmutable();
    }

    // Convert when no client is given by the provided holder
    public static ItemEnchantments enchantmentKeyToItemEnchantments(@NotNull ResourceKey<Enchantment> resourceKey, int enchantLevel, @NotNull HolderLookup.Provider provider) {
        Holder<Enchantment> enchantmentHolder = getEnchantmentHolder(resourceKey, provider);
        ItemEnchantments.Mutable mutableItemEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        mutableItemEnchantments.set(enchantmentHolder, enchantLevel);
        return mutableItemEnchantments.toImmutable();
    }
}
