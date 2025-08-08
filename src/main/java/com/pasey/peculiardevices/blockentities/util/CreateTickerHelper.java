package com.pasey.peculiardevices.blockentities.util;

import com.pasey.peculiardevices.blockentities.base.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CreateTickerHelper {
    public static <E extends BlockEntity> BlockEntityTicker<E> createTickerHelper(
            BlockEntityType<E> blockEntityType,
            BlockEntityType<? extends BlockEntity> expectedType) {

        // Verify the blockEntityType matches the expected type
        return blockEntityType == expectedType ? (level, pos, state, entity) -> {
            if (entity instanceof TickableBlockEntity tickableEntity) {
                tickableEntity.tick();
            }
        } : null;
    }
}
