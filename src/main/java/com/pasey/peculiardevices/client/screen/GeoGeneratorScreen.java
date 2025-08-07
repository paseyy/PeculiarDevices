package com.pasey.peculiardevices.client.screen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.client.screen.base.BaseDeviceScreen;
import com.pasey.peculiardevices.menu.GeoGeneratorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

public class GeoGeneratorScreen extends BaseDeviceScreen<GeoGeneratorMenu> {
    private final MutableComponent energyStoredText = Component.literal("");
    private final MutableComponent energyGeneratedText = Component.literal("");

    public GeoGeneratorScreen(GeoGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        energyStoredText.getSiblings().clear();
        energyStoredText.append("Energy: ")
                        .append(String.valueOf(menu.getEnergy()))
                        .append("/")
                        .append(String.valueOf(menu.getMaxEnergy()));
        energyGeneratedText.getSiblings().clear();
        energyGeneratedText.append("Generating ")
                           .append(String.valueOf(menu.getBlockEntity().getEnergyGenPerTick()))
                           .append(" FE/s");
        pGuiGraphics.drawString(font, energyStoredText, this.leftPos + 44, this.topPos + 29, 0x6666FF, false);
        pGuiGraphics.drawString(font, energyGeneratedText, this.leftPos + 44, this.topPos + 40, 0x6666FF, false);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(PeculiarDevices.MODID, "textures/gui/container/geo_generator.png");
    }
}
