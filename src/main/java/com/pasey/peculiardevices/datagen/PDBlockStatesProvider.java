package com.pasey.peculiardevices.datagen;

import com.google.gson.JsonObject;
import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blocks.GeoPipe;
import com.pasey.peculiardevices.client.model.CableModelLoader;
import com.pasey.peculiardevices.registration.PDBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PDBlockStatesProvider extends BlockStateProvider {
    private final ExistingFileHelper existingFileHelper;

    public PDBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PeculiarDevices.MODID, exFileHelper);
        existingFileHelper = exFileHelper;
    }


    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(PDBlocks.GEO_ENERGY_CELL.get(), state ->
                models().orientable("geo_energy_cell",
                        modLoc("block/geo_energy_cell_side"),
                        modLoc("block/geo_energy_cell_front"),
                        modLoc("block/geo_energy_cell_side")));
        horizontalBlock(PDBlocks.GEO_GENERATOR.get(), state ->
                models().getExistingFile(modLoc("block/geo_generator")));
        horizontalBlock(PDBlocks.VIBRATORY_MILL.get(), state ->
                models().getExistingFile(modLoc("block/vibratory_mill")));

        // registerGrimeDynamo();
        registerHorizontalPoweredBlock(PDBlocks.GRIME_DYNAMO.get(), "grime_dynamo");

        // stackables (only geo pipe?)
        downStackableBlock(PDBlocks.GEO_PIPE.get());

        // cables
        registerCable();

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

    private void registerHorizontalPoweredBlock(Block block, String blockName) {
        BlockModelBuilder modelOn = models()
                .withExistingParent(blockName + "_on", modLoc("block/" + blockName))
                .texture("0", modLoc("block/" + blockName + "_on"));
        BlockModelBuilder modelOff = models()
                .withExistingParent(blockName + "_off", modLoc("block/" + blockName))
                .texture("0", modLoc("block/" + blockName + "_off"));

        horizontalBlock(block,
                state -> state.getValue(BlockStateProperties.POWERED) ? modelOn : modelOff
        );
    }

    private void registerCable() {
        BlockModelBuilder model = models().getBuilder("cable")
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((builder, helper) -> new CableLoaderBuilder(CableModelLoader.GENERATOR_LOADER, builder, helper))
                .end();
        simpleBlock(PDBlocks.CABLE.get(), model);
    }

    public static class CableLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {
        public CableLoaderBuilder(ResourceLocation loader, BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(loader, parent, existingFileHelper);
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            return super.toJson(json);
        }
    }
}
