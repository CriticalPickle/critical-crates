package com.criticalpickle.criticalcrates.datagen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.criticalpickle.criticalcrates.block.CrateBlock;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
import com.criticalpickle.criticalcrates.block.OreCrateBlock;
import com.criticalpickle.criticalcrates.block.SoilCrateBlock;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.util.IDUtils;
import com.criticalpickle.criticalcrates.util.ItemModelPropertyUtils;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.block.dispatch.multipart.CombinedCondition;
import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, CriticalCrates.MODID);
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        for(int i = 0; i < ModItems.getCrateItems().length; i++) {
            if(!ModItems.getCrateItems(i).getDescriptionId().contains("iron")
                    || ModItems.getCrateItems(i).getDescriptionId().contains("iron_crate")) {
                blockItemWithOverrides(ModItems.getCrateItems(i), itemModels);
            }
            // Else generated automatically with block
        }

        for(int i = 0; i < ModBlocks.getCrates().length; i++) {
            if(ModBlocks.getCrates(i) instanceof OreCrateBlock oreCrateBlock && !oreCrateBlock.isCrateType("ore")) {
                axisCrateBlock(oreCrateBlock, blockModels);
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

    /// Gets the relating block type texture folder.
    private String getBlockType(Block block) {
        if (block instanceof GlassCrateBlock) {
            return "glass";
        }
        else if (block instanceof OreCrateBlock oreCrateBlock) {
            return !oreCrateBlock.getCrateType().equals("ore") ? "ore_upgraded" : "ore";
        }
        else if (block instanceof SoilCrateBlock) {
            return "soil";
        }
        return "wood";
    }

    /// Constructs the model location for a block based on its name and state suffix.
    private Identifier makeModelLoc(Block block, String suffix) {
        String blockName = IDUtils.getItemID(block.asItem());
        return Identifier.fromNamespaceAndPath(CriticalCrates.MODID,
                "block/" + blockName + suffix);
    }

    /// Constructs the texture location for a block based on its type, name, and state suffix.
    private Identifier makeTextureLoc(Block block, String suffix) {
        String blockName = IDUtils.getItemID(block.asItem()), blockType = getBlockType(block);
        return Identifier.fromNamespaceAndPath(CriticalCrates.MODID,
                "block/" + blockType + "/" + blockName + suffix);
    }

    private static Condition constructCombinedCond(List<ModelCondition<?>> modelConditions) {
        List<Condition> conditions = new ArrayList<>();
        for (ModelCondition<?> modelCondition : modelConditions) {
            conditions.add(modelCondition.toCondition());
        }
        return new CombinedCondition(CombinedCondition.Operation.AND, conditions);
    }

    private BlockModelDefinitionGenerator constructGenerator(Block block, Variant base, boolean onlyAxis, boolean addMoist) {
        BlockModelDefinitionGenerator finalGenerator;
        MultiVariantGenerator multiVariantGenerator = MultiVariantGenerator.dispatch(block, BlockModelGenerators.variant(base))
                .with(PropertyDispatch.modify(CrateBlock.AXIS)
                        .select(Direction.Axis.Y, BlockModelGenerators.NOP)
                        .select(Direction.Axis.Z, BlockModelGenerators.X_ROT_90)
                        .select(Direction.Axis.X, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90))
                );

        finalGenerator = multiVariantGenerator;
        if (onlyAxis && addMoist) {
            multiVariantGenerator = multiVariantGenerator.with(PropertyDispatch.modify(SoilCrateBlock.MOISTURE)
                    .generate(moist -> moist == 7 ?
                            VariantMutator.MODEL.withValue(makeModelLoc(block, "_moist"))
                            : BlockModelGenerators.NOP)
            );
            finalGenerator = multiVariantGenerator;
        }
        else if (block instanceof SoilCrateBlock) {
            record SoilPart(String suffix, ModelCondition<?>... conditions) {}
            final List<SoilPart> soilParts = List.of(
                    new SoilPart("",
                            ModelCondition.of(CrateBlock.EXPLOSION_RESIST, false),
                            ModelCondition.of(CrateBlock.LAMP_UPGRADE, false),
                            ModelCondition.of(CrateBlock.FIREPROOF, false),
                            ModelCondition.of(CrateBlock.SLIMY, false)
                    ),
                    new SoilPart("_resistant",
                            ModelCondition.of(CrateBlock.EXPLOSION_RESIST, true)
                    ),
                    new SoilPart("_lamp",
                            ModelCondition.of(CrateBlock.LAMP_UPGRADE, true),
                            ModelCondition.of(CrateBlock.LIT, false)
                    ),
                    new SoilPart("_lamp_on",
                            ModelCondition.of(CrateBlock.LAMP_UPGRADE, true),
                            ModelCondition.of(CrateBlock.LIT, true)
                    ),
                    new SoilPart("_fireproof",
                            ModelCondition.of(CrateBlock.FIREPROOF, true)
                    ),
                    new SoilPart("_slimy",
                            ModelCondition.of(CrateBlock.SLIMY, true)
                    )
            );

            MultiPartGenerator multiPart = MultiPartGenerator.multiPart(block);
            for (SoilPart soilPart : soilParts) {
                for (boolean moist : new boolean[]{false, true}) {
                    final ModelCondition<Integer> moistCondition = moist ?
                            ModelCondition.of(SoilCrateBlock.MOISTURE, 7)
                            : ModelCondition.of(SoilCrateBlock.MOISTURE, 0, 1, 2, 3, 4, 5, 6);
                    final String suffix = moist ? "_moist" + soilPart.suffix() : soilPart.suffix();
                    final Variant variant = new Variant(makeModelLoc(block, suffix));

                    for (Direction.Axis axis : Direction.Axis.values()) {
                        List<ModelCondition<?>> allConditions = new ArrayList<>(Arrays.stream(soilPart.conditions)
                                .toList());
                        allConditions.add(moistCondition);
                        allConditions.add(ModelCondition.of(CrateBlock.AXIS, axis));

                        final Variant axisVariant = switch (axis) {
                            case Y -> variant;
                            case Z -> variant.with(BlockModelGenerators.X_ROT_90);
                            case X -> variant.with(BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90));
                        };

                        multiPart = multiPart.with(constructCombinedCond(allConditions),
                                BlockModelGenerators.variant(axisVariant));
                    }
                }
            }
            finalGenerator = multiPart;
        }
        else if (!onlyAxis) {
            multiVariantGenerator = multiVariantGenerator
                    .with(PropertyDispatch.modify(CrateBlock.EXPLOSION_RESIST)
                            .select(true, VariantMutator.MODEL.withValue(makeModelLoc(block, "_resistant")))
                            .select(false, BlockModelGenerators.NOP)
                    )
                    .with(PropertyDispatch.modify(CrateBlock.LAMP_UPGRADE, CrateBlock.LIT)
                            .select(true, false, VariantMutator.MODEL.withValue(makeModelLoc(block, "_lamp")))
                            .select(true, true, VariantMutator.MODEL.withValue(makeModelLoc(block, "_lamp_on")))
                            .select(false, false, BlockModelGenerators.NOP)
                            .select(false, true, BlockModelGenerators.NOP)
                    )
                    .with(PropertyDispatch.modify(CrateBlock.FIREPROOF)
                            .select(true, VariantMutator.MODEL.withValue(makeModelLoc(block, "_fireproof")))
                            .select(false, BlockModelGenerators.NOP)
                    )
                    .with(PropertyDispatch.modify(CrateBlock.SLIMY)
                            .select(true, VariantMutator.MODEL.withValue(makeModelLoc(block, "_slimy")))
                            .select(false, BlockModelGenerators.NOP)
                    );
            finalGenerator = multiVariantGenerator;
        }

        return finalGenerator;
    }

    /// Generate only axis property for crate
    private void axisCrateBlock(Block block, BlockModelGenerators blockModels) {
        String blockName = IDUtils.getItemID(block.asItem());
        Variant base = new Variant(makeModelLoc(block, ""));

        createTemplate(blockName, "", block.getDescriptionId().contains(CriticalCrates.MODID)).create(
                block,
                new TextureMapping()
                        .put(TextureSlot.SIDE, new Material(makeTextureLoc(block, "")))
                        .put(TextureSlot.END, new Material(makeTextureLoc(block, "_top"))),
                blockModels.modelOutput
        );

        BlockModelDefinitionGenerator finalGenerator;
        if(block instanceof OreCrateBlock oreCrateBlock && !oreCrateBlock.isCrateType("soil")) {
            finalGenerator = constructGenerator(block, base, true, false);
        }
        else {
            final ModelTemplate MOIST_TEMPLATE = createTemplate(blockName, "_moist", block.getDescriptionId().contains(CriticalCrates.MODID));
            MOIST_TEMPLATE.create(
                    block,
                    new TextureMapping()
                            .put(TextureSlot.SIDE, new Material(makeTextureLoc(block, "_moist")))
                            .put(TextureSlot.END, new Material(makeTextureLoc(block, "_moist_top"))),
                    blockModels.modelOutput
            );
            finalGenerator = constructGenerator(block, base, true, true);
        }
        blockModels.blockStateOutput.accept(finalGenerator);
    }

    /// Create a new template based on passed in values
    private static ModelTemplate createTemplate(String blockName, String key, boolean crate) {
        if(!crate) {
            throw new IllegalArgumentException("Block of " + blockName + " must be a crate from CriticalCrates!");
        }
        else {
            return new ModelTemplate(
                    Optional.of(ModelLocationUtils.decorateItemModelLocation(CriticalCrates.MODID + ":" + blockName + key)),
                    Optional.of(key), TextureSlot.END, TextureSlot.SIDE
            ).extend().parent(ModelTemplates.CUBE_COLUMN.model.get()).build();
        }
    }

    /// Generate the model templates for crates
    private static Map<String, ModelTemplate> generateTemplates(Block block, String blockName) {
        Map<String, ModelTemplate> templates = new HashMap<>();
        List<String> keys = List.of(
                "",
                "_resistant",
                "_lamp",
                "_lamp_on",
                "_fireproof",
                "_slimy"
        );

        for (String key : keys) {
            final ModelTemplate base_template = createTemplate(blockName, key, block.getDescriptionId().contains(CriticalCrates.MODID));
            templates.put(key, base_template);

            if (block instanceof SoilCrateBlock) {
                final ModelTemplate moist_template = createTemplate(blockName, "_moist" + key, block.getDescriptionId().contains(CriticalCrates.MODID));
                templates.put("_moist" + key, moist_template);
            }
        }

        return templates;
    }

    /// Generate block models and states for crate
    private void axisWithOtherPropertiesCrateBlock(Block block, BlockModelGenerators blockModels) {
        String blockName = IDUtils.getItemID(block.asItem());
        Variant base = new Variant(makeModelLoc(block, ""));

        Map<String, ModelTemplate> templates = generateTemplates(block, blockName);

        for(Map.Entry<String, ModelTemplate> template : templates.entrySet()) {
            template.getValue().create(
                    block,
                    new TextureMapping()
                            .put(TextureSlot.SIDE,
                                    new Material(makeTextureLoc(block, template.getKey())))
                            .put(TextureSlot.END,
                                    new Material(makeTextureLoc(block, template.getKey() + "_top"))),
                    blockModels.modelOutput
            );
        }

        BlockModelDefinitionGenerator generator = constructGenerator(block, base, false, false);
        blockModels.blockStateOutput.accept(generator);
    }

    /// Generate crate item models that are dependent on custom data components
    private void blockItemWithOverrides(Item item, ItemModelGenerators itemModels) {
        String itemName = IDUtils.getItemID(item);

        itemModels.itemModelOutput.accept(item, new SelectItemModel.Unbaked(
                Optional.empty(),
                new SelectItemModel.UnbakedSwitch<>(
                        new ItemModelPropertyUtils.CrateDataValue(),
                        List.of(
                                new SelectItemModel.SwitchCase<>(
                                        List.of("resistant"),
                                        new CuboidItemModelWrapper.Unbaked(
                                                Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + itemName + "_resistant"),
                                                Optional.empty(),
                                                Collections.emptyList()
                                        )
                                ),
                                new SelectItemModel.SwitchCase<>(
                                        List.of("lamp"),
                                        new CuboidItemModelWrapper.Unbaked(
                                                Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + itemName + "_lamp"),
                                                Optional.empty(),
                                                Collections.emptyList()
                                        )
                                ),
                                new SelectItemModel.SwitchCase<>(
                                        List.of("fireproof"),
                                        new CuboidItemModelWrapper.Unbaked(
                                                Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + itemName + "_fireproof"),
                                                Optional.empty(),
                                                Collections.emptyList()
                                        )
                                ),
                                new SelectItemModel.SwitchCase<>(
                                        List.of("slimy"),
                                        new CuboidItemModelWrapper.Unbaked(
                                                Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + itemName + "_slimy"),
                                                Optional.empty(),
                                                Collections.emptyList()
                                        )
                                )
                        )
                ),
                Optional.of(
                        new CuboidItemModelWrapper.Unbaked(
                                Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "block/" + itemName),
                                Optional.empty(),
                                Collections.emptyList()
                        )
                )
        ));
    }
}
