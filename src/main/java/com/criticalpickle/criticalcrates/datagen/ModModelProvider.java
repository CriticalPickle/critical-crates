package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, CriticalCrates.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for(int i = 0; i < ModBlocks.getCrates().length; i++) {
            axisWithOtherPropertiesCrateBlock(ModBlocks.getCrates(i), blockModels);
        }

//        for(int i = 0; i < ModItems.getCrateItems().length; i++) {
//            blockItemWithOverrides(ModItems.getCrateItems(i));
//        }

        itemModels.generateFlatItem(ModItems.PLIERS_ITEM.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.OBSIDIAN_REINFORCEMENT_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.LAMP_SIMULATOR_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FIREPROOFING_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SOAP.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    private void axisWithOtherPropertiesCrateBlock(Block block, BlockModelGenerators blockModels) {
        blockModels.createRotatableColumn(block);
//        PropertyDispatch dispatch = PropertyDispatch.modify(CrateBlock.AXIS).select()
//        VariantBlockStateBuilder builder = getVariantBuilder(block);
//        String blockName = block.getDescriptionId().substring(block.getDescriptionId().indexOf("s.") + 2);
//        ConfiguredModel model;

//        for(Direction.Axis axis : Direction.Axis.values()) {
//            // Loop through extra properties (add more loops below, but make following code nested)
//            String path = null;
//            for(boolean resistance : new boolean[]{false, true}) {
//                for(boolean lamp : new boolean[]{false, true}) {
//                    for(boolean lit : new boolean[]{false, true}) {
//                        for(boolean fire : new boolean[]{false, true}) {
//                            if(resistance) {
//                                path = blockName + "_resistant";
//                            }
//                            else if(lamp) {
//                                path = lit ? blockName + "_lamp_on" : blockName + "_lamp";
//                            }
//                            else if(fire) {
//                                path = blockName + "_fireproof";
//                            }
//                            else {
//                                path = blockName;
//                            }
//
//                            if (axis == Direction.Axis.X || axis == Direction.Axis.Z) {
//                                path += "_horizontal";
//                            }
//
//                            int x = axis == Direction.Axis.Y ? 0 : 90;
//                            int y = axis == Direction.Axis.X ? 90 : 0;
//
//                            if (path.contains("horizontal")) {
//                                String name = path.substring(0, path.indexOf("_horizontal"));
//                                if(block instanceof GlassCrateBlock) {
//                                    model = ConfiguredModel.builder()
//                                            .modelFile(models().cubeColumnHorizontal(path, modLoc("block/glass/" + name), modLoc("block/glass/" + name + "_top")).renderType("translucent"))
//                                            .rotationX(x)
//                                            .rotationY(y)
//                                            .build()[0];
//                                }
//                                else {
//                                    model = ConfiguredModel.builder()
//                                            .modelFile(models().cubeColumnHorizontal(path, modLoc("block/wood/" + name), modLoc("block/wood/" + name + "_top")))
//                                            .rotationX(x)
//                                            .rotationY(y)
//                                            .build()[0];
//                                }
//                            } else {
//                                if(block instanceof GlassCrateBlock) {
//                                    model = ConfiguredModel.builder()
//                                            .modelFile(models().cubeColumn(path, modLoc("block/glass/" + path), modLoc("block/glass/" + path + "_top")).renderType("translucent"))
//                                            .rotationX(x)
//                                            .rotationY(y)
//                                            .build()[0];
//                                }
//                                else {
//                                    model = ConfiguredModel.builder()
//                                            .modelFile(models().cubeColumn(path, modLoc("block/wood/" + path), modLoc("block/wood/" + path + "_top")))
//                                            .rotationX(x)
//                                            .rotationY(y)
//                                            .build()[0];
//                                }
//                            }
//
//                            builder.partialState()
//                                    .with(CrateBlock.AXIS, axis)
//                                    .with(CrateBlock.EXPLOSION_RESIST, resistance)
//                                    .with(CrateBlock.LAMP_UPGRADE, lamp)
//                                    .with(CrateBlock.LIT, lit)
//                                    .with(CrateBlock.FIREPROOF, fire)
//                                    .addModels(model);
//                        }
//                    }
//                }
//            }
//        }
    }

//    private void blockItemWithOverrides(Item item) {
//        String itemName = item.getDescriptionId().substring(item.getDescriptionId().indexOf("s.") + 2);
//        ItemModelBuilder builder = withExistingParent(itemName, modLoc("block/" + itemName));
//        ResourceLocation resistanceResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "resistant");
//        ResourceLocation lampResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "lamp");
//        ResourceLocation fireResLoc = ResourceLocation.tryBuild(CriticalCrates.MODID, "fire");
//
//        // Generate other variations
//        withExistingParent(itemName + "_resistant", modLoc("block/" + itemName + "_resistant"));
//        withExistingParent(itemName + "_lamp", modLoc("block/" + itemName + "_lamp"));
//        withExistingParent(itemName + "_fireproof", modLoc("block/" + itemName + "_fireproof"));
//
//        // Generate base variation with appropriate references to overrides
//        builder.override()
//                .predicate(resistanceResLoc, 1)
//                .model(getExistingFile(modLoc("block/" + itemName + "_resistant")))
//                .end()
//                .override()
//                .predicate(lampResLoc, 1)
//                .model(getExistingFile(modLoc("block/" + itemName + "_lamp")))
//                .end()
//                .override()
//                .predicate(fireResLoc, 1)
//                .model(getExistingFile(modLoc("block/" + itemName + "_fireproof")))
//                .end();
//    }
}
