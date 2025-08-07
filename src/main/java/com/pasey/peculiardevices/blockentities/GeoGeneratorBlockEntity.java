package com.pasey.peculiardevices.blockentities;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blockentities.base.GeneratorBlockEntity;
import com.pasey.peculiardevices.blockentities.util.CustomEnergyStorage;
import com.pasey.peculiardevices.menu.GeoGeneratorMenu;
import com.pasey.peculiardevices.registration.PDBlockEntities;
import com.pasey.peculiardevices.registration.PDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.pasey.peculiardevices.blockentities.util.SidedItemHandler.getBack;

public class GeoGeneratorBlockEntity extends GeneratorBlockEntity {
    private int numConnectedPipes;
    private int pipeScanTimer = 0;
    private final int pipeScanMaxTimer = 100;

    public GeoGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PDBlockEntities.GEO_GENERATOR_BE.get(), pPos, pBlockState, 0,
                new CustomEnergyStorage(20000, 0, 1000, 0));

    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
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
    public @NotNull Component getDisplayName() {
        return Component.translatable("block." + PeculiarDevices.MODID + ".geo_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new GeoGeneratorMenu(pContainerId, pPlayerInventory, this, this.energyContainerData);
    }

    @Override
    public void tick() {
        if (pipeScanTimer-- <= 0) {
            numConnectedPipes = countPipesBelow();
            pipeScanTimer = pipeScanMaxTimer;
        }

        super.tick();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY && side == getBack(getBlockState())) {
            return getEnergyOptional().cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected boolean requiresFuel() {
        return false;
    }

    @Override
    protected int[] getFuelSlots() {
        return null;
    }

    @Override
    protected boolean canBurn() {
        return true;
    }

    @Override
    protected int getFuelBurnTime() {
        return 0;
    }

    @Override
    protected void consumeFuel() {}

    @Override
    public int getEnergyGenPerTick() {
        return numConnectedPipes;
    }

    private int countPipesBelow() {
        int count = 0;
        BlockPos pos = getBlockPos().below();
        while (getLevel() != null && getLevel().getBlockState(pos).is(PDBlocks.GEO_PIPE.get())) {
            count++;
            pos = pos.below();
        }
        return count;
    }
}
