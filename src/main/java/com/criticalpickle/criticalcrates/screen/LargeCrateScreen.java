package com.criticalpickle.criticalcrates.screen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class LargeCrateScreen extends AbstractContainerScreen<LargeCrateMenu> {
    private static final Identifier GUI_PNG =
            Identifier.fromNamespaceAndPath(CriticalCrates.MODID, "textures/gui/crate/large_crate_gui.png");

    public LargeCrateScreen(LargeCrateMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 114 + menu.getRows() * 18);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED, GUI_PNG,
                i, j, 0.0F, 0.0F,
                this.imageWidth, menu.getRows() * 18 + 17,
                256, 256
        );
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED, GUI_PNG,
                i, j + menu.getRows() * 18 + 17, 0.0F, 126.0F,
                this.imageWidth, 96,
                256, 256
        );
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
        this.extractTooltip(guiGraphics, mouseX, mouseY);
    }
}
