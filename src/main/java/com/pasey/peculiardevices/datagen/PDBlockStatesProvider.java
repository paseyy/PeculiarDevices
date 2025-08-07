package com.pasey.peculiardevices.datagen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blocks.GeoPipe;
import com.pasey.peculiardevices.registration.PDBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PDBlockStatesProvider extends BlockStateProvider {
    private final ExistingFileHelper existingFileHelper;

    public PDBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PeculiarDevices.MODID, exFileHelper);
        existingFileHelper = exFileHelper;
    }


    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(PDBlocks.GEO_GENERATOR.get(), state ->
                models().getExistingFile(modLoc("block/geo_generator")));
        horizontalBlock(PDBlocks.VIBRATORY_MILL.get(), state ->
                models().getExistingFile(modLoc("block/vibratory_mill")));

        downStackableBlock(PDBlocks.GEO_PIPE.get());

        // simple blocks
        simpleBlockWithItem(PDBlocks.BARBERTONITE_ORE.get(), cubeAll(PDBlocks.BARBERTONITE_ORE.get()));
        simpleBlockWithItem(PDBlocks.GEO_DEVICE_FRAME.get(), cubeAll(PDBlocks.GEO_DEVICE_FRAME.get()));
        simpleBlockWithItem(PDBlocks.LITHIUM_ORE.get(), cubeAll(PDBlocks.LITHIUM_ORE.get()));
    }

    private void downStackableBlock(Block block) {
        getVariantBuilder(block).forAllStates(state -> {
            Boolean is_bottom = state.getValue(GeoPipe.BOTTOM);
            return ConfiguredModel.builder()
                   .modelFile(
                           new ModelFile.ExistingModelFile(modLoc(is_bottom ? "block/geo_pipe_end" : "block/geo_pipe"),
                           existingFileHelper)
                   )
                   .build();
        });
    }
}
