package com.criticalpickle.criticalcrates.screen;

import com.criticalpickle.criticalcrates.CriticalCrates;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class LargeCrateScreen extends AbstractContainerScreen<LargeCrateMenu> {
    private static final ResourceLocation GUI_PNG =
            ResourceLocation.fromNamespaceAndPath(CriticalCrates.MODID, "textures/gui/crate/large_crate_gui.png");
    private final int containerRows;

    public LargeCrateScreen(LargeCrateMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        containerRows = 6;
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI_PNG, i, j, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        guiGraphics.blit(GUI_PNG, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
