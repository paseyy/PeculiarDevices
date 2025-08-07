package com.pasey.peculiardevices.menu.base;

import com.pasey.peculiardevices.blockentities.base.ProcessorBlockEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class ProcessorMenu<T extends ProcessorBlockEntity<?>> extends DeviceMenu<T> {
    protected final ContainerData progressData;

    protected ProcessorMenu(@Nullable MenuType<?> pMenuType, Inventory playerInv, int pContainerId, T pBlockEntity, ContainerData progressData) {
        super(pMenuType, pContainerId, pBlockEntity);
        this.progressData = progressData;

        checkContainerSize(playerInv, getInventorySlots());
        addDataSlots(progressData);
    }

    @Override
    public T getBlockEntity() {
        return blockEntity;
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
