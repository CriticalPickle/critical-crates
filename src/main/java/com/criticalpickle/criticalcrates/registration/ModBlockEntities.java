package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.entity.CrateBlockEntity;
import com.criticalpickle.criticalcrates.block.entity.GlassCrateBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CriticalCrates.MODID);

    public static final Supplier<BlockEntityType<CrateBlockEntity>> CRATE_BE = BLOCK_ENTITIES.register("crate_be",
            () -> new BlockEntityType<>(CrateBlockEntity::new, ModBlocks.getWoodCrates()));

    public static final Supplier<BlockEntityType<GlassCrateBlockEntity>> GLASS_CRATE_BE = BLOCK_ENTITIES.register("glass_crate_be",
            () -> new BlockEntityType<>(GlassCrateBlockEntity::new, ModBlocks.getGlassCrates()));

}
