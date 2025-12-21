package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.CrateBlock;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.util.IDUtils;
import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

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

        itemModels.generateFlatItem(ModItems.PLIERS_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.OBSIDIAN_REINFORCEMENT_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.LAMP_SIMULATOR_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FIREPROOFING_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SOAP.get(), ModelTemplates.FLAT_ITEM);
    }

    private void axisWithOtherPropertiesCrateBlock(Block block, BlockModelGenerators blockModels) {
        printSelects();
        String blockName = IDUtils.getItemID(block.asItem());

        Identifier baseLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName),
                resistantLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName + "_resistant"),
                lampLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName + "_lamp"),
                lampOnLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName + "_lamp_on"),
                fireLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName + "_fireproof");
        Variant base = new Variant(baseLoc);
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, BlockModelGenerators.variant(base))
                        .with(PropertyDispatch.modify(CrateBlock.AXIS)
                                .select(Direction.Axis.Y, BlockModelGenerators.NOP)
                                .select(Direction.Axis.Z, BlockModelGenerators.X_ROT_90)
                                .select(Direction.Axis.X, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90))
                        )
                        .with(PropertyDispatch.modify(CrateBlock.EXPLOSION_RESIST)
                                .select(true, VariantMutator.MODEL.withValue(resistantLoc))
                                .select(false, BlockModelGenerators.NOP)
                        )
                        .with(PropertyDispatch.modify(CrateBlock.LAMP_UPGRADE, CrateBlock.LIT)
                                .select(true, false, VariantMutator.MODEL.withValue(lampLoc))
                                .select(true, true, VariantMutator.MODEL.withValue(lampOnLoc))
                                .select(false, false, BlockModelGenerators.NOP)
                                .select(false, true, BlockModelGenerators.NOP)
                        )
                        .with(PropertyDispatch.modify(CrateBlock.FIREPROOF)
                                .select(true, VariantMutator.MODEL.withValue(fireLoc))
                                .select(false, BlockModelGenerators.NOP)
                        )

        );
    }

    private static String computePathName(Block block, Direction.Axis axis, boolean resistance, boolean lamp, boolean lit, boolean fire) {
        String path = null, blockName = IDUtils.getItemID(block.asItem());

        if(resistance) {
            path = blockName + "_resistant";
        }
        else if(lamp) {
            path = lit ? blockName + "_lamp_on" : blockName + "_lamp";
        }
        else if(fire) {
            path = blockName + "_fireproof";
        }
        else {
            path = blockName;
        }

        if (axis == Direction.Axis.X || axis == Direction.Axis.Z) {
            path += "_horizontal";
        }

        return path;
    }

    private static Quadrant quadrantFromDegrees(int degrees) {
        return switch (degrees % 360) {
            case 0 -> Quadrant.R0;
            case 90 -> Quadrant.R90;
            case 180 -> Quadrant.R180;
            case 270 -> Quadrant.R270;
            default -> throw new IllegalArgumentException("Invalid rotation: " + degrees);
        };
    }

//    private void blockItemWithOverrides(Item item) {
//        String itemName = item.getDescriptionId().substring(item.getDescriptionId().indexOf("s.") + 2);
//        ItemModelBuilder builder = withExistingParent(itemName, modLoc("block/" + itemName));
//        Identifier resistanceResLoc = Identifier.tryBuild(CriticalCrates.MODID, "resistant");
//        Identifier lampResLoc = Identifier.tryBuild(CriticalCrates.MODID, "lamp");
//        Identifier fireResLoc = Identifier.tryBuild(CriticalCrates.MODID, "fire");
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

    private static void printSelects() {
        for (Direction.Axis axis : Direction.Axis.values()) {
            for (boolean res : new boolean[]{false,true}) {
                for (boolean lamp : new boolean[]{false,true}) {
                    for (boolean lit : new boolean[]{false,true}) {
                        for (boolean fire : new boolean[]{false,true}) {

                            boolean rot;
                            char directionSymbol;
                            if(axis == Direction.Axis.X) {
                                rot = true;
                                directionSymbol = 'X';
                            }
                            else if(axis == Direction.Axis.Z) {
                                rot = true;
                                directionSymbol = 'Z';
                            }
                            else {
                                rot = false;
                                directionSymbol = 'Y';
                            }

                            if(rot && onlyOne(res, lamp, fire)) {
                                if(res) {
                                    System.out.printf(".select(Direction.Axis.%c, %b, %b, %b, %b, %s)%n",
                                            directionSymbol, res, lamp, lit, fire, "VariantMutator.MODEL.withValue(resistantLoc).then(VariantMutator.X_ROT.withValue(Quadrant.R90))");
                                }
                                else if(lamp) {
                                    if(lit) {
                                        System.out.printf(".select(Direction.Axis.%s, %b, %b, %b, %b, %s)%n",
                                                directionSymbol, res, lamp, lit, fire, "VariantMutator.MODEL.withValue(lampOnLoc).then(VariantMutator.X_ROT.withValue(Quadrant.R90))");
                                    }
                                    else {
                                        System.out.printf(".select(Direction.Axis.%s, %b, %b, %b, %b, %s)%n",
                                                directionSymbol, res, lamp, lit, fire, "VariantMutator.MODEL.withValue(lampLoc).then(VariantMutator.X_ROT.withValue(Quadrant.R90))");
                                    }
                                }
                                else {
                                    System.out.printf(".select(Direction.Axis.%s, %b, %b, %b, %b, %s)%n",
                                            directionSymbol, res, lamp, lit, fire, "VariantMutator.MODEL.withValue(fireLoc).then(VariantMutator.X_ROT.withValue(Quadrant.R90))");
                                }
                            }
                            else {
                                System.out.printf(".select(Direction.Axis.%s, %b, %b, %b, %b, %s)%n",
                                        directionSymbol, res, lamp, lit, fire, "BlockModelGenerators.NOP");
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean onlyOne(boolean res, boolean lamp, boolean fire) {
        long trueCount = Stream.of(res, lamp, fire).filter(Boolean::booleanValue).count();
        return trueCount == 1;
    }
}
