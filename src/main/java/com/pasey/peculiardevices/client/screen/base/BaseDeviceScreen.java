package com.pasey.peculiardevices.client.screen.base;

import com.pasey.peculiardevices.menu.base.DeviceMenu;
import com.pasey.peculiardevices.menu.base.ProcessorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class BaseDeviceScreen<T extends DeviceMenu<?>> extends AbstractContainerScreen<T> {
    private final MutableComponent energyBarTooltip = Component.literal("");

    public BaseDeviceScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pGuiGraphics);
        pGuiGraphics.blit(getTexture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        renderEnergyBar(pGuiGraphics, this.leftPos + 11, this.topPos + 67, 176, 56);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        if(isHovering(11, 12, 10, 56, pMouseX, pMouseY)) {
            energyBarTooltip.getSiblings().clear();
            energyBarTooltip.append("Energy: ")
                    .append(String.valueOf(menu.getEnergy()))
                    .append("/")
                    .append(String.valueOf(menu.getMaxEnergy()));
            pGuiGraphics.renderTooltip(font, energyBarTooltip, pMouseX, pMouseY);
        }
    }

    /**
     * This method renders the progress arrow for the processor menu.
     * All coordinates should specify the upper left corner.
     * @param pGuiGraphics
     * @param targetX The X coordinate to render the progress arrow at
     * @param targetY The Y coordinate to render the progress arrow at
     * @param sourceX The X coordinate of the source texture
     * @param sourceY The Y coordinate of the source texture
     * @param height The height of the progress arrow
     */
    protected void renderProgressArrow(GuiGraphics pGuiGraphics, int targetX, int targetY, int sourceX, int sourceY, int height) {
        if (!(menu instanceof ProcessorMenu<?> processorMenu))
            return;

        if(processorMenu.isCrafting()) {
            int length = processorMenu.getProgressArrowSize();
            pGuiGraphics.blit(getTexture(), targetX, targetY, sourceX, sourceY, length, height);
        }
    }

    private void renderEnergyBar(GuiGraphics pGuiGraphics, int targetX, int targetY, int sourceX, int sourceY) {
        if (menu.getEnergy() > 0) {
            int energyBarHeight = (int) (menu.getScaledEnergy() * 56);
            pGuiGraphics.blit(getTexture(), targetX, targetY - energyBarHeight, sourceX, sourceY - energyBarHeight, 10, energyBarHeight);
        }
    }

    protected abstract ResourceLocation getTexture();
}
