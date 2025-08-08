package com.pasey.peculiardevices.registration;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.blockentities.GeoEnergyCellBlockEntity;
import com.pasey.peculiardevices.blockentities.GeoGeneratorBlockEntity;
import com.pasey.peculiardevices.blockentities.VibratoryMillBlockEntity;
import com.pasey.peculiardevices.blockentities.cables.CableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PDBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PeculiarDevices.MODID);


    public static final RegistryObject<BlockEntityType<CableBlockEntity>> CABLE_BE =
            BLOCK_ENTITIES.register("cable_block_entity",
                    () -> BlockEntityType.Builder.of(CableBlockEntity::new,
                                    PDBlocks.CABLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<GeoEnergyCellBlockEntity>> GEO_ENERGY_CELL_BE =
            BLOCK_ENTITIES.register("geo_energy_cell_be",
                    () -> BlockEntityType.Builder.of(GeoEnergyCellBlockEntity::new,
                                    PDBlocks.GEO_ENERGY_CELL.get()).build(null));
    public static final RegistryObject<BlockEntityType<GeoGeneratorBlockEntity>> GEO_GENERATOR_BE =
            BLOCK_ENTITIES.register("geo_generator_be",
                    () -> BlockEntityType.Builder.of(GeoGeneratorBlockEntity::new,
                                    PDBlocks.GEO_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<VibratoryMillBlockEntity>> VIBRATORY_MILL_BE =
            BLOCK_ENTITIES.register("vibratory_mill_be",
                    () -> BlockEntityType.Builder.of(VibratoryMillBlockEntity::new,
                                    PDBlocks.VIBRATORY_MILL.get()).build(null));
}
