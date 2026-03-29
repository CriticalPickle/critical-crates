package com.criticalpickle.criticalcrates.block.entity.renderer;

import com.criticalpickle.criticalcrates.block.entity.GlassCrateBlockEntity;
import com.criticalpickle.criticalcrates.block.entity.OreCrateBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class OreUpgradeGlassCrateBlockEntityRenderer implements BlockEntityRenderer<OreCrateBlockEntity, OreUpgradeGlassCrateBlockEntityRenderState>  {
    public OreUpgradeGlassCrateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public OreUpgradeGlassCrateBlockEntityRenderState createRenderState() {
        return new OreUpgradeGlassCrateBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(OreCrateBlockEntity blockEntity, OreUpgradeGlassCrateBlockEntityRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

        renderState.blockEntity = blockEntity;
    }

    @Override
    public void submit(OreUpgradeGlassCrateBlockEntityRenderState oreUpgradeGlassCrateBlockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        ItemStackRenderState itemRendererState = new ItemStackRenderState();
        OreCrateBlockEntity blockEntity = oreUpgradeGlassCrateBlockEntityRenderState.blockEntity;
        ItemStack stack = blockEntity.getInventory().copyToList().getFirst();
        int lightTexture = LightTexture.pack(blockEntity.getLevel().getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos()),
                blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos()));

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.scale(0.75f, 0.75f, 0.75f);
        poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRenderingRotation()));

        Minecraft.getInstance().getItemModelResolver().updateForTopItem(itemRendererState, stack,
                ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);

        itemRendererState.submit(poseStack, submitNodeCollector, lightTexture, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
