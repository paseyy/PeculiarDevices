package com.pasey.peculiardevices.blocks.devices;

import com.pasey.peculiardevices.blockentities.GeoElectricFurnaceBlockEntity;
import com.pasey.peculiardevices.blocks.base.BaseDeviceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.ParametersAreNonnullByDefault;

public class GeoElectricFurnace extends BaseDeviceBlock {
    public GeoElectricFurnace() {
        super(Properties.of()
                .strength(1.0f)
                .requiresCorrectToolForDrops()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL));
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GeoElectricFurnaceBlockEntity(pPos, pState);
    }
}
