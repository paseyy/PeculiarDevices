package com.pasey.peculiardevices.blocks.devices;

import com.pasey.peculiardevices.blocks.base.BaseDeviceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public class GrimeDynamo extends BaseDeviceBlock {
    public GrimeDynamo() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(1.0f)
                .sound(SoundType.METAL)
                .noOcclusion()
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return null;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = super.getStateForPlacement(pContext);
        if (state == null)
            return null;

        boolean isOnDirt = pContext.getLevel().getBlockState(pContext.getClickedPos().below()).is(Blocks.DIRT);
        return state.setValue(POWERED, isOnDirt);
    }
}
