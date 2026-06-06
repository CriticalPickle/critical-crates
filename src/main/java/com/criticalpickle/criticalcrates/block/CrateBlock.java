package com.criticalpickle.criticalcrates.block;

import com.criticalpickle.criticalcrates.Config;
import com.criticalpickle.criticalcrates.block.entity.CrateBlockEntity;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.registration.ModTags;
import com.criticalpickle.criticalcrates.util.CacheSwitchInventory;
import com.criticalpickle.criticalcrates.util.DataComponentUtils;
import static com.criticalpickle.criticalcrates.util.CrateUtil.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateBlock extends BaseEntityBlock {
    public static final MapCodec<CrateBlock> CODEC = simpleCodec(CrateBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty SWITCH = BooleanProperty.create("switch");
    public static final BooleanProperty EXPLOSION_RESIST = BooleanProperty.create("explosion_resistant");
    public static final BooleanProperty LAMP_UPGRADE = BooleanProperty.create("lamp_upgrade");
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty FIREPROOF = BooleanProperty.create("fireproof");
    public static final BooleanProperty SLIMY = BooleanProperty.create("slimy");

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public CrateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y).setValue(SWITCH, false)
                .setValue(EXPLOSION_RESIST, false).setValue(LAMP_UPGRADE, false).setValue(LIT, false)
                .setValue(POWERED, false).setValue(FIREPROOF, false).setValue(SLIMY, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, SWITCH, EXPLOSION_RESIST, LAMP_UPGRADE, LIT, POWERED, FIREPROOF, SLIMY);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis axis = context.getClickedFace().getAxis();
        CustomData data = context.getItemInHand().get(DataComponents.CUSTOM_DATA);
        boolean resistant = false, lamp = false, fire = false, slimy = false;

        if(data != null) {
            if(data.contains("explosion_resistant")) {
                resistant = data.copyTag().getBoolean("explosion_resistant");
            }

            if(data.contains("lamp_upgrade")) {
                lamp = data.copyTag().getBoolean("lamp_upgrade");
            }

            if(data.contains("fireproof")) {
                fire = data.copyTag().getBoolean("fireproof");
            }

            if(data.contains("slimy")) {
                slimy = data.copyTag().getBoolean("slimy");
            }
        }

        return this.defaultBlockState().setValue(AXIS, axis).setValue(SWITCH, false)
                .setValue(EXPLOSION_RESIST, resistant).setValue(LAMP_UPGRADE, lamp).setValue(FIREPROOF, fire).setValue(SLIMY, slimy);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return hasFireEffect(state);
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Most Wood (5) or No Effect (0)
        return hasFireEffect(state) ? 5 : 0;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Most Wood (15) or No Effect (0)
        return hasFireEffect(state) ? 20 : 0;
    }

    /// Fire effects block: Y/N
    protected boolean hasFireEffect(BlockState state) {
        return !state.is(ModBlocks.CRIMSON_CRATE.get()) && !state.is(ModBlocks.WARPED_CRATE.get()) && !state.getValue(FIREPROOF);
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        //Obsidian (1200) or Barrel (2.5)
        return state.getValue(EXPLOSION_RESIST) ? 1200f : 2.5f;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return state.getValue(LAMP_UPGRADE);
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return level.getBlockState(pos).getValue(LIT) ? 15 : 0;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if(!state.getValue(SLIMY) || entity.isSuppressingBounce()) {
            super.fallOn(level, state, pos, entity, fallDistance);
        }
        else {
            entity.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(state.getValue(SLIMY)) {
            double d0 = Math.abs(entity.getDeltaMovement().y);
            if (d0 < 0.1 && !entity.isSteppingCarefully()) {
                double d1 = 0.4 + d0 * 0.2;
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0, d1));
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if(!level.getBlockState(entity.getOnPos()).getValue(SLIMY) || entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(level, entity);
        }
        else {
            this.bounceUp(entity);
        }
    }

    protected void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < 0.0) {
            double d0 = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
        }
    }

    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if(state.getValue(SLIMY)) {
            // Return float greater than 1 for instant slime-like breaking
            return 1.01F;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return rotatePillar(state, rotation);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if(blockState.getValue(SWITCH)) {
            CrateBlockEntity crateBlockEntity = new CrateBlockEntity(blockPos, blockState);
            crateBlockEntity.copyInventory(CacheSwitchInventory.getCache());
            return crateBlockEntity;
        }
        return new CrateBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (state.getValue(LAMP_UPGRADE) && oldState.getBlock() != state.getBlock() && level instanceof ServerLevel serverlevel) {
            this.checkAndFlip(state, serverlevel, pos);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (state.getValue(LAMP_UPGRADE) && level instanceof ServerLevel serverlevel) {
            this.checkAndFlip(state, serverlevel, pos);
        }
    }

    /// Functionality from CopperBulbBlock class (flips state dependent on value in "POWERED" and if it is "LIT")
    public void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
        boolean isPowered = level.hasNeighborSignal(pos);
        if (isPowered != state.getValue(POWERED)) {
            BlockState blockstate = state;
            if (!state.getValue(POWERED)) {
                blockstate = state.cycle(LIT);
            }
            level.setBlockAndUpdate(pos, blockstate.setValue(POWERED, isPowered));
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof CrateBlockEntity blockEntity) {
                if(state.getValue(SWITCH)) {
                    CacheSwitchInventory.cache(blockEntity.getInventory());
                }
                else {
                    blockEntity.drop();
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = new ItemStack(this);
        CompoundTag dataTag = new CompoundTag();

        dataTag.putBoolean("explosion_resistant", state.getValue(EXPLOSION_RESIST));
        dataTag.putBoolean("lamp_upgrade", state.getValue(LAMP_UPGRADE));
        dataTag.putBoolean("fireproof", state.getValue(FIREPROOF));
        dataTag.putBoolean("slimy", state.getValue(SLIMY));
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));

        if(dataTag.getBoolean("fireproof")) {
            stack.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
        }

        return stack;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        else {
            if (level.getBlockEntity(pos) instanceof CrateBlockEntity blockEntity) {
                player.openMenu(new SimpleMenuProvider(blockEntity, blockEntity.getDisplayName()), pos);
                level.playSound(null, pos, getInventoryOpenSound(), SoundSource.BLOCKS, 0.5f, 1f);
            }

            return InteractionResult.CONSUME;
        }
    }

    // Implemented for glass crate override
    protected SoundEvent getInventoryOpenSound() {
        return SoundEvents.BARREL_OPEN;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            Item itemInHand = stack.getItem();
            CompoundTag dataTag = new CompoundTag();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            SoundEvent sound = SoundEvents.ANVIL_USE;
            String iron = "";

            // Check if block is iron variant and add iron string
            if(!state.is(ModTags.Blocks.ORE_CRATES) && state.getBlock() instanceof OreCrateBlock) {
                iron = "iron_";
            }

            // Check for item change conditions
            if(Config.ADDONS_REMOVABLE.getAsBoolean() && hasUpgrades(state) && itemInHand == ModItems.PLIERS_ITEM.get()) {
                if(!(state.getBlock() instanceof OreCrateBlock) || state.is(ModTags.Blocks.ORE_CRATES)) {
                    level.setBlockAndUpdate(pos, state.setValue(EXPLOSION_RESIST, false).setValue(LAMP_UPGRADE, false)
                            .setValue(LIT, false).setValue(FIREPROOF, false).setValue(SLIMY, false));

                    spawnRemovedUpgrades(state, level, pos);
                    setDataTagUpgrades(dataTag, false, false, false, false, null);
                }
                else {
                    String baseBlockID = state.getBlock().getDescriptionId(), baseID = baseBlockID.substring(baseBlockID.indexOf("iron_") + 5);
                    Block crateBlock = getCrateBlock("block.criticalcrates." + baseID);

                    if(crateBlock != null) {
                        blockEntity = switchCrate(level, pos, state, crateBlock, dataTag);
                    }

                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(),
                            new ItemStack(ModItems.IRON_SUPPORTS_ITEM.get())));
                }
            }
            else if(Config.STAINED_COLOR_REMOVABLE.getAsBoolean() && hasSoap(itemInHand, state)) {
                Block crateBlock = getCrateBlock("block.criticalcrates." + iron + "glass_crate");

                if(crateBlock != null) {
                    if(!player.isCreative()) stack.shrink(1);
                    blockEntity = switchCrate(level, pos, state, crateBlock, dataTag);
                }
                sound = SoundEvents.DYE_USE;
            }
            else if(!hasUpgrades(state) && itemInHand == ModItems.OBSIDIAN_REINFORCEMENT_ITEM.get()) {
                if(!player.isCreative()) stack.shrink(1);
                level.setBlockAndUpdate(pos, state.setValue(EXPLOSION_RESIST, true));
                setDataTagUpgrades(dataTag, true, false, false, false, null);
            }
            else if(!hasUpgrades(state) && itemInHand == ModItems.LAMP_SIMULATOR_ITEM.get()) {
                if(!player.isCreative()) stack.shrink(1);
                level.setBlockAndUpdate(pos, state.setValue(LAMP_UPGRADE, true));
                setDataTagUpgrades(dataTag, false, true, false, false, null);
            }
            else if(!hasUpgrades(state) && itemInHand == ModItems.FIREPROOFING_ITEM.get()) {
                if(!player.isCreative()) stack.shrink(1);
                level.setBlockAndUpdate(pos, state.setValue(FIREPROOF, true));
                setDataTagUpgrades(dataTag, false, false, true, false, blockEntity);
            }
            else if(!hasUpgrades(state) && itemInHand == ModItems.SLIMY_FRAMING_ITEM.get()) {
                if(!player.isCreative()) stack.shrink(1);
                level.setBlockAndUpdate(pos, state.setValue(SLIMY, true));
                setDataTagUpgrades(dataTag, false, false, false, true, blockEntity);
            }
            else if(!hasUpgrades(state) && !state.is(ModTags.Blocks.ORE_CRATES) && itemInHand == ModItems.IRON_SUPPORTS_ITEM.get()) {
                String baseBlockID = state.getBlock().getDescriptionId(), baseID = baseBlockID.substring(baseBlockID.indexOf("s.") + 2);
                Block crateBlock = getCrateBlock("block.criticalcrates.iron_" + baseID);

                if(crateBlock != null) {
                    if(!player.isCreative()) stack.shrink(1);
                    blockEntity = switchCrate(level, pos, state, crateBlock, dataTag);
                }
            }
            else if(canDye(state, itemInHand)) {
                String dyeColor = itemInHand.getDescriptionId().substring(itemInHand.getDescriptionId().indexOf("minecraft.") + 10, itemInHand.getDescriptionId().indexOf("_dye"));
                Block crateBlock = getCrateBlock("block.criticalcrates." + iron + dyeColor + "_stained_glass_crate");

                if(crateBlock != null) {
                    if(!player.isCreative()) stack.shrink(1);
                    blockEntity = switchCrate(level, pos, state, crateBlock, dataTag);
                }
                sound = SoundEvents.GLOW_INK_SAC_USE;
            }
            else if(canApplyGlassFoundation(state, itemInHand)) {
                String itemID = itemInHand.getDescriptionId(),
                        glassType = itemID.substring(itemID.indexOf("s.") + 2, itemID.indexOf("_foundation"));

                Block crateBlock = getCrateBlock("block.criticalcrates." + iron + glassType + "_crate");

                returnBase(state, itemInHand, level, pos);
                if(crateBlock != null) {
                    if(!player.isCreative()) stack.shrink(1);
                    blockEntity = switchCrate(level, pos, state, crateBlock, dataTag);
                }
                sound = SoundEvents.GLASS_PLACE;
            }
            else if(!state.is(ModTags.Blocks.ORE_CRATES) && itemInHand == ModItems.IRON_FOUNDATION_ITEM.get()) {
                Block crateBlock = getCrateBlock("block.criticalcrates.iron_crate");

                returnBase(state, itemInHand, level, pos);
                if(crateBlock != null) {
                    if(!player.isCreative()) stack.shrink(1);
                    blockEntity = switchCrate(level, pos, state, crateBlock, dataTag);
                }
                sound = SoundEvents.ANVIL_LAND;
            }
            else if(!state.is(ModTags.Blocks.SOIL_CRATES) && itemInHand == ModItems.DIRT_FOUNDATION_ITEM.get()) {
                Block crateBlock = getCrateBlock("block.criticalcrates." + iron + "dirt_crate");

                returnBase(state, itemInHand, level, pos);
                if(crateBlock != null) {
                    if(!player.isCreative()) stack.shrink(1);
                    blockEntity = switchCrate(level, pos, state, crateBlock, dataTag);
                }
                sound = SoundEvents.ROOTED_DIRT_PLACE;
            }
            else if(canApplyWoodFoundation(state, itemInHand)) {
                String itemID = itemInHand.getDescriptionId(),
                        woodType = itemID.substring(itemID.indexOf("s.") + 2, itemID.indexOf("_foundation"));

                Block crateBlock = getCrateBlock("block.criticalcrates." + iron + woodType + "_crate");

                returnBase(state, itemInHand, level, pos);
                if(crateBlock != null) {
                    if(!player.isCreative()) stack.shrink(1);
                    blockEntity = switchCrate(level, pos, state, crateBlock, dataTag);
                }
                sound = SoundEvents.WOOD_BREAK;
            }
            else {
                // Exit if no change in upgrade
                return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
            }

            if(blockEntity != null && !dataTag.getBoolean("fireproof")) {
                DataComponentUtils.addBlockEntityDataTag(blockEntity, dataTag);
            }

            handleItemResult(level, pos, stack, player, sound);

            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    /// Crate has upgrades: Y/N
    protected boolean hasUpgrades(BlockState state) {
        return state.getValue(EXPLOSION_RESIST) || state.getValue(LAMP_UPGRADE) || state.getValue(FIREPROOF) || state.getValue(SLIMY);
    }

    /// Switch state of crate to state of "crateBlock"
    public BlockEntity switchCrate(Level level, BlockPos pos, BlockState state, Block crateBlock, CompoundTag dataTag) {
        boolean resistant, lamp, fire, slimy;

        if(hasUpgrades(state)) {
            if(state.getValue(EXPLOSION_RESIST)) {
                resistant = true;
                lamp = fire = slimy = false;
                setDataTagUpgrades(dataTag, resistant, lamp, fire, slimy, null);
            }
            else if(state.getValue(LAMP_UPGRADE)) {
                lamp = true;
                resistant = fire = slimy = false;
                setDataTagUpgrades(dataTag, resistant, lamp, fire, slimy, null);
            }
            else if(state.getValue(FIREPROOF)) {
                fire = true;
                resistant = lamp = slimy = false;
                setDataTagUpgrades(dataTag, resistant, lamp, fire, slimy, null);
            }
            else if(state.getValue(SLIMY)) {
                slimy = true;
                resistant = lamp = fire = false;
                setDataTagUpgrades(dataTag, resistant, lamp, fire, slimy, null);
            }
        }
        else {
            setDataTagUpgrades(dataTag, false, false, false, false, null);
        }

        level.setBlockAndUpdate(pos, state.setValue(SWITCH, true));
        handleBlockSwap(level, pos, state, crateBlock);

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(dataTag.getBoolean("fireproof") && blockEntity != null) {
            DataComponentUtils.addBlockEntityDataComponents(blockEntity, dataTag, DataComponents.FIRE_RESISTANT);
        }

        return blockEntity;
    }
}
