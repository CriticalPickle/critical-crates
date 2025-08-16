package com.criticalpickle.criticalcrates.block;

import com.criticalpickle.criticalcrates.Config;
import com.criticalpickle.criticalcrates.block.entity.CrateBlockEntity;
import com.criticalpickle.criticalcrates.registration.ModBlocks;
import com.criticalpickle.criticalcrates.registration.ModItems;
import com.criticalpickle.criticalcrates.util.DataComponentUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
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
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateBlock extends BaseEntityBlock {
    public static final MapCodec<CrateBlock> CODEC = simpleCodec(CrateBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty EXPLOSION_RESIST = BooleanProperty.create("explosion_resistant");
    public static final BooleanProperty LAMP_UPGRADE = BooleanProperty.create("lamp_upgrade");
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty FIREPROOF = BooleanProperty.create("fireproof");

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public CrateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y).setValue(EXPLOSION_RESIST, false)
                .setValue(LAMP_UPGRADE, false).setValue(LIT, false).setValue(POWERED, false)
                .setValue(FIREPROOF, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, EXPLOSION_RESIST, LAMP_UPGRADE, LIT, POWERED, FIREPROOF);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis axis = context.getClickedFace().getAxis();
        CustomData data = context.getItemInHand().get(DataComponents.CUSTOM_DATA);
        boolean resistant = false, lamp = false, fire = false;

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
        }

        return this.defaultBlockState().setValue(AXIS, axis).setValue(EXPLOSION_RESIST, resistant)
                .setValue(LAMP_UPGRADE, lamp).setValue(FIREPROOF, fire);
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

    // Fire effects block: Y/N
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
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return rotatePillar(state, rotation);
    }

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

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
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

    // Functionality from CopperBulbBlock class (flips state dependent on value in "POWERED" and if it is "LIT")
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
                blockEntity.drop();
                level.updateNeighbourForOutputSignal(pos, this);
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
        } else {
            if (level.getBlockEntity(pos) instanceof CrateBlockEntity blockEntity) {
                player.openMenu(new SimpleMenuProvider(blockEntity, Component.translatable("container.criticalcrates.crate")), pos);
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
            Item itemInStack = stack.getItem();
            CompoundTag dataTag = new CompoundTag();
            BlockEntity blockEntity = level.getBlockEntity(pos);

            // Check for upgrade change conditions
            if(Config.ADDONS_REMOVABLE.getAsBoolean() && hasUpgrades(state) && itemInStack == ModItems.PLIERS_ITEM.get()) {
                level.setBlockAndUpdate(pos, state.setValue(EXPLOSION_RESIST, false).setValue(LAMP_UPGRADE, false)
                        .setValue(LIT, false).setValue(FIREPROOF, false));

                spawnRemovedUpgrades(state, level, pos);

                dataTag.putBoolean("explosion_resistant", false);
                dataTag.putBoolean("lamp_upgrade", false);
                dataTag.putBoolean("fireproof", false);
            }
            else if(!hasUpgrades(state) && itemInStack == ModItems.OBSIDIAN_REINFORCEMENT_ITEM.get()) {
                stack.shrink(1);
                level.setBlockAndUpdate(pos, state.setValue(EXPLOSION_RESIST, true));
                dataTag.putBoolean("explosion_resistant", true);
                dataTag.putBoolean("lamp_upgrade", false);
                dataTag.putBoolean("fireproof", false);
            }
            else if(!hasUpgrades(state) && itemInStack == ModItems.LAMP_SIMULATOR_ITEM.get()) {
                stack.shrink(1);
                level.setBlockAndUpdate(pos, state.setValue(LAMP_UPGRADE, true));
                dataTag.putBoolean("explosion_resistant", false);
                dataTag.putBoolean("lamp_upgrade", true);
                dataTag.putBoolean("fireproof", false);
            }
            else if(!hasUpgrades(state) && itemInStack == ModItems.FIREPROOFING_ITEM.get()) {
                stack.shrink(1);
                level.setBlockAndUpdate(pos, state.setValue(FIREPROOF, true));
                dataTag.putBoolean("explosion_resistant", false);
                dataTag.putBoolean("lamp_upgrade", false);
                dataTag.putBoolean("fireproof", true);

                if(blockEntity != null) {
                    DataComponentUtils.addBlockEntityDataComponents(blockEntity, dataTag, DataComponents.FIRE_RESISTANT);
                }
            }
            else {
                // Exit out if no change in upgrade
                return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
            }

            if(blockEntity != null && !dataTag.getBoolean("fireproof")) {
                DataComponentUtils.addBlockEntityDataTag(blockEntity, dataTag);
            }

            playUpgradeSounds(level, pos, stack, player);

            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    // Spawn removed crate upgrades dependent on state property values
    private static void spawnRemovedUpgrades(BlockState state, Level level, BlockPos pos) {
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
    }

    // Crate has upgrades: Y/N
    private static boolean hasUpgrades(BlockState state) {
        return state.getValue(EXPLOSION_RESIST) || state.getValue(LAMP_UPGRADE) || state.getValue(FIREPROOF);
    }

    // Play appropriate crate upgrade sounds
    private static void playUpgradeSounds(Level level, BlockPos pos, ItemStack stack, Player player) {
        if(stack.getItem() != ModItems.PLIERS_ITEM.get()) {
            level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.5f, 1f);
        }
        else {
            if(!player.isCreative()) {
                stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
            }

            if(!(stack.getDamageValue() >= stack.getMaxDamage())){
                level.playSound(null, pos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1f, 1f);
            }
        }
    }
}
