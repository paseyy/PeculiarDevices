package com.pasey.peculiardevices.blockentities.util;

import com.pasey.peculiardevices.blockentities.base.DeviceBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SidedItemHandler {
    public static LazyOptional<IItemHandler>[] createSidedHandlers(DeviceBlockEntity be) {
        @SuppressWarnings("unchecked")
        LazyOptional<IItemHandler>[] handlers = new LazyOptional[6];
        for (Direction dir : Direction.values()) {
            handlers[dir.ordinal()] = LazyOptional.of(() -> new IItemHandler() {
                @Override
                public int getSlots() {
                    return be.getInventory().getSlots();
                }

                @Override
                public @NotNull ItemStack getStackInSlot(int slot) {
                    return be.getStackInSlot(slot);
                }

                @Override
                public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                    if (!be.canPlaceItemThroughFace(slot, stack, dir)) return stack;
                    return be.getInventory().insertItem(slot, stack, simulate);
                }

                @Override
                public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                    if (!be.canTakeItemThroughFace(slot, dir)) return ItemStack.EMPTY;
                    return be.getInventory().extractItem(slot, amount, simulate);
                }

                @Override
                public int getSlotLimit(int slot) {
                    return be.getInventory().getSlotLimit(slot);
                }

                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                    return be.canPlaceItemThroughFace(slot, stack, dir);
                }
            });
        }
        return handlers;
    }

    public static Direction getFacing(BlockState state) {
        // If block has a "FACING" property:
        if (state.hasProperty(BlockStateProperties.FACING)) {
            return state.getValue(BlockStateProperties.FACING);
        }

        // If block has a "HORIZONTAL_FACING" property:
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        }

        // Default if no facing property
        return Direction.NORTH;
    }

    public static Direction getBack(BlockState state) { return getRight(getRight(getFacing(state))); }

    public static Direction getRight(BlockState state) {
        return getRight(getFacing(state));
    }

    public static Direction getRight(Direction facing) {
        if (facing.getAxis().isHorizontal()) {
            return facing.getClockWise();
        } else {
            // For vertical directions (UP, DOWN), default to NORTH's right (EAST) or handle separately
            return Direction.EAST;
        }
    }

    public static Direction getLeft(BlockState state) {
        return getLeft(getFacing(state));
    }
    public static Direction getLeft(Direction facing) {
        if (facing.getAxis().isHorizontal()) {
            return facing.getCounterClockWise();
        } else {
            return Direction.EAST;
        }
    }

    public static boolean contains(int[] arr, int x) {
        return Arrays.stream(arr).anyMatch(i -> i == x);
    }
}

