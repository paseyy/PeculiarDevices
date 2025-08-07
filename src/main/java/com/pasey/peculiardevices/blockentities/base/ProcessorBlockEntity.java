package com.pasey.peculiardevices.blockentities.base;

import com.pasey.peculiardevices.blockentities.util.CustomEnergyStorage;
import com.pasey.peculiardevices.recipe.base.BaseRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Predicate;

import static com.pasey.peculiardevices.blockentities.util.SidedItemHandler.*;
import static com.pasey.peculiardevices.blockentities.util.SidedItemHandler.getLeft;

public abstract class ProcessorBlockEntity<T extends BaseRecipe<T>> extends DeviceBlockEntity {
    protected final int[] inputSlots;
    protected final int[] outputSlots;

    protected int progress = 0;
    protected int maxProgress = 0;
    protected final ContainerData data;


    public ProcessorBlockEntity(BlockEntityType<? extends DeviceBlockEntity> pType, BlockPos pPos, BlockState pBlockState, int inventorySlots, int[] inputSlots, int[] outputSlots, CustomEnergyStorage energyStorage) {
        super(pType, pPos, pBlockState, inventorySlots, energyStorage);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> ProcessorBlockEntity.this.progress;
                    case 1 -> ProcessorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> ProcessorBlockEntity.this.progress = pValue;
                    case 1 -> ProcessorBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick() {
        if(level != null && level.isClientSide())
            return;

        if(hasRecipe()) {
            if(maxProgress == 0) {
                maxProgress = getRecipeMaxProgress();
            }

            progress++;

            if (progress >= maxProgress) {
                craftItem();
                progress = 0;
                maxProgress = 0;
            }

            setChanged();
        }
        else {
            progress = 0;
            setChanged();
        }
    }


    private void craftItem() {
        Optional<T> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            System.err.println("Empty recipe despite successful check!");
            return;
        }

        NonNullList<Ingredient> inputs = recipe.get().getIngredients();
        for (Ingredient ingredient : inputs) {
            for (int slot : this.getInputSlots()) {
                if (ingredient.test(getStackInSlot(slot))) {
                    getInventory().extractItem(slot, ingredient.getItems()[0].getCount(), false);
                    break;
                }

                System.err.println("Necessary ingredients for recipe have not been found!");
            }
        }

        boolean success = false;
        NonNullList<ItemStack> outputs = recipe.get().getOutputs();
        for (ItemStack outputItem : outputs) {
            for (int slot : this.getOutputSlots()) {
                ItemStack inventoryItem = getInventory().getStackInSlot(slot);
                if (outputItem.getItem() == inventoryItem.getItem()
                        && outputItem.getCount() + inventoryItem.getCount() < outputItem.getMaxStackSize())
                {
                    setStackInSlot(slot, new ItemStack(outputItem.getItem(),
                            getStackInSlot(slot).getCount() + outputItem.getCount()));
                    success = true;
                    break;
                }
            }

            if(success)
                break;

            // Item is not present in inventory slots, move it to empty slot
            for (int slot : this.getOutputSlots()) {
                if (getStackInSlot(slot).isEmpty()) {
                    setStackInSlot(slot, new ItemStack(outputItem.getItem(), outputItem.getCount()));
                    success = true;
                    break;
                }
            }

            if(!success)
                System.err.println("Could not craft item despite there being room!");
        }
    }

    private boolean hasRecipe() {
        Optional<T> recipe = getCurrentRecipe();

        if(recipe.isEmpty())
            return false;

        NonNullList<ItemStack> result = recipe.get().getOutputs();
        return canInsertItemIntoOutput(result);
    }

    private int getRecipeMaxProgress() {
        Optional<T> recipe = getCurrentRecipe();

        return recipe.orElseThrow().getCraftingTime();
    }

    private boolean canInsertItemIntoOutput(NonNullList<ItemStack> result) {
        NonNullList<Integer> outputIndices = getOutputSlots();
        long emptyOutputSlots = outputIndices.stream().filter(i -> getStackInSlot(i).isEmpty()).count();

        if (result.size() <= emptyOutputSlots) {
            return true;
        }

        // We compare every resulting item with every filled output slot. If a result item
        // cannot be added to any output slot, we add 1 to the number of new (not yet present) items!
        // If we have more new items than free output slots, we cannot insert.
        int cannot_insert = 0;
        for (ItemStack resultItem : result)
        {
            for (ItemStack presentItem : outputIndices
                .stream()
                .map(this::getStackInSlot)
                .filter(Predicate.not(ItemStack::isEmpty))
                .toList())
            {
                if (resultItem.equals(presentItem)
                        && presentItem.getCount() + resultItem.getCount() < presentItem.getMaxStackSize())
                {
                    break;
                }
            }
            cannot_insert += 1;
        }

        return cannot_insert <= emptyOutputSlots;
    }

    private Optional<T> getCurrentRecipe() {
        SimpleContainer container = new SimpleContainer(getInventory().getSlots());

        for (int i = 0; i < getInventory().getSlots(); i++) {
            container.setItem(i, getStackInSlot(i));
        }

        assert this.level != null;
        return this.level.getRecipeManager().getRecipeFor(getRecipeType(), container, level);
    }

    protected abstract RecipeType<T> getRecipeType();

    protected NonNullList<Integer> getInputSlots() {
        NonNullList<Integer> result = NonNullList.create();
        for (int slot : inputSlots) {
            result.add(slot);
        }
        return result;
    }

    protected NonNullList<Integer> getOutputSlots() {
        NonNullList<Integer> result = NonNullList.create();
        for (int slot : outputSlots) {
            result.add(slot);
        }
        return result;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        BlockState state = this.getBlockState();
        if (side == getRight(state) || side == Direction.UP) return inputSlots; // input
        if (side == getLeft(state) || side == Direction.DOWN) return outputSlots; // output
        return new int[] {0}; // default fallback
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        BlockState state = this.getBlockState();
        return contains(inputSlots, slot) && (side == Direction.UP || side == getRight(state));
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, Direction side) {
        BlockState state = this.getBlockState();
        return contains(outputSlots, slot) && (side == Direction.DOWN || side == getLeft(state));
    }

    public int getProgress() {
        return progress;
    }
    
    public int getMaxProgress() {
        return maxProgress;
    }
}
