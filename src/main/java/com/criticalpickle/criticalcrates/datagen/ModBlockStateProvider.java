package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.CrateBlock;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CriticalCrates.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for(int i = 0; i < ModBlocks.getCrates().length; i++) {
            axisWithOtherPropertiesCrateBlock(ModBlocks.getCrates(i));
        }
    }

    private void axisWithOtherPropertiesCrateBlock(Block block) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        String blockName = block.getDescriptionId().substring(block.getDescriptionId().indexOf("s.") + 2);
        ConfiguredModel model;

        for(Direction.Axis axis : Direction.Axis.values()) {
            // Loop through extra properties (add more loops below, but make following code nested)
            String path = null;
            for(boolean resistance : new boolean[]{false, true}) {
                for(boolean lamp : new boolean[]{false, true}) {
                    for(boolean lit : new boolean[]{false, true}) {
                        for(boolean fire : new boolean[]{false, true}) {
                            for(boolean slimy : new boolean[]{false, true}) {
                                if(resistance) {
                                    path = blockName + "_resistant";
                                }
                                else if(lamp) {
                                    path = lit ? blockName + "_lamp_on" : blockName + "_lamp";
                                }
                                else if(fire) {
                                    path = blockName + "_fireproof";
                                }
                                else if(slimy) {
                                    path = blockName + "_slimy";
                                }
                                else {
                                    path = blockName;
                                }

                                if (axis == Direction.Axis.X || axis == Direction.Axis.Z) {
                                    path += "_horizontal";
                                }

                                int x = axis == Direction.Axis.Y ? 0 : 90;
                                int y = axis == Direction.Axis.X ? 90 : 0;

                                if (path.contains("horizontal")) {
                                    String name = path.substring(0, path.indexOf("_horizontal"));
                                    if (block instanceof GlassCrateBlock) {
                                        model = ConfiguredModel.builder()
                                                .modelFile(models().cubeColumnHorizontal(path, modLoc("block/glass/" + name), modLoc("block/glass/" + name + "_top")).renderType("translucent"))
                                                .rotationX(x)
                                                .rotationY(y)
                                                .build()[0];
                                    } else {
                                        model = ConfiguredModel.builder()
                                                .modelFile(models().cubeColumnHorizontal(path, modLoc("block/wood/" + name), modLoc("block/wood/" + name + "_top")))
                                                .rotationX(x)
                                                .rotationY(y)
                                                .build()[0];
                                    }
                                } else {
                                    if (block instanceof GlassCrateBlock) {
                                        model = ConfiguredModel.builder()
                                                .modelFile(models().cubeColumn(path, modLoc("block/glass/" + path), modLoc("block/glass/" + path + "_top")).renderType("translucent"))
                                                .rotationX(x)
                                                .rotationY(y)
                                                .build()[0];
                                    } else {
                                        model = ConfiguredModel.builder()
                                                .modelFile(models().cubeColumn(path, modLoc("block/wood/" + path), modLoc("block/wood/" + path + "_top")))
                                                .rotationX(x)
                                                .rotationY(y)
                                                .build()[0];
                                    }
                                }

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
