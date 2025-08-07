package com.pasey.peculiardevices.datagen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blocks.base.BaseDeviceBlock;
import com.pasey.peculiardevices.registration.PDBlocks;
import com.pasey.peculiardevices.tags.PDTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class PDBlockTagsProvider extends BlockTagsProvider {
    public PDBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PeculiarDevices.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        Block[] deviceBlocks = PDBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> block instanceof BaseDeviceBlock)
                .toArray(Block[]::new);

        // generics
        tag(PDTags.Blocks.PD_DEVICES)
                .add(deviceBlocks)
                .add(PDBlocks.GEO_PIPE.get());

        tag(PDTags.Blocks.PD_ORES)
                .add(PDBlocks.LITHIUM_ORE.get())
                .add(PDBlocks.BARBERTONITE_ORE.get())
        ;


        // specifics
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(PDTags.Blocks.PD_DEVICES)
                .addTag(PDTags.Blocks.PD_ORES)
        ;

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(PDBlocks.LITHIUM_ORE.get())
        ;

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(PDBlocks.BARBERTONITE_ORE.get())
        ;

        tag(Tags.Blocks.ORE_RATES_SINGULAR)
                .add(PDBlocks.LITHIUM_ORE.get())
                .add(PDBlocks.BARBERTONITE_ORE.get())
        ;

        tag(Tags.Blocks.ORES)
                .addTag(PDTags.Blocks.PD_ORES)
        ;

        tag(Tags.Blocks.ORES_IN_GROUND_STONE)
                .add(PDBlocks.LITHIUM_ORE.get())
                .add(PDBlocks.BARBERTONITE_ORE.get())
        ;


    }
}

