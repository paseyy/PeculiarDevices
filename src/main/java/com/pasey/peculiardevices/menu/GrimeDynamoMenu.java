package com.pasey.peculiardevices.menu;

import com.pasey.peculiardevices.blockentities.GrimeDynamoBlockEntity;
import com.pasey.peculiardevices.menu.base.DeviceMenu;
import com.pasey.peculiardevices.registration.PDMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class GrimeDynamoMenu extends DeviceMenu<GrimeDynamoBlockEntity> {
    protected final ContainerData burnData;

    // Client Constructor
    public GrimeDynamoMenu(int containerId, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerId, playerInv,
                Objects.requireNonNull(playerInv.player.level().getBlockEntity(additionalData.readBlockPos())),
                new SimpleContainerData(2), new SimpleContainerData(2));
    }

    // Server Constructor
    public GrimeDynamoMenu(int pContainerId, Inventory playerInv, BlockEntity pBlockEntity, ContainerData energyData, ContainerData burnData) {
        super(PDMenus.GRIME_DYNAMO_MENU.get(), pContainerId, (GrimeDynamoBlockEntity) pBlockEntity);
        this.burnData = burnData;

        createPlayerHotbar(playerInv);
        createPlayerInventory(playerInv);

        addDataSlots(energyData);
        addDataSlots(burnData);

        createBlockEntityInventory(blockEntity);
    }

    public void createBlockEntityInventory(GrimeDynamoBlockEntity be) {
        be.getInventoryOptional().ifPresent(inventory -> addSlot(new SlotItemHandler(inventory, 0, 80, 42)));
    }

    @Override
    public int getInventorySlots() {
        return 1;
    }

    public int getBurnTime() {
        return burnData.get(0);
    }

    public int getMaxBurnTime() {
        return this.burnData.get(1);
    }

    public float getScaledBurnTime() {
        return (float) getBurnTime() / getMaxBurnTime();
    }
}
