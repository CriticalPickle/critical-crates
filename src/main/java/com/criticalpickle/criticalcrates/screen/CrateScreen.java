package com.criticalpickle.criticalcrates.screen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CrateScreen extends AbstractContainerScreen<CrateMenu> {
    private static final Identifier GUI_PNG =
            Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "textures/gui/crate/crate_gui.png");

    public CrateScreen(CrateMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, GUI_PNG);
//
//        int x = (width - imageWidth) / 2;
//        int y = (height - imageHeight) / 2;

        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            GUI_PNG,
            this.leftPos, this.topPos,
            0, 0,
            this.imageWidth, this.imageHeight,
            256, 256
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
