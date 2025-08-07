package com.pasey.peculiardevices.worldgen;

import com.pasey.peculiardevices.PeculiarDevices;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class PDPlacedFeatures {
    public static final ResourceKey<PlacedFeature> LITHIUM_ORE_PLACED_KEY = registerKey("lithium_ore_placed");
    public static final ResourceKey<PlacedFeature> BARBERTONITE_ORE_PLACED_KEY = registerKey("barbertonite_ore_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, LITHIUM_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(PDConfiguredFeatures.OVERWORLD_LITHIUM_ORE_KEY),
                PDOrePlacement.commonOrePlacement(20, HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-32),
                                VerticalAnchor.absolute(128))
                )
        );

        register(context, BARBERTONITE_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(PDConfiguredFeatures.OVERWORLD_BARBERTONITE_ORE_KEY),
                PDOrePlacement.commonOrePlacement(10, HeightRangePlacement.triangle(
                        VerticalAnchor.absolute(16),
                        VerticalAnchor.absolute(64))
                )
        );
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(PeculiarDevices.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
