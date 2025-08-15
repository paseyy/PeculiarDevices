package com.pasey.peculiardevices.blockentities;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blockentities.base.GeneratorBlockEntity;
import com.pasey.peculiardevices.blockentities.util.EnergyStorageParams;
import com.pasey.peculiardevices.menu.GrimeDynamoMenu;
import com.pasey.peculiardevices.registration.PDBlockEntities;
import com.pasey.peculiardevices.tags.PDTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Map;

import static com.pasey.peculiardevices.blockentities.util.SidedItemHandler.contains;
import static com.pasey.peculiardevices.blockentities.util.SidedItemHandler.getRight;

public class GrimeDynamoBlockEntity extends GeneratorBlockEntity {
    public static final int[] FUEL_SLOTS = {0};
    public static final int INVENTORY_SLOTS = 1;

    public static Map<Item, Integer> BURN_TIMES = Map.ofEntries(
            Map.entry(Items.DIRT, 200),
            Map.entry(Items.PODZOL, 300),
            Map.entry(Items.MUD, 400),
            Map.entry(Items.SOUL_SOIL, 600),
            Map.entry(Items.MYCELIUM, 100),
            Map.entry(Items.COARSE_DIRT, 100)
    );

    public GrimeDynamoBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PDBlockEntities.GRIME_DYNAMO_BE.get(), pPos, pBlockState, INVENTORY_SLOTS, FUEL_SLOTS,
                new EnergyStorageParams(10000, 0, 1000, 0));
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide()) {
            return;
        }

        super.tick();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        BlockState state = this.getBlockState();
        if (side == getRight(state) || side == Direction.UP) return FUEL_SLOTS; // input
        return new int[] {0}; // default fallback
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        BlockState state = this.getBlockState();
        return contains(FUEL_SLOTS, slot) && (side == Direction.UP || side == getRight(state));
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, Direction side) {
        return false;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block." + PeculiarDevices.MODID + ".grime_dynamo");
    }

    @Override
    protected boolean requiresFuel() {
        return true;
    }

    @Override
    protected int[] getFuelSlots() {
        return new int[0];
    }

    @Override
    protected boolean canBurn() {
        return getInventory().getStackInSlot(0).is(PDTags.Items.PD_GRIME_DYNAMO_BURNABLES);
    }

    @Override
    protected int getFuelBurnTime() {
        Item item = getInventory().getStackInSlot(0).getItem();
        return BURN_TIMES.getOrDefault(item, 0);
    }

    @Override
    protected void consumeFuel() {
        getInventory().extractItem(0, 1, false);
    }

    @Override
    public int getEnergyGenPerTick() {
        return 1;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GrimeDynamoMenu(pContainerId, pPlayerInventory, this, this.energyContainerData, this.burnData);
    }
}
