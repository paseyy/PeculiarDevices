package com.pasey.peculiardevices.client.screen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.client.screen.base.BaseDeviceScreen;
import com.pasey.peculiardevices.menu.GrimeDynamoMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.pasey.peculiardevices.client.screen.util.FormatText.formatCapacity;

public class GrimeDynamoScreen extends BaseDeviceScreen<GrimeDynamoMenu> {
    public GrimeDynamoScreen(GrimeDynamoMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        renderEnergyBar(pGuiGraphics, this.leftPos + 11, this.topPos + 67, 176, 56);
        renderBurnTimer(pGuiGraphics, this.leftPos + 80, this.topPos + 39, 186, 14);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        Component energyText =
                Component.literal("Energy: " + formatCapacity(menu.getEnergy(), menu.getMaxEnergy()));
        if(isHovering(11, 15, 10, 56, pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(font, energyText, pMouseX, pMouseY);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.fromNamespaceAndPath(PeculiarDevices.MODID, "textures/gui/container/grime_dynamo.png");
    }

    protected void renderBurnTimer(GuiGraphics pGuiGraphics, int targetX, int targetY, int sourceX, int sourceY) {
        if (menu.getBurnTime() > 0) {
            int burnTimerHeight = (int) (menu.getScaledBurnTime() * 14);
            pGuiGraphics.blit(getTexture(), targetX, targetY - burnTimerHeight, sourceX, sourceY - burnTimerHeight, 14, burnTimerHeight);
        }
    }
}
