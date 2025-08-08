// Credit to TurtyWurty: https://github.com/DaRealTurtyWurty/1.20-Tutorial-Mod/blob/main/src/main/java/dev/turtywurty/tutorialmod/blockentity/util/CustomEnergyStorage.java
package com.pasey.peculiardevices.blockentities.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
    private final BlockEntity owner;

    public CustomEnergyStorage(EnergyStorageParams params, BlockEntity owner) {
        this(params.capacity, params.maxReceive, params.maxDistribute, params.energy, owner);
    }

    private CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, BlockEntity owner) {
        super(capacity, maxReceive, maxExtract, energy);
        this.owner = owner;
    }

    public void setEnergy(int energy) {
        if(energy < 0)
            energy = 0;
        if(energy > this.capacity)
            energy = this.capacity;

        this.energy = energy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            energy += energyReceived;
            owner.setChanged();
            if (owner.getLevel() != null && !owner.getLevel().isClientSide()) {
                owner.getLevel().sendBlockUpdated(owner.getBlockPos(), owner.getBlockState(), owner.getBlockState(), Block.UPDATE_ALL);
            }
        }

        return energyReceived;
    }

    public void addEnergy(int energy) {
        setEnergy(this.energy + energy);
    }

    public void removeEnergy(int energy) {
        setEnergy(this.energy - energy);
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }
}

