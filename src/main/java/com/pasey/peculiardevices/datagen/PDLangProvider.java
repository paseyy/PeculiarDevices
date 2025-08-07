package com.pasey.peculiardevices.datagen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.registration.PDBlocks;
import com.pasey.peculiardevices.registration.PDCreativeTabs;
import com.pasey.peculiardevices.registration.PDItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class PDLangProvider extends LanguageProvider {
    public PDLangProvider(PackOutput output, String locale) {
        super(output, PeculiarDevices.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        // Blocks
        add(PDBlocks.GEO_DEVICE_FRAME.get(), "Geo Device Frame");
        add(PDBlocks.GEO_GENERATOR.get(), "Geothermal Generator");
        add(PDBlocks.GEO_PIPE.get(), "Geothermal Pipe");
        add(PDBlocks.LITHIUM_ORE.get(), "Lithium Ore");
        add(PDBlocks.VIBRATORY_MILL.get(), "Vibratory Mill");

        // Items
        add(PDItems.CHROMIUM_INGOT.get(), "Chromium Ingot");
        add(PDItems.CHROMIUM_MILLINGS.get(), "Chromium Millings");
        add(PDItems.COPPER_MILLINGS.get(), "Copper Millings");
        add(PDItems.GOLD_MILLINGS.get(), "Gold Millings");
        add(PDItems.HEAT_EXCHANGE_UNIT.get(), "Heat Exchange Unit");
        add(PDItems.IRON_MILLINGS.get(), "Iron Millings");
        add(PDItems.LITHIUM_BATTERY.get(), "Lithium Battery");
        add(PDItems.LITHIUM_INGOT.get(), "Lithium Ingot");
        add(PDItems.LITHIUM_MILLINGS.get(), "Lithium Millings");
        add(PDItems.MILLING_CHAMBER.get(), "Milling Chamber");
        add(PDItems.RAW_BARBERTONITE.get(), "Raw Barbertonite");
        add(PDItems.RAW_LITHIUM.get(), "Raw Lithium");

        // Creative Tab
        add(PDCreativeTabs.PD_TAB.get().getDisplayName().getString(), "Peculiar Devices");
    }
}
