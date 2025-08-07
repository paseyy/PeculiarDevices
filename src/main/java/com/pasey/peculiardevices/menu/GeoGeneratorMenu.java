package com.pasey.peculiardevices.menu;

import com.pasey.peculiardevices.blockentities.GeoGeneratorBlockEntity;
import com.pasey.peculiardevices.menu.base.DeviceMenu;
import com.pasey.peculiardevices.registration.PDMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class GeoGeneratorMenu extends DeviceMenu<GeoGeneratorBlockEntity> {

    // Client Constructor
    public GeoGeneratorMenu(int containerId, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerId, playerInv,
                Objects.requireNonNull(playerInv.player.level().getBlockEntity(additionalData.readBlockPos())),
                new SimpleContainerData(2));
    }

    // Server Constructor
    public GeoGeneratorMenu(int pContainerId, Inventory playerInv, BlockEntity pBlockEntity, ContainerData energyData) {
        super(PDMenus.GEO_GENERATOR_MENU.get(), pContainerId, (GeoGeneratorBlockEntity) pBlockEntity);

        createPlayerHotbar(playerInv);
        createPlayerInventory(playerInv);

        addDataSlots(energyData);
    }

    @Override
    public int getInventorySlots() {
        return 0;
    }
}
