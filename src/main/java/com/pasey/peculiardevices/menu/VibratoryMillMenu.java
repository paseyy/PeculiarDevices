package com.pasey.peculiardevices.menu;

import com.pasey.peculiardevices.blockentities.VibratoryMillBlockEntity;
import com.pasey.peculiardevices.menu.base.ProcessorMenu;
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

public class VibratoryMillMenu extends ProcessorMenu<VibratoryMillBlockEntity> {

    // Client Constructor
    public VibratoryMillMenu(int containerId, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerId, playerInv,
                Objects.requireNonNull(playerInv.player.level().getBlockEntity(additionalData.readBlockPos())),
                new SimpleContainerData(2));
    }

    // Server Constructor
    public VibratoryMillMenu(int containerId, Inventory playerInv, BlockEntity blockEntity, ContainerData progressData) {
        super(PDMenus.VIBRATORY_MILL_MENU.get(), playerInv, containerId, (VibratoryMillBlockEntity) blockEntity, progressData);

        createPlayerHotbar(playerInv);
        createPlayerInventory(playerInv);

        createBlockEntityInventory((VibratoryMillBlockEntity) blockEntity);
    }

    public void createBlockEntityInventory(VibratoryMillBlockEntity be) {
        be.getOptional().ifPresent(inventory -> {
            addSlot(new SlotItemHandler(inventory, 0, 59, 35));
            addSlot(new OutputSlotItemHandler(inventory, 1, 109, 16));
            addSlot(new OutputSlotItemHandler(inventory, 2, 109, 35));
            addSlot(new OutputSlotItemHandler(inventory, 3, 109, 54));
        });
    }

    @Override
    public int getInventorySlots() {
        return 4;
    }
}
