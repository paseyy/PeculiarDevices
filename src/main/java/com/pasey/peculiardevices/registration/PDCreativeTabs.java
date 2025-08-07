package com.pasey.peculiardevices.registration;

import com.pasey.peculiardevices.PeculiarDevices;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PDCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PeculiarDevices.MODID);

    public static final RegistryObject<CreativeModeTab> PD_TAB = CREATIVE_MODE_TABS.register("pd_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(PDItems.LITHIUM_ORE_ITEM.get()))
                    .title(Component.translatable("creativetab.pd_tab"))
                    .displayItems((displayParameters, output) -> {
                        // items
                        output.accept(PDItems.CHROMIUM_MILLINGS.get());
                        output.accept(PDItems.CHROMIUM_INGOT.get());
                        output.accept(PDItems.GOLD_MILLINGS.get());
                        output.accept(PDItems.HEAT_EXCHANGE_UNIT.get());
                        output.accept(PDItems.IRON_MILLINGS.get());
                        output.accept(PDItems.LITHIUM_BATTERY.get());
                        output.accept(PDItems.LITHIUM_INGOT.get());
                        output.accept(PDItems.LITHIUM_MILLINGS.get());
                        output.accept(PDItems.MILLING_CHAMBER.get());
                        output.accept(PDItems.RAW_BARBERTONITE.get());
                        output.accept(PDItems.RAW_LITHIUM.get());

                        // block items
                        output.accept(PDItems.BARBERTONITE_ORE_ITEM.get());
                        output.accept(PDItems.GEO_DEVICE_FRAME_ITEM.get());
                        output.accept(PDItems.GEO_GENERATOR_ITEM.get());
                        output.accept(PDItems.GEO_PIPE_ITEM.get());
                        output.accept(PDItems.LITHIUM_ORE_ITEM.get());
                        output.accept(PDItems.VIBRATORY_MILL_ITEM.get());

                    })
                    .build()
    );
}
