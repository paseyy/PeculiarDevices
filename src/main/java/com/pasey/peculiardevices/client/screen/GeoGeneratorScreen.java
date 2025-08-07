package com.pasey.peculiardevices.client.screen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.client.screen.base.BaseDeviceScreen;
import com.pasey.peculiardevices.menu.GeoGeneratorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.pasey.peculiardevices.client.screen.util.FormatText.formatCapacity;
import static com.pasey.peculiardevices.client.screen.util.FormatText.formatEnergy;

public class GeoGeneratorScreen extends BaseDeviceScreen<GeoGeneratorMenu> {

    public GeoGeneratorScreen(GeoGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        renderEnergyBar(pGuiGraphics, this.leftPos + 11, this.topPos + 67, 176, 56);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        Component energyStoredText =
                Component.literal("Energy: " + formatCapacity(menu.getEnergy(), menu.getMaxEnergy()));
        Component energyGeneratedText =
                Component.literal("Generating " + formatEnergy(menu.getBlockEntity().getEnergyGenPerTick(), true) + "/t");
        pGuiGraphics.drawString(font, energyStoredText, this.leftPos + 33, this.topPos + 26, 0x6666FF, false);
        pGuiGraphics.drawString(font, energyGeneratedText, this.leftPos + 33, this.topPos + 37, 0x6666FF, false);

        Component energyText =
                Component.literal("Energy: " + formatCapacity(menu.getEnergy(), menu.getMaxEnergy()));
        if(isHovering(11, 15, 10, 56, pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(font, energyText, pMouseX, pMouseY);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(PeculiarDevices.MODID, "textures/gui/container/geo_generator.png");
    }
}
