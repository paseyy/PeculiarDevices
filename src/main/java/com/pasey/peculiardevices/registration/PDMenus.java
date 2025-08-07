package com.pasey.peculiardevices.registration;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.menu.GeoGeneratorMenu;
import com.pasey.peculiardevices.menu.VibratoryMillMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PDMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, PeculiarDevices.MODID);

    public static final RegistryObject<MenuType<GeoGeneratorMenu>> GEO_GENERATOR_MENU =
            MENU_TYPES.register("geo_generator_menu", () -> IForgeMenuType.create(GeoGeneratorMenu::new));

    public static final RegistryObject<MenuType<VibratoryMillMenu>> VIBRATORY_MILL_MENU =
            MENU_TYPES.register("vibratory_mill_menu", () -> IForgeMenuType.create(VibratoryMillMenu::new));
}
