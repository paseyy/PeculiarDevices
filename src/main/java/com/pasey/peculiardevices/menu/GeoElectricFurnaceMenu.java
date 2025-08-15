package com.pasey.peculiardevices.menu;

import com.pasey.peculiardevices.blockentities.GeoElectricFurnaceBlockEntity;
import com.pasey.peculiardevices.menu.base.DeviceMenu;
import com.pasey.peculiardevices.menu.util.OutputSlotItemHandler;
import com.pasey.peculiardevices.registration.PDMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class GeoElectricFurnaceMenu extends DeviceMenu<GeoElectricFurnaceBlockEntity> {
    protected final ContainerData progressData;

    // Client Constructor
    public GeoElectricFurnaceMenu(int pContainerId, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(pContainerId, playerInv,
                Objects.requireNonNull(playerInv.player.level()).getBlockEntity(additionalData.readBlockPos()),
                new SimpleContainerData(2));
    }

    // Server Constructor
    public GeoElectricFurnaceMenu(int containerId, Inventory playerInv, BlockEntity blockEntity, ContainerData progressData) {
        super(PDMenus.GEO_ELECTRIC_FURNACE_MENU.get(), containerId, (GeoElectricFurnaceBlockEntity) blockEntity);

        this.progressData = progressData;
        addDataSlots(progressData);

        createPlayerHotbar(playerInv);
        createPlayerInventory(playerInv);
        createBlockEntityInventory((GeoElectricFurnaceBlockEntity) blockEntity);
    }

    public void createBlockEntityInventory(GeoElectricFurnaceBlockEntity be) {
        be.getInventoryOptional().ifPresent(inventory -> {
            addSlot(new SlotItemHandler(inventory, 0, 59, 35));
            addSlot(new OutputSlotItemHandler(inventory, 1, 109, 35));
        });
    }

    @Override
    public int getInventorySlots() {
        return 2;
    }

    public int getProgress() {
        return progressData.get(0);
    }

    public int getMaxProgress() {
        return progressData.get(1);
    }

    public boolean isCrafting() {
        return getProgress() > 0;
    }

    public int getProgressArrowSize() {
        return Mth.ceil(getScaledProgress() * 24);
    }

    public float getScaledProgress() {
        return getMaxProgress() == 0 ? 0.0f : ((float) getProgress()) / getMaxProgress();
    }
}
