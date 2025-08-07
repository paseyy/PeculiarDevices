package com.pasey.peculiardevices.worldgen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.registration.PDBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class PDConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_LITHIUM_ORE_KEY = registerKey("lithium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_BARBERTONITE_ORE_KEY = registerKey("barbertonite_ore");


    public static void  bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest replacesStone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest replacesDeepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldLithiumOres = List.of(
                OreConfiguration.target(replacesStone, PDBlocks.LITHIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(replacesDeepslate, PDBlocks.LITHIUM_ORE.get().defaultBlockState())
        );
        register(context, OVERWORLD_LITHIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldLithiumOres, 10, 0.3f));

        List<OreConfiguration.TargetBlockState> overworldBarbertoniteOres = List.of(
                OreConfiguration.target(replacesStone, PDBlocks.BARBERTONITE_ORE.get().defaultBlockState()),
                OreConfiguration.target(replacesDeepslate, PDBlocks.BARBERTONITE_ORE.get().defaultBlockState())
        );
        register(context, OVERWORLD_BARBERTONITE_ORE_KEY, Feature.ORE, new OreConfiguration(overworldBarbertoniteOres, 5, 0.9f));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(PeculiarDevices.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key,
                                                                                          F feature,
                                                                                          FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
