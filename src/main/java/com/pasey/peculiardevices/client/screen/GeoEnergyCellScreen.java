package com.pasey.peculiardevices.client.screen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.client.screen.base.BaseDeviceScreen;
import com.pasey.peculiardevices.menu.GeoEnergyCellMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.pasey.peculiardevices.client.screen.util.FormatText.formatCapacity;

public class GeoEnergyCellScreen extends BaseDeviceScreen<GeoEnergyCellMenu> {
    public GeoEnergyCellScreen(GeoEnergyCellMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        renderEnergyBar(pGuiGraphics, this.leftPos + 83, this.topPos + 16, 176, 56);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        Component energyText =
                Component.literal("Energy: " + formatCapacity(menu.getEnergy(), menu.getMaxEnergy()));
        if(isHovering(83, 16, 10, 56, pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(font, energyText, pMouseX, pMouseY);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(PeculiarDevices.MODID, "textures/gui/container/geo_energy_cell.png");
    }
}
