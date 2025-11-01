package com.criticalpickle.criticalcrates.mixin.supplementaries;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.mehvahdjukaar.supplementaries.common.events.overrides.SoapBehavior")
public class SoapMixin {
    @Inject(method = "tryPerformingAction", at = @At("HEAD"), cancellable = true, remap = false)
    private void onTryPerformingAction(Level level, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        BlockState state = level.getBlockState(hit.getBlockPos());
        if(state.getBlock().getDescriptionId().contains("criticalcrates")) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
