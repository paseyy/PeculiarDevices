package com.pasey.peculiardevices.blockentities.base;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blockentities.util.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

public abstract class GeneratorBlockEntity extends DeviceBlockEntity {
    private int[] fuelSlots;
    private int burnTime = 0, maxBurnTime = 0;

    public GeneratorBlockEntity(BlockEntityType<? extends DeviceBlockEntity> pType, BlockPos pPos, BlockState pBlockState, int inventorySlots, CustomEnergyStorage energyStorage) {
        super(pType, pPos, pBlockState, inventorySlots, energyStorage);
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide()) {
            return;
        }

        distributeEnergy();
        generateEnergy();
    }

    private void distributeEnergy() {
        // Credit to McJty for the energy distribution logic: https://www.mcjty.eu/docs/1.20/ep4 (08/07/2025)
        // Check all sides of the block and send energy if that block supports the energy capability
        for (Direction direction : Direction.values()) {
            if (getEnergyStorage().getEnergyStored() <= 0) {
                return;
            }
            BlockEntity be = level.getBlockEntity(getBlockPos().relative(direction));
            if (be != null) {
                be.getCapability(ForgeCapabilities.ENERGY).map(e -> {
                    if (e.canReceive()) {
                        int received = e.receiveEnergy(Math.min(getEnergyStorage().getEnergyStored(), getEnergyStorage().getMaxExtract()), false);
                        getEnergyStorage().extractEnergy(received, false);
                        setChanged();
                        return received;
                    }
                    return 0;
                });
            }
        }
    }

    private void generateEnergy() {
        if (getEnergyStorage().getEnergyStored() < getEnergyStorage().getMaxEnergyStored()) {
            if (requiresFuel()) {
                if (burnTime <= 0) {
                    if (canBurn()) {
                        burnTime = maxBurnTime = getFuelBurnTime();
                        consumeFuel();
                    }
                } else { // Currently burning fuel
                    burnTime--;
                    getEnergyStorage().addEnergy(getEnergyGenPerTick());
                    sendUpdate();
                }
            } else { // Does not require fuel
                getEnergyStorage().addEnergy(getEnergyGenPerTick());
                sendUpdate();
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

        CompoundTag data = pTag.getCompound(PeculiarDevices.MODID);

        if (data.contains("burnTime", Tag.TAG_INT)) {
            burnTime = data.getInt("burnTime");
        }
        if (data.contains("maxBurnTime", Tag.TAG_INT)) {
            maxBurnTime = data.getInt("maxBurnTime");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);

        var data = new CompoundTag();
        data.putInt("burnTime", burnTime);
        data.putInt("maxBurnTime", maxBurnTime);
    }

    protected abstract boolean requiresFuel();

    protected abstract int[] getFuelSlots();

    protected abstract boolean canBurn();

    protected abstract int getFuelBurnTime();

    protected abstract void consumeFuel();

    public abstract int getEnergyGenPerTick();

}
