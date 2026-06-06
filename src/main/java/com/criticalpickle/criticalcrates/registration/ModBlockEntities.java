package com.criticalpickle.criticalcrates.registration;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.entity.CrateBlockEntity;
import com.criticalpickle.criticalcrates.block.entity.GlassCrateBlockEntity;
import com.criticalpickle.criticalcrates.block.entity.OreCrateBlockEntity;
import com.criticalpickle.criticalcrates.block.entity.SoilCrateBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CriticalCrates.MODID);

    public static final Supplier<BlockEntityType<CrateBlockEntity>> CRATE_BE = BLOCK_ENTITIES.register("crate_be",
            () -> BlockEntityType.Builder.of(CrateBlockEntity::new, ModBlocks.getWoodBECrates()).build(null));

    public static final Supplier<BlockEntityType<GlassCrateBlockEntity>> GLASS_CRATE_BE = BLOCK_ENTITIES.register("glass_crate_be",
            () -> BlockEntityType.Builder.of(GlassCrateBlockEntity::new, ModBlocks.getGlassBECrates()).build(null));

    public static final Supplier<BlockEntityType<OreCrateBlockEntity>> ORE_CRATE_BE = BLOCK_ENTITIES.register("ore_crate_be",
            () -> BlockEntityType.Builder.of(OreCrateBlockEntity::new, ModBlocks.getOreBECrates()).build(null));

    public static final Supplier<BlockEntityType<SoilCrateBlockEntity>> SOIL_CRATE_BE = BLOCK_ENTITIES.register("soil_crate_be",
            () -> BlockEntityType.Builder.of(SoilCrateBlockEntity::new, ModBlocks.getSoilBECrates()).build(null));

}
