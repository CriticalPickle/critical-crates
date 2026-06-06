package com.criticalpickle.criticalcrates.util;

import com.criticalpickle.criticalcrates.Config;
import com.criticalpickle.criticalcrates.block.CrateBlock;
import com.criticalpickle.criticalcrates.block.GlassCrateBlock;
import com.criticalpickle.criticalcrates.block.OreCrateBlock;
import com.criticalpickle.criticalcrates.block.SoilCrateBlock;
import com.criticalpickle.criticalcrates.item.CrateBlockItem;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.registration.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.criticalpickle.criticalcrates.block.CrateBlock.*;

public class CrateUtil {
    public static BlockState rotatePillar(BlockState state, Rotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch (state.getValue(AXIS)) {
                    case X -> {
                        return state.setValue(AXIS, Direction.Axis.Z);
                    }
                    case Z -> {
                        return state.setValue(AXIS, Direction.Axis.X);
                    }
                    default -> {
                        return state;
                    }
                }
            default:
                return state;
        }
    }

    /// Set upgrades for all crate data
    public static void setDataTagUpgrades(CompoundTag dataTag, boolean resistant, boolean lamp,
                                           boolean fire, boolean slimy, BlockEntity blockEntity) {
        if(resistant) {
            dataTag.putBoolean("explosion_resistant", true);
            dataTag.putBoolean("lamp_upgrade", false);
            dataTag.putBoolean("fireproof", false);
            dataTag.putBoolean("slimy", false);
        }
        else if(lamp) {
            dataTag.putBoolean("explosion_resistant", false);
            dataTag.putBoolean("lamp_upgrade", true);
            dataTag.putBoolean("fireproof", false);
            dataTag.putBoolean("slimy", false);
        }
        else if(fire) {
            dataTag.putBoolean("explosion_resistant", false);
            dataTag.putBoolean("lamp_upgrade", false);
            dataTag.putBoolean("fireproof", true);
            dataTag.putBoolean("slimy", false);

            if(blockEntity != null) {
                DataComponentUtils.addBlockEntityDataComponents(blockEntity, dataTag, DataComponents.FIRE_RESISTANT);
            }
        }
        else if(slimy) {
            dataTag.putBoolean("explosion_resistant", false);
            dataTag.putBoolean("lamp_upgrade", false);
            dataTag.putBoolean("fireproof", false);
            dataTag.putBoolean("slimy", true);
        }
        else {
            dataTag.putBoolean("explosion_resistant", false);
            dataTag.putBoolean("lamp_upgrade", false);
            dataTag.putBoolean("fireproof", false);
            dataTag.putBoolean("slimy", false);
        }
    }

    /// Spawn removed crate upgrades dependent on state property values
    public static void spawnRemovedUpgrades(BlockState state, Level level, BlockPos pos) {
        if(state.getValue(EXPLOSION_RESIST)) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                    new ItemStack(ModItems.OBSIDIAN_REINFORCEMENT_ITEM.get())));
        }
        else if(state.getValue(LAMP_UPGRADE)) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                    new ItemStack(ModItems.LAMP_SIMULATOR_ITEM.get())));
        }
        else if(state.getValue(FIREPROOF)) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                    new ItemStack(ModItems.FIREPROOFING_ITEM.get())));
        }
        else if(state.getValue(SLIMY)) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                    new ItemStack(ModItems.SLIMY_FRAMING_ITEM.get())));
        }
    }

    /// Check if item is soap and block is cleanable
    public static boolean hasSoap(Item stackItem, BlockState blockState) {
        String itemName = stackItem.getName(new ItemStack(stackItem)).getString(), blockName = blockState.getBlock().getName().getString();
        return blockName.contains("Stained") && itemName.equals("Soap");
    }

    /// Get crate block given a string
    public static Block getCrateBlock(String crateName) {
        Item crateItem = ModItems.findCrateItemByID(crateName);
        Block crateBlock = null;

        if(crateItem instanceof CrateBlockItem crateBlockItem) {
            crateBlock = crateBlockItem.getBlock();
        }

        return crateBlock;
    }

    /// Helper function to handle the swaping of block state data
    public static void handleBlockSwap(Level level, BlockPos pos, BlockState state, Block crateBlock) {
        if(!state.hasProperty(SoilCrateBlock.MOISTURE) || !crateBlock.defaultBlockState().hasProperty(SoilCrateBlock.MOISTURE)) {
            level.setBlockAndUpdate(pos, crateBlock.defaultBlockState().setValue(AXIS, state.getValue(AXIS)).setValue(SWITCH, true)
                    .setValue(EXPLOSION_RESIST, state.getValue(EXPLOSION_RESIST)).setValue(LAMP_UPGRADE, state.getValue(LAMP_UPGRADE))
                    .setValue(LIT, state.getValue(LIT)).setValue(POWERED, state.getValue(POWERED)).setValue(FIREPROOF, state.getValue(FIREPROOF))
                    .setValue(SLIMY, state.getValue(SLIMY)));
        }
        else {
            level.setBlockAndUpdate(pos, crateBlock.defaultBlockState().setValue(AXIS, state.getValue(AXIS)).setValue(SWITCH, true)
                    .setValue(EXPLOSION_RESIST, state.getValue(EXPLOSION_RESIST)).setValue(LAMP_UPGRADE, state.getValue(LAMP_UPGRADE))
                    .setValue(LIT, state.getValue(LIT)).setValue(POWERED, state.getValue(POWERED)).setValue(FIREPROOF, state.getValue(FIREPROOF))
                    .setValue(SLIMY, state.getValue(SLIMY)).setValue(SoilCrateBlock.MOISTURE, state.getValue(SoilCrateBlock.MOISTURE)));
        }
    }

    /// Check if crate is currently able to be dyed with a specific dye item
    public static boolean canDye(BlockState state, Item item) {
        if((Config.STAINED_CRATES_DYEABLE.getAsBoolean() || Config.GLASS_CRATES_DYEABLE.getAsBoolean())
                && state.is(ModTags.Blocks.GLASS_CRATES)) {
            List<Item> dye = List.of(
                    Items.WHITE_DYE,
                    Items.LIGHT_GRAY_DYE,
                    Items.GRAY_DYE,
                    Items.BLACK_DYE,
                    Items.BROWN_DYE,
                    Items.RED_DYE,
                    Items.ORANGE_DYE,
                    Items.YELLOW_DYE,
                    Items.LIME_DYE,
                    Items.GREEN_DYE,
                    Items.CYAN_DYE,
                    Items.LIGHT_BLUE_DYE,
                    Items.BLUE_DYE,
                    Items.PURPLE_DYE,
                    Items.MAGENTA_DYE,
                    Items.PINK_DYE
            );

            if(dye.contains(item)) {
                String crateID = state.getBlock().getDescriptionId(),
                        crateType = crateID.substring(crateID.indexOf("s.") + 2, crateID.indexOf("_crate")),
                        itemID = item.getDescriptionId(),
                        itemColor = itemID.substring(itemID.indexOf("minecraft.") + 10, itemID.indexOf("_dye"));

                if(crateType.contains("iron") && !state.is(ModTags.Blocks.ORE_CRATES)) {
                    crateType = crateType.substring(crateType.indexOf("iron_") + 5);
                }

                return (Config.STAINED_CRATES_DYEABLE.getAsBoolean() && crateType.contains("_stained")
                        && !itemColor.equals(crateType.substring(0, crateType.indexOf("_stained"))))
                        || (Config.GLASS_CRATES_DYEABLE.getAsBoolean() && crateType.equals("glass"));
            }
        }

        return false;
    }

    /// Check if crate is currently able to be changed with a wooden foundation item
    public static boolean canApplyWoodFoundation(BlockState state, Item item) {
        List<Item> validFoundations = List.of(
                ModItems.OAK_FOUNDATION_ITEM.get(),
                ModItems.SPRUCE_FOUNDATION_ITEM.get(),
                ModItems.BIRCH_FOUNDATION_ITEM.get(),
                ModItems.JUNGLE_FOUNDATION_ITEM.get(),
                ModItems.ACACIA_FOUNDATION_ITEM.get(),
                ModItems.DARK_OAK_FOUNDATION_ITEM.get(),
                ModItems.MANGROVE_FOUNDATION_ITEM.get(),
                ModItems.CHERRY_FOUNDATION_ITEM.get(),
                ModItems.BAMBOO_FOUNDATION_ITEM.get(),
                ModItems.CRIMSON_FOUNDATION_ITEM.get(),
                ModItems.WARPED_FOUNDATION_ITEM.get()
        );

        if(validFoundations.contains(item) && state.getBlock() instanceof CrateBlock crateBlock) {
            String crateID = crateBlock.getDescriptionId(),
                    crateType = crateID.substring(crateID.indexOf("s.") + 2, crateID.indexOf("_crate")),
                    itemID = item.getDescriptionId(),
                    woodType = itemID.substring(itemID.indexOf("s.") + 2, itemID.indexOf("_foundation"));

            if(crateType.contains("iron") && !state.is(ModTags.Blocks.ORE_CRATES)) {
                crateType = crateType.substring(crateType.indexOf("iron_") + 5);
            }

            return !crateType.equals(woodType) && (state.is(ModTags.Blocks.GLASS_CRATES) || state.is(ModTags.Blocks.ORE_CRATES)
                    || state.is(ModTags.Blocks.SOIL_CRATES) || Config.WOOD_CHANGE_WOOD_CRATE.getAsBoolean());
        }

        return false;
    }

    /// Check if crate is currently able to be changed with a wooden foundation item
    public static boolean canApplyGlassFoundation(BlockState state, Item item) {
        List<Item> validFoundations = List.of(
                ModItems.GLASS_FOUNDATION_ITEM.get(),
                ModItems.WHITE_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.LIGHT_GRAY_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.GRAY_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.BLACK_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.BROWN_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.RED_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.ORANGE_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.YELLOW_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.LIME_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.GREEN_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.CYAN_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.LIGHT_BLUE_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.BLUE_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.PURPLE_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.MAGENTA_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.PINK_STAINED_GLASS_FOUNDATION_ITEM.get()
        );

        if(validFoundations.contains(item) && state.getBlock() instanceof CrateBlock crateBlock) {
            String crateID = crateBlock.getDescriptionId(),
                    crateType = crateID.substring(crateID.indexOf("s.") + 2, crateID.indexOf("_crate")),
                    itemID = item.getDescriptionId(),
                    glassType = itemID.substring(itemID.indexOf("s.") + 2, itemID.indexOf("_foundation"));

            if(crateType.contains("iron") && !state.is(ModTags.Blocks.ORE_CRATES)) {
                crateType = crateType.substring(crateType.indexOf("iron_") + 5);
            }

            return !crateType.equals(glassType) && (state.is(ModTags.Blocks.WOODEN_CRATES) || state.is(ModTags.Blocks.ORE_CRATES)
                    || state.is(ModTags.Blocks.SOIL_CRATES) || Config.GLASS_CHANGE_GLASS_CRATE.getAsBoolean());
        }

        return false;
    }

    /// Respawn base foundation for current block state
    public static void returnBase(BlockState state, Item foundation, Level level, BlockPos pos) {


        if(state.getBlock() instanceof GlassCrateBlock) {
            returnGlassBase(state, level, pos);
        }
        else if(!state.is(ModTags.Blocks.ORE_CRATES) && state.getBlock() instanceof OreCrateBlock block) {
            if(block.isCrateType("glass")) {
                returnGlassBase(state, level, pos);
            }
            else if(block.isCrateType("soil")) {
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                        new ItemStack(ModItems.DIRT_FOUNDATION_ITEM.get())));
            }
            else if(block.isCrateType("wood")){
                returnWoodBase(state, level, pos);
            }

            if(foundation.getDescriptionId().contains("iron")) {
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                        new ItemStack(ModItems.IRON_SUPPORTS_ITEM.get())));
            }
        }
        else if(state.is(ModTags.Blocks.ORE_CRATES)) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                    new ItemStack(ModItems.IRON_FOUNDATION_ITEM.get())));
        }
        else if(state.is(ModTags.Blocks.SOIL_CRATES)) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                    new ItemStack(ModItems.DIRT_FOUNDATION_ITEM.get())));
        }
        else {
            returnWoodBase(state, level, pos);
        }
    }

    /// Respawn wood foundation for current block state
    public static void returnWoodBase(BlockState state, Level level, BlockPos pos) {
        List<Item> foundations = List.of(
                ModItems.OAK_FOUNDATION_ITEM.get(),
                ModItems.SPRUCE_FOUNDATION_ITEM.get(),
                ModItems.BIRCH_FOUNDATION_ITEM.get(),
                ModItems.JUNGLE_FOUNDATION_ITEM.get(),
                ModItems.ACACIA_FOUNDATION_ITEM.get(),
                ModItems.DARK_OAK_FOUNDATION_ITEM.get(),
                ModItems.MANGROVE_FOUNDATION_ITEM.get(),
                ModItems.CHERRY_FOUNDATION_ITEM.get(),
                ModItems.BAMBOO_FOUNDATION_ITEM.get(),
                ModItems.CRIMSON_FOUNDATION_ITEM.get(),
                ModItems.WARPED_FOUNDATION_ITEM.get()
        );

        String baseBlockID = state.getBlock().getDescriptionId(),
                baseID = baseBlockID.substring(baseBlockID.indexOf("s.") + 2),
                baseWood, foundationID, foundationType;

        if(baseID.contains("iron_")) {
            baseWood = baseID.substring(baseID.indexOf("iron_") + 5, baseID.indexOf("_crate"));
        }
        else {
            baseWood = baseID.substring(0, baseID.indexOf("_crate"));
        }

        for(Item foundation : foundations) {
            foundationID = foundation.getDescriptionId();
            foundationType = foundationID.substring(foundationID.indexOf("s.") + 2, foundationID.indexOf("_foundation"));

            if (foundationType.equals(baseWood)) {
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                        new ItemStack(foundation)));
            }
        }
    }

    /// Respawn glass foundation for current block state
    public static void returnGlassBase(BlockState state, Level level, BlockPos pos) {
        List<Item> foundations = List.of(
                ModItems.GLASS_FOUNDATION_ITEM.get(),
                ModItems.WHITE_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.LIGHT_GRAY_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.GRAY_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.BLACK_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.BROWN_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.RED_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.ORANGE_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.YELLOW_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.LIME_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.GREEN_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.CYAN_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.LIGHT_BLUE_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.BLUE_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.PURPLE_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.MAGENTA_STAINED_GLASS_FOUNDATION_ITEM.get(),
                ModItems.PINK_STAINED_GLASS_FOUNDATION_ITEM.get()
        );

        String baseBlockID = state.getBlock().getDescriptionId(),
                baseID = baseBlockID.substring(baseBlockID.indexOf("s.") + 2),
                baseGlass, foundationID, foundationType;

        if(baseID.contains("iron_")) {
            baseGlass = baseID.substring(baseID.indexOf("iron_") + 5, baseID.indexOf("_crate"));
        }
        else {
            baseGlass = baseID.substring(0, baseID.indexOf("_crate"));
        }

        for(Item foundation : foundations) {
            foundationID = foundation.getDescriptionId();
            foundationType = foundationID.substring(foundationID.indexOf("s.") + 2, foundationID.indexOf("_foundation"));

            if (foundationType.equals(baseGlass)) {
                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                        new ItemStack(foundation)));
            }
        }
    }

    /// Play appropriate crate upgrade sounds and damage pliers, if applicable
    public static void handleItemResult(Level level, BlockPos pos, ItemStack stack, Player player, SoundEvent sound) {
        if(stack.getItem() == ModItems.PLIERS_ITEM.get()) {
            if(!player.isCreative()) {
                stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
            }

            if(!(stack.getDamageValue() >= stack.getMaxDamage())) {
                level.playSound(null, pos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1f, 1f);
            }
        }
        else if(sound == SoundEvents.ANVIL_USE || sound == SoundEvents.ANVIL_LAND) {
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 0.5f, 1f);
        }
        else {
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 1f, 1f);
        }
    }
}
