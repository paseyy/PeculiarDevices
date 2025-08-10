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
        renderEnergyBar(pGuiGraphics, this.leftPos + 11, this.topPos + 67, 176, 56);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        Component energyStoredText1 =
                Component.literal("Energy:");
        Component energyStoredText2 =
                Component.literal(formatCapacity(menu.getEnergy(), menu.getMaxEnergy()));
        pGuiGraphics.drawString(font, energyStoredText1, this.leftPos + 47, this.topPos + 24, DISPLAY_TEXT_COLOR, false);
        pGuiGraphics.drawString(font, energyStoredText2, this.leftPos + 47, this.topPos + 34, DISPLAY_TEXT_COLOR, false);

        Component energyText =
                Component.literal("Energy: " + formatCapacity(menu.getEnergy(), menu.getMaxEnergy()));
        if(isHovering(11, 12, 10, 56, pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(font, energyText, pMouseX, pMouseY);
        }
    }

    @Override
    @SuppressWarnings("removal")
    protected ResourceLocation getTexture() {
        return new ResourceLocation(PeculiarDevices.MODID, "textures/gui/container/geo_energy_cell.png");
    }
}
