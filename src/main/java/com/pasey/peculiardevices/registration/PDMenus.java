package com.pasey.peculiardevices.registration;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.menu.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PDMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, PeculiarDevices.MODID);

    public static final RegistryObject<MenuType<GeoElectricFurnaceMenu>> GEO_ELECTRIC_FURNACE_MENU =
            MENU_TYPES.register("geo_electric_furnace_menu", () -> IForgeMenuType.create(GeoElectricFurnaceMenu::new));

    public static final RegistryObject<MenuType<GeoEnergyCellMenu>> GEO_ENERGY_CELL_MENU =
            MENU_TYPES.register("geo_energy_cell_menu", () -> IForgeMenuType.create(GeoEnergyCellMenu::new));

    public static final RegistryObject<MenuType<GeoGeneratorMenu>> GEO_GENERATOR_MENU =
            MENU_TYPES.register("geo_generator_menu", () -> IForgeMenuType.create(GeoGeneratorMenu::new));

    public static final RegistryObject<MenuType<GrimeDynamoMenu>> GRIME_DYNAMO_MENU =
            MENU_TYPES.register("grime_dynamo_menu", () -> IForgeMenuType.create(GrimeDynamoMenu::new));

    public static final RegistryObject<MenuType<VibratoryMillMenu>> VIBRATORY_MILL_MENU =
            MENU_TYPES.register("vibratory_mill_menu", () -> IForgeMenuType.create(VibratoryMillMenu::new));
}
