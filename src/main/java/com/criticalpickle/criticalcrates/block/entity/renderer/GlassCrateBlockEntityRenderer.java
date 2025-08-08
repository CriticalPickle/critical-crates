package com.criticalpickle.criticalcrates.block.entity.renderer;

import com.criticalpickle.criticalcrates.block.entity.GlassCrateBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;

public class GlassCrateBlockEntityRenderer implements BlockEntityRenderer<GlassCrateBlockEntity> {
    public GlassCrateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(GlassCrateBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = blockEntity.inventory.getStackInSlot(0);
        int lightTexture = LightTexture.pack(blockEntity.getLevel().getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos()), blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos()));

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.scale(0.75f, 0.75f, 0.75f);
        poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRenderingRotation()));

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, lightTexture, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
        poseStack.popPose();
    }
}
