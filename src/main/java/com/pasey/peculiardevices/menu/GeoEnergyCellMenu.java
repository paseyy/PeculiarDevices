package com.pasey.peculiardevices.menu;

import com.pasey.peculiardevices.blockentities.GeoEnergyCellBlockEntity;
import com.pasey.peculiardevices.menu.base.DeviceMenu;
import com.pasey.peculiardevices.registration.PDMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class GeoEnergyCellMenu extends DeviceMenu<GeoEnergyCellBlockEntity> {
    // Client Constructor
    public GeoEnergyCellMenu(int containerId, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerId, playerInv,
                Objects.requireNonNull(playerInv.player.level().getBlockEntity(additionalData.readBlockPos())),
                new SimpleContainerData(2)
        );
    }

    public GeoEnergyCellMenu(int pContainerId, Inventory playerInv, BlockEntity pBlockEntity, ContainerData energyData) {
        super(PDMenus.GEO_ENERGY_CELL_MENU.get(), pContainerId, (GeoEnergyCellBlockEntity) pBlockEntity);

        createPlayerHotbar(playerInv);
        createPlayerInventory(playerInv);
        addDataSlots(energyData);

        createBlockEntityInventory(blockEntity);
    }

    public void createBlockEntityInventory(GeoEnergyCellBlockEntity be) {
        be.getInventoryOptional().ifPresent(inventory -> addSlot(new SlotItemHandler(inventory, 0, 147, 32)));
    }

    @Override
    public int getInventorySlots() {
        return 1;
    }
}
