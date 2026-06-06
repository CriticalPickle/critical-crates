package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.CrateBlock;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
import com.criticalpickle.criticalcrates.block.OreCrateBlock;
import com.criticalpickle.criticalcrates.block.SoilCrateBlock;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CriticalCrates.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for(int i = 0; i < ModBlocks.getCrates().length; i++) {
            if(ModBlocks.getCrates(i) instanceof OreCrateBlock oreCrateBlock && !oreCrateBlock.isCrateType("ore")) {
                oreUpgradedCrateBlock(oreCrateBlock);
            }
            else {
                allPropertiesCrateBlock(ModBlocks.getCrates(i));
            }
        }
    }

    private ConfiguredModel makeModel(int xAxis, int yAxis, String dir, String jsonPath, boolean translucent) {
        boolean isHorizontal = jsonPath.endsWith("_horizontal");

        String textureName = isHorizontal ? jsonPath.substring(0, jsonPath.indexOf("_horizontal")) : jsonPath;

        if (isHorizontal) {
            return translucent ?
                    ConfiguredModel.builder()
                            .modelFile(models().cubeColumnHorizontal(
                                    jsonPath,
                                    modLoc("block/" + dir + "/" + textureName),
                                    modLoc("block/" + dir + "/" + textureName + "_top")
                            ).renderType("translucent"))
                            .rotationX(xAxis)
                            .rotationY(yAxis)
                            .build()[0]
                    : ConfiguredModel.builder()
                    .modelFile(models().cubeColumnHorizontal(
                            jsonPath,
                            modLoc("block/" + dir + "/" + textureName),
                            modLoc("block/" + dir + "/" + textureName + "_top")
                    ))
                    .rotationX(xAxis)
                    .rotationY(yAxis)
                    .build()[0];
        }
        else {
            return translucent ?
                    ConfiguredModel.builder()
                            .modelFile(models().cubeColumn(
                                    jsonPath,
                                    modLoc("block/" + dir + "/" + jsonPath),
                                    modLoc("block/" + dir + "/" + jsonPath + "_top")
                            ).renderType("translucent"))
                            .rotationX(xAxis)
                            .rotationY(yAxis)
                            .build()[0]
                    : ConfiguredModel.builder()
                    .modelFile(models().cubeColumn(
                            jsonPath,
                            modLoc("block/" + dir + "/" + jsonPath),
                            modLoc("block/" + dir + "/" + jsonPath + "_top")
                    ))
                    .rotationX(xAxis)
                    .rotationY(yAxis)
                    .build()[0];
        }
    }

    private void oreUpgradedCrateBlock(OreCrateBlock block) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        String blockName = block.getDescriptionId().substring(block.getDescriptionId().indexOf("s.") + 2), jsonPath;
        ConfiguredModel model;

        for(Direction.Axis axis : Direction.Axis.values()) {
            jsonPath = blockName;
            jsonPath += axis == Direction.Axis.X || axis == Direction.Axis.Z ? "_horizontal" : "";
            int x = axis == Direction.Axis.Y ? 0 : 90;
            int y = axis == Direction.Axis.X ? 90 : 0;

            if (block.isCrateType("soil")) {
                for (int moist : new int[]{0, 1, 2, 3, 4, 5, 6, 7}) {
                    if (moist == 7) {
                        String mainPath = jsonPath.substring(0, jsonPath.indexOf("crate") + 5) + "_moist";
                        jsonPath = mainPath + jsonPath.substring(jsonPath.indexOf("crate") + 5);
                    }

                    model = makeModel(x, y, "ore_upgraded", jsonPath, false);

                    builder.partialState()
                            .with(CrateBlock.AXIS, axis)
                            .with(SoilCrateBlock.MOISTURE, moist)
                            .addModels(model);
                }
            }
            else {
                model = makeModel(x, y, "ore_upgraded", jsonPath, blockName.contains("glass"));

                builder.partialState()
                        .with(CrateBlock.AXIS, axis)
                        .addModels(model);
            }
        }
    }

    private void allPropertiesCrateBlock(Block block) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        String blockName = block.getDescriptionId().substring(block.getDescriptionId().indexOf("s.") + 2), jsonPath;
        ConfiguredModel model;

        for(Direction.Axis axis : Direction.Axis.values()) {
            // Loop through extra properties (add more loops below, but make following code nested)
            for(boolean resistance : new boolean[]{false, true}) {
                for(boolean lamp : new boolean[]{false, true}) {
                    for(boolean lit : new boolean[]{false, true}) {
                        for(boolean fire : new boolean[]{false, true}) {
                            for(boolean slimy : new boolean[]{false, true}) {
                                if(resistance) {
                                    jsonPath = blockName + "_resistant";
                                }
                                else if(lamp) {
                                    jsonPath = lit ? blockName + "_lamp_on" : blockName + "_lamp";
                                }
                                else if(fire) {
                                    jsonPath = blockName + "_fireproof";
                                }
                                else if(slimy) {
                                    jsonPath = blockName + "_slimy";
                                }
                                else {
                                    jsonPath = blockName;
                                }

                                jsonPath += axis == Direction.Axis.X || axis == Direction.Axis.Z ? "_horizontal" : "";
                                int x = axis == Direction.Axis.Y ? 0 : 90;
                                int y = axis == Direction.Axis.X ? 90 : 0;

                                switch (block) {
                                    case GlassCrateBlock glassCrateBlock -> model = makeModel(x, y, "glass", jsonPath, true);
                                    case OreCrateBlock oreCrateBlock -> model = makeModel(x, y, "ore", jsonPath, false);
                                    case SoilCrateBlock soilCrateBlock -> {
                                        for (int moist : new int[]{0, 1, 2, 3, 4, 5, 6, 7}) {
                                            if (moist == 7) {
                                                String mainPath = jsonPath.substring(0, jsonPath.indexOf("crate") + 5) + "_moist";
                                                jsonPath = mainPath + jsonPath.substring(jsonPath.indexOf("crate") + 5);
                                            }

                                            model = makeModel(x, y, "soil", jsonPath, false);

                                            builder.partialState()
                                                    .with(CrateBlock.AXIS, axis)
                                                    .with(CrateBlock.EXPLOSION_RESIST, resistance)
                                                    .with(CrateBlock.LAMP_UPGRADE, lamp)
                                                    .with(CrateBlock.LIT, lit)
                                                    .with(CrateBlock.FIREPROOF, fire)
                                                    .with(CrateBlock.SLIMY, slimy)
                                                    .with(SoilCrateBlock.MOISTURE, moist)
                                                    .addModels(model);
                                        }
                                        model = null;
                                    }
                                    default -> model = makeModel(x, y, "wood", jsonPath, false);
                                }

                                if(model != null) {
                                    builder.partialState()
                                            .with(CrateBlock.AXIS, axis)
                                            .with(CrateBlock.EXPLOSION_RESIST, resistance)
                                            .with(CrateBlock.LAMP_UPGRADE, lamp)
                                            .with(CrateBlock.LIT, lit)
                                            .with(CrateBlock.FIREPROOF, fire)
                                            .with(CrateBlock.SLIMY, slimy)
                                            .addModels(model);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
