package com.pasey.peculiardevices.blockentities;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blockentities.base.ProcessorBlockEntity;
import com.pasey.peculiardevices.blockentities.util.CustomEnergyStorage;
import com.pasey.peculiardevices.menu.VibratoryMillMenu;
import com.pasey.peculiardevices.recipe.MillingRecipe;
import com.pasey.peculiardevices.registration.PDBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class VibratoryMillBlockEntity extends ProcessorBlockEntity<MillingRecipe> {
    public static final int INVENTORY_SLOTS = 4;

    public VibratoryMillBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PDBlockEntities.VIBRATORY_MILL_BE.get(), pPos, pBlockState, INVENTORY_SLOTS, new int[] {0}, new int[] {1, 2, 3},
                new CustomEnergyStorage(10000, 1000, 1000, 0));
    }

    @Override
    @Nullable
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new VibratoryMillMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block." + PeculiarDevices.MODID + ".vibratory_mill");
    }

    @Override
    protected RecipeType<MillingRecipe> getRecipeType() {
        return MillingRecipe.Type.INSTANCE;
    }
}
