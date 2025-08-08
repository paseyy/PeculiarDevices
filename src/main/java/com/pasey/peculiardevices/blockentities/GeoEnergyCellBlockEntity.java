package com.pasey.peculiardevices.blockentities;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blockentities.base.DeviceBlockEntity;
import com.pasey.peculiardevices.blockentities.util.EnergyStorageParams;
import com.pasey.peculiardevices.menu.GeoEnergyCellMenu;
import com.pasey.peculiardevices.registration.PDBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.pasey.peculiardevices.blockentities.util.SidedItemHandler.getFacing;

public class GeoEnergyCellBlockEntity extends DeviceBlockEntity {

    public GeoEnergyCellBlockEntity(BlockPos pos, BlockState state) {
        super(PDBlockEntities.GEO_ENERGY_CELL_BE.get(), pos, state, 1,
                new EnergyStorageParams(100_000, 100,100, 0));
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide()) {
            return;
        }

       distributeEnergy();
    }

    private void distributeEnergy() {
        if (level == null) return;

        if (getEnergyStorage().getEnergyStored() <= 0) {
            return;
        }

        BlockEntity be = level.getBlockEntity(this.getBlockPos().relative(getFacing(this.getBlockState())));
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

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] {};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, Direction side) {
        return false;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return getEnergyOptional().cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block." + PeculiarDevices.MODID + ".geo_energy_cell");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new GeoEnergyCellMenu(i, inventory, this, this.energyContainerData);
    }
}
