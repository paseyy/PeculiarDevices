package com.pasey.peculiardevices.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public class GeoDeviceFrame extends Block {

    public GeoDeviceFrame() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .strength(1.0f, 1.0f)
                .sound(SoundType.METAL)

        );
    }
}
