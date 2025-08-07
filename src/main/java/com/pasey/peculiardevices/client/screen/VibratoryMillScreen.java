package com.pasey.peculiardevices.client.screen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.client.screen.base.BaseDeviceScreen;
import com.pasey.peculiardevices.menu.VibratoryMillMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

public class VibratoryMillScreen extends BaseDeviceScreen<VibratoryMillMenu> {
    public VibratoryMillScreen(VibratoryMillMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        renderProgressArrow(pGuiGraphics, this.leftPos + 79, this.topPos + 35, 176, 0, 17);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(PeculiarDevices.MODID, "textures/gui/container/vibratory_mill.png");
    }
}
