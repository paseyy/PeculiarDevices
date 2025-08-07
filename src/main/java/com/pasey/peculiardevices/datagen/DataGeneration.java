package com.pasey.peculiardevices.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataGeneration {
    public static void generate(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new PDBlockStatesProvider(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new PDItemModelsProvider(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new PDLangProvider(packOutput, "en_us"));

        PDBlockTagsProvider pdBlockTags = new PDBlockTagsProvider(packOutput, lookupProvider, event.getExistingFileHelper());

        generator.addProvider(event.includeServer(), pdBlockTags);
        generator.addProvider(event.includeServer(), new PDRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(),
                new PDItemTagsProvider(packOutput, lookupProvider, pdBlockTags, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(PDLootTablesProvider::new, LootContextParamSets.BLOCK))));
        generator.addProvider(event.includeServer(), new PDWorldGenProvider(packOutput, lookupProvider));
    }
}
