package com.pasey.peculiardevices.blockentities.base;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blockentities.util.EnergyStorageParams;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

public abstract class GeneratorBlockEntity extends DeviceBlockEntity {
    private int[] fuelSlots;
    private int burnTime = 0, maxBurnTime = 0;

    public final ContainerData burnData = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> burnTime;
                case 1 -> maxBurnTime;
                default -> throw new IndexOutOfBoundsException("Invalid index: " + pIndex);
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> burnTime = pValue;
                case 1 -> maxBurnTime = pValue;
                default -> throw new IndexOutOfBoundsException("Invalid index: " + pIndex);
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public GeneratorBlockEntity(BlockEntityType<? extends DeviceBlockEntity> pType, BlockPos pPos, BlockState pBlockState, int inventorySlots, EnergyStorageParams energyStorage) {
        this(pType, pPos, pBlockState, inventorySlots, new int[] {0}, energyStorage);
    }

    public GeneratorBlockEntity(BlockEntityType<? extends DeviceBlockEntity> pType, BlockPos pPos, BlockState pBlockState, int inventorySlots, int[] fuelSlots, EnergyStorageParams energyStorage) {
        super(pType, pPos, pBlockState, inventorySlots, energyStorage);

        this.fuelSlots = fuelSlots;
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

        CompoundTag data = pTag.contains(PeculiarDevices.MODID, Tag.TAG_COMPOUND)
                ? pTag.getCompound(PeculiarDevices.MODID)
                : new CompoundTag();

        data.putInt("burnTime", burnTime);
        data.putInt("maxBurnTime", maxBurnTime);
        pTag.put(PeculiarDevices.MODID, data);
    }

    public int getBurnTime() {
        return burnTime;
    }

    protected abstract boolean requiresFuel();

    protected abstract int[] getFuelSlots();

    protected abstract boolean canBurn();

    protected abstract int getFuelBurnTime();

    protected abstract void consumeFuel();

    public abstract int getEnergyGenPerTick();
}
