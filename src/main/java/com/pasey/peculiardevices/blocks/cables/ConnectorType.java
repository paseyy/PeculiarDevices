// Credit to McJty https://www.mcjty.eu/docs/1.20/ep5 (08/08/2025)
package com.pasey.peculiardevices.blocks.cables;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;

public enum ConnectorType implements StringRepresentable {
    NONE,
    CABLE,
    BLOCK;

    public static final ConnectorType[] VALUES = values();

    @Override
    @Nonnull
    public String getSerializedName() {
        return toString().toLowerCase();
    }
}