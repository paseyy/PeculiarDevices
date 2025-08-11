package com.pasey.peculiardevices.capabilities;

import com.pasey.peculiardevices.items.base.EnergyItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

// --- Inner Capability Provider ---
public class EnergyCapabilityProvider implements ICapabilityProvider, IEnergyStorage {
    private final ItemStack stack;
    private final int capacity;
    private final int maxReceive;
    private final int maxExtract;
    private final LazyOptional<IEnergyStorage> holder;

    public EnergyCapabilityProvider(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
        this.stack = stack;
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.holder = LazyOptional.of(() -> this);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return holder.cast();
        }
        return LazyOptional.empty();
    }

    // IEnergyStorage implementation
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = EnergyItem.getEnergy(stack);
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            EnergyItem.setEnergy(stack, energy + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = EnergyItem.getEnergy(stack);
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            EnergyItem.setEnergy(stack, energy - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return EnergyItem.getEnergy(stack);
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }
}