package com.pasey.peculiardevices.blockentities;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blockentities.base.DeviceBlockEntity;
import com.pasey.peculiardevices.blockentities.util.EnergyStorageParams;
import com.pasey.peculiardevices.menu.GeoElectricFurnaceMenu;
import com.pasey.peculiardevices.registration.PDBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

import static com.pasey.peculiardevices.blockentities.util.SidedItemHandler.*;
import static com.pasey.peculiardevices.blockentities.util.SidedItemHandler.getLeft;
import static com.pasey.peculiardevices.blocks.base.BaseDeviceBlock.POWERED;

public class GeoElectricFurnaceBlockEntity extends DeviceBlockEntity {
    protected final int inputSlot = 0;
    protected final int outputSlot = 1;

    protected int progress = 0;
    protected int maxProgress = 0;
    protected final ContainerData progressData;


    public GeoElectricFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PDBlockEntities.GEO_ELECRIC_FURNACE_BE.get(), pPos, pBlockState, 2,
                new EnergyStorageParams(10000, 1000, 0, 0));

        this.progressData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> GeoElectricFurnaceBlockEntity.this.progress;
                    case 1 -> GeoElectricFurnaceBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> GeoElectricFurnaceBlockEntity.this.progress = pValue;
                    case 1 -> GeoElectricFurnaceBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick() {
        if(level != null && level.isClientSide())
            return;

        boolean active = hasRecipe() && hasEnergy();

        if (active) {
            if (maxProgress == 0) {
                maxProgress = getRecipeMaxProgress();
            }
            progress++;

            if (progress >= maxProgress) {
                craftItem();
                consumeEnergy();
                progress = 0;
                maxProgress = 0;
                setChanged();
            }
        } else {
            progress = 0;
            setChanged();
        }

        BlockState current = getBlockState();
        if (current.getValue(POWERED) != active) {
            BlockState updated = current.setValue(POWERED, active);
            level.setBlock(getBlockPos(), updated, 3);
        }
    }

    protected boolean hasEnergy() {
        return getEnergyStorage().getEnergyStored() > getEnergyNeededForCrafting();
    }

    protected int getEnergyNeededForCrafting() {
        return 1000;
    }

    protected void consumeEnergy() {
        getEnergyStorage().removeEnergy(getEnergyNeededForCrafting());
    }

    private void craftItem() {
        if (level == null) return;

        Optional<? extends AbstractCookingRecipe> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            System.err.println("Empty recipe despite successful check!");
            return;
        }

        NonNullList<Ingredient> inputs = recipe.get().getIngredients();
        for (Ingredient ingredient : inputs) {
            if (ingredient.test(getStackInSlot(inputSlot))) {
                getInventory().extractItem(inputSlot, ingredient.getItems()[0].getCount(), false);
            }
        }

        boolean success = false;
        ItemStack result = recipe.get().getResultItem(level.registryAccess());
        ItemStack inventoryItem = getInventory().getStackInSlot(outputSlot);
        if (result.getItem() == inventoryItem.getItem()
                && result.getCount() + inventoryItem.getCount() < result.getMaxStackSize())
        {
            setStackInSlot(outputSlot, new ItemStack(result.getItem(),
                    getStackInSlot(outputSlot).getCount() + result.getCount()));
            success = true;
        }

        // Item is not present in inventory slots, move it to empty slot
        if (getStackInSlot(outputSlot).isEmpty()) {
            setStackInSlot(outputSlot, new ItemStack(result.getItem(), result.getCount()));
            success = true;
        }

        if(!success)
            System.err.println("Could not craft item despite there being room!");
    }

    private boolean hasRecipe() {
        if (level == null) return false;

        Optional<? extends AbstractCookingRecipe> recipe = getCurrentRecipe();

        if(recipe.isEmpty())
            return false;

        ItemStack result = recipe.get().getResultItem(level.registryAccess());
        return canInsertItemIntoOutput(result);
    }

    private int getRecipeMaxProgress() {
        Optional<? extends AbstractCookingRecipe> recipe = getCurrentRecipe();

        return recipe.orElseThrow().getCookingTime();
    }

    private boolean canInsertItemIntoOutput(ItemStack result) {
        if (getStackInSlot(outputSlot).isEmpty()) {
            return true;
        }

        if (getStackInSlot(outputSlot).getItem() != result.getItem()) {
            return false;
        }

        return getStackInSlot(outputSlot).getCount() + result.getCount() <= getStackInSlot(outputSlot).getMaxStackSize();
    }

    private Optional<? extends AbstractCookingRecipe> getCurrentRecipe() {
        SimpleContainer container = new SimpleContainer(getInventory().getSlots());

        for (int i = 0; i < getInventory().getSlots(); i++) {
            container.setItem(i, getStackInSlot(i));
        }

        assert this.level != null;
        return this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, level);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        BlockState state = this.getBlockState();
        if (side == getRight(state) || side == Direction.UP) return new int[] { inputSlot }; // input
        if (side == getLeft(state) || side == Direction.DOWN) return new int[] { outputSlot }; // output
        return new int[] {0}; // default fallback
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        BlockState state = this.getBlockState();
        return slot == inputSlot && (side == Direction.UP || side == getRight(state));
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, Direction side) {
        BlockState state = this.getBlockState();
        return slot == outputSlot && (side == Direction.DOWN || side == getLeft(state));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block." + PeculiarDevices.MODID + ".geo_electric_furnace");
    }

    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GeoElectricFurnaceMenu(pContainerId, pPlayerInventory, this, this.progressData);
    }
}
