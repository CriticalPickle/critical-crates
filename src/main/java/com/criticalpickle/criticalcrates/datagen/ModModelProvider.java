package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.CrateBlock;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
import com.criticalpickle.criticalcrates.block.OreCrateBlock;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.util.IDUtils;
import com.criticalpickle.criticalcrates.util.ItemModelPropertyUtils;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.*;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, CriticalCrates.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for(int i = 0; i < ModItems.getCrateItems().length; i++) {
            if(!ModItems.getCrateItems(i).getDescriptionId().contains("iron")
                    || ModItems.getCrateItems(i).getDescriptionId().contains("iron_crate")) {
                blockItemWithOverrides(ModItems.getCrateItems(i), itemModels);
            }
            // Else generated automatically with block
        }

        for(int i = 0; i < ModBlocks.getCrates().length; i++) {
            if(!ModBlocks.getCrates(i).equals(ModBlocks.IRON_CRATE.get()) && ModBlocks.getCrates(i) instanceof OreCrateBlock) {
                axisCrateBlock(ModBlocks.getCrates(i), blockModels);
            }
            else {
                axisWithOtherPropertiesCrateBlock(ModBlocks.getCrates(i), blockModels);
            }
        }

        itemModels.generateFlatItem(ModItems.PLIERS_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.OBSIDIAN_REINFORCEMENT_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.LAMP_SIMULATOR_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FIREPROOFING_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SLIMY_FRAMING_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.IRON_SUPPORTS_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SOAP.get(), ModelTemplates.FLAT_ITEM);

        for(int j = 0; j < ModItems.getCrateFoundations().length; j++) {
            itemModels.generateFlatItem(ModItems.getCrateFoundations(j), ModelTemplates.FLAT_ITEM);
        }
    }

    /// Generate only axis property for crate
    private void axisCrateBlock(Block block, BlockModelGenerators blockModels) {
        String blockName = IDUtils.getItemID(block.asItem()), blockType = "ore_upgraded";
        Identifier baseLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName);
        Variant base = new Variant(baseLoc);

        final ModelTemplate BASE_TEMPLATE = createTemplate(blockName, "", blockType, block.getDescriptionId().contains(CriticalCrates.MODID));
        Identifier textureLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockType + "/" + blockName);
        Identifier textureLocTop = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockType + "/" + blockName + "_top");
        BASE_TEMPLATE.create(
                block,
                new TextureMapping()
                        .put(TextureSlot.SIDE, textureLoc)
                        .put(TextureSlot.END, textureLocTop),
                blockModels.modelOutput
        );

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block, BlockModelGenerators.variant(base))
                        .with(PropertyDispatch.modify(CrateBlock.AXIS)
                                .select(Direction.Axis.Y, BlockModelGenerators.NOP)
                                .select(Direction.Axis.Z, BlockModelGenerators.X_ROT_90)
                                .select(Direction.Axis.X, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90))
                        )
        );
    }

    /// Generate block models and states for crate
    private void axisWithOtherPropertiesCrateBlock(Block block, BlockModelGenerators blockModels) {
        String blockName = IDUtils.getItemID(block.asItem()), blockType
                = block instanceof GlassCrateBlock ? "glass" : block instanceof OreCrateBlock ? "ore" : "wood";
        Identifier baseLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName),
                resistantLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName + "_resistant"),
                lampLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName + "_lamp"),
                lampOnLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName + "_lamp_on"),
                fireLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName + "_fireproof"),
                slimyLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockName + "_slimy");
        Variant base = new Variant(baseLoc);

        Map<String, ModelTemplate> templates = generateTemplates(block, blockName, blockType);

        for(Map.Entry<String, ModelTemplate> template : templates.entrySet()) {
            Identifier textureLoc = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockType + "/" + blockName + template.getKey());
            Identifier textureLocTop = Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + blockType + "/" + blockName + template.getKey() + "_top");
            template.getValue().create(
                    block,
                    new TextureMapping()
                            .put(TextureSlot.SIDE, textureLoc)
                            .put(TextureSlot.END, textureLocTop),
                    blockModels.modelOutput
            );
        }

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
                        .with(PropertyDispatch.modify(CrateBlock.SLIMY)
                                .select(true, VariantMutator.MODEL.withValue(slimyLoc))
                                .select(false, BlockModelGenerators.NOP)
                        )
        );
    }

    /// Generate the model templates for crates
    private static Map<String, ModelTemplate> generateTemplates(Block block, String blockName, String blockType) {
        Map<String, ModelTemplate> templates = new HashMap<>();

        String key = "";
        final ModelTemplate BASE_TEMPLATE = createTemplate(blockName, key, blockType, block.getDescriptionId().contains(CriticalCrates.MODID));
        templates.put(key, BASE_TEMPLATE);

        key = "_resistant";
        final ModelTemplate RESISTANT_TEMPLATE = createTemplate(blockName, key, blockType, block.getDescriptionId().contains(CriticalCrates.MODID));
        templates.put(key, RESISTANT_TEMPLATE);

        key = "_lamp";
        final ModelTemplate LAMP_TEMPLATE = createTemplate(blockName, key, blockType, block.getDescriptionId().contains(CriticalCrates.MODID));
        templates.put(key, LAMP_TEMPLATE);

        key = "_lamp_on";
        final ModelTemplate LAMP_ON_TEMPLATE = createTemplate(blockName, key, blockType, block.getDescriptionId().contains(CriticalCrates.MODID));
        templates.put(key, LAMP_ON_TEMPLATE);

        key = "_fireproof";
        final ModelTemplate FIREPROOF_TEMPLATE = createTemplate(blockName, key, blockType, block.getDescriptionId().contains(CriticalCrates.MODID));
        templates.put(key, FIREPROOF_TEMPLATE);

        key = "_slimy";
        final ModelTemplate SLIMY_TEMPLATE = createTemplate(blockName, key, blockType, block.getDescriptionId().contains(CriticalCrates.MODID));
        templates.put(key, SLIMY_TEMPLATE);

        return templates;
    }

    /// Create a new template based on passed in values
    private static ModelTemplate createTemplate(String blockName, String key, String blockType, boolean crate) {
        if(!crate) {
            throw new IllegalArgumentException("Block of " + blockName + " must be a crate from CriticalCrates!");
        }
        else if(blockType.equals("glass") || blockName.contains("glass")) {
            return new ModelTemplate(
                    Optional.of(ModelLocationUtils.decorateItemModelLocation(CriticalCrates.MODID + ":" + blockName + key)),
                    Optional.of(key), TextureSlot.END, TextureSlot.SIDE
            ).extend().parent(ModelTemplates.CUBE_COLUMN.model.get()).renderType("translucent").build();
        }
        else {
            return new ModelTemplate(
                    Optional.of(ModelLocationUtils.decorateItemModelLocation(CriticalCrates.MODID + ":" + blockName + key)),
                    Optional.of(key), TextureSlot.END, TextureSlot.SIDE
            ).extend().parent(ModelTemplates.CUBE_COLUMN.model.get()).build();
        }
    }

    /// Generate crate item models that are dependent on custom data components
    private void blockItemWithOverrides(Item item, ItemModelGenerators itemModels) {
        String itemName = IDUtils.getItemID(item);

        itemModels.itemModelOutput.accept(item, new SelectItemModel.Unbaked(
                new SelectItemModel.UnbakedSwitch(
                        new ItemModelPropertyUtils.CrateDataValue(),
                        List.of(
                                new SelectItemModel.SwitchCase(
                                        List.of("resistant"),
                                        new BlockModelWrapper.Unbaked(
                                                Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + itemName + "_resistant"),
                                                Collections.emptyList()
                                        )
                                ),
                                new SelectItemModel.SwitchCase(
                                        List.of("lamp"),
                                        new BlockModelWrapper.Unbaked(
                                                Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + itemName + "_lamp"),
                                                Collections.emptyList()
                                        )
                                ),
                                new SelectItemModel.SwitchCase(
                                        List.of("fireproof"),
                                        new BlockModelWrapper.Unbaked(
                                                Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + itemName + "_fireproof"),
                                                Collections.emptyList()
                                        )
                                ),
                                new SelectItemModel.SwitchCase(
                                        List.of("slimy"),
                                        new BlockModelWrapper.Unbaked(
                                                Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + itemName + "_slimy"),
                                                Collections.emptyList()
                                        )
                                )
                        )
                ),
                Optional.of(
                        new BlockModelWrapper.Unbaked(
                                Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + itemName),
                                Collections.emptyList()
                        )
                )
        ));
    }
}
