// Credit to McJty https://github.com/McJty/Tut4_3Power/blob/main/src/main/java/com/mcjty/tutpower/cables/blocks/CableBlockEntity.java (08/08/2025)
//MIT License
//
//        Copyright (c) 2023 McJty
//
//        Permission is hereby granted, free of charge, to any person obtaining a copy
//        of this software and associated documentation files (the "Software"), to deal
//        in the Software without restriction, including without limitation the rights
//        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//        copies of the Software, and to permit persons to whom the Software is
//        furnished to do so, subject to the following conditions:
//
//        The above copyright notice and this permission notice shall be included in all
//        copies or substantial portions of the Software.
//
//        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//        SOFTWARE.

package com.pasey.peculiardevices.blockentities.cables;

import com.pasey.peculiardevices.blockentities.base.TickableBlockEntity;
import com.pasey.peculiardevices.blockentities.util.CustomEnergyStorage;
import com.pasey.peculiardevices.blockentities.util.EnergyStorageParams;
import com.pasey.peculiardevices.registration.PDBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class CableBlockEntity extends BlockEntity implements TickableBlockEntity {
    private final CustomEnergyStorage energyStorage = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyOptional = LazyOptional.of(() -> energyStorage);

    // Keep track of all the energy output devices that are connected to this cable network
    private Set<BlockPos> outputs = null;

    public CableBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(PDBlockEntities.CABLE_BE.get(), pos, state);
    }


    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        if (energyStorage.getEnergyStored() > 0) {
            // Only do something if we have energy
            checkOutputs();
            if (!outputs.isEmpty()) {
                // Distribute energy over all outputs
                int amount = energyStorage.getEnergyStored() / outputs.size();
                for (BlockPos p : outputs) {
                    BlockEntity te = level.getBlockEntity(p);
                    if (te != null) {
                        te.getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
                            if (handler.canReceive()) {
                                int received = handler.receiveEnergy(amount, false);
                                energyStorage.extractEnergy(received, false);
                            }
                        });
                    }
                }
            }
        }
    }

    // This function will cache all outputs for this cable network. It will do this
    // by traversing all cables connected to this cable and then check for all energy
    // receivers around those cables.
    private void checkOutputs() {
        if (level == null) return;
        if (outputs == null) {
            outputs = new HashSet<>();
            traverse(worldPosition, cable -> {
                // Check for all energy receivers around this position (ignore cables)
                for (Direction direction : Direction.values()) {
                    BlockPos p = cable.getBlockPos().relative(direction);
                    BlockEntity be = level.getBlockEntity(p);
                    if (be != null && !(be instanceof CableBlockEntity)) {
                        be.getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
                            if (handler.canReceive()) {
                                outputs.add(p);
                            }
                        });
                    }
                }
            });
        }
    }

    public void markDirty() {
        traverse(worldPosition, cable -> cable.outputs = null);
    }

    // This is a generic function that will traverse all cables connected to this cable
    // and call the given consumer for each cable.
    private void traverse(BlockPos pos, Consumer<CableBlockEntity> consumer) {
        Set<BlockPos> traversed = new HashSet<>();
        traversed.add(pos);
        consumer.accept(this);
        traverse(pos, traversed, consumer);
    }

    private void traverse(BlockPos pos, Set<BlockPos> traversed, Consumer<CableBlockEntity> consumer) {
        for (Direction direction : Direction.values()) {
            BlockPos p = pos.relative(direction);
            if (!traversed.contains(p)) {
                traversed.add(p);
                if (level != null && level.getBlockEntity(p) instanceof CableBlockEntity cable) {
                    consumer.accept(cable);
                    cable.traverse(p, traversed, consumer);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Energy", energyStorage.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }
    }

    @Nonnull
    private CustomEnergyStorage createEnergyStorage() {
        return new CustomEnergyStorage(
                new EnergyStorageParams(1000, 100, 100, 0),
                this
        );
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyOptional.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }
}
