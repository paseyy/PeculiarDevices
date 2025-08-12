package com.pasey.peculiardevices.client.handler;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.client.model.CableModelLoader;
import com.pasey.peculiardevices.client.screen.GeoEnergyCellScreen;
import com.pasey.peculiardevices.client.screen.GeoGeneratorScreen;
import com.pasey.peculiardevices.client.screen.GrimeDynamoScreen;
import com.pasey.peculiardevices.client.screen.VibratoryMillScreen;
import com.pasey.peculiardevices.registration.PDMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PeculiarDevices.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(PDMenus.VIBRATORY_MILL_MENU.get(), VibratoryMillScreen::new);
            MenuScreens.register(PDMenus.GEO_GENERATOR_MENU.get(), GeoGeneratorScreen::new);
            MenuScreens.register(PDMenus.GEO_ENERGY_CELL_MENU.get(), GeoEnergyCellScreen::new);
            MenuScreens.register(PDMenus.GRIME_DYNAMO_MENU.get(), GrimeDynamoScreen::new);
        });
    }

    @SubscribeEvent
    public static void modelInit(ModelEvent.RegisterGeometryLoaders event) {
        CableModelLoader.register(event);
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(ResourceLocation.fromNamespaceAndPath(PeculiarDevices.MODID, "item/jackhammer_model"));
    }


}
