package com.pasey.peculiardevices.datagen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.registration.PDItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PDItemModelsProvider extends ItemModelProvider {
    private final ExistingFileHelper existingFileHelper;

    public PDItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PeculiarDevices.MODID, existingFileHelper);
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    protected void registerModels() {
        // items
        basicItem(PDItems.CHROMIUM_INGOT.get());
        basicItem(PDItems.CHROMIUM_MILLINGS.get());
        basicItem(PDItems.COPPER_MILLINGS.get());
        basicItem(PDItems.GOLD_MILLINGS.get());
        basicItem(PDItems.HEAT_EXCHANGE_UNIT.get());
        basicItem(PDItems.IRON_MILLINGS.get());
        basicItem(PDItems.LITHIUM_BATTERY.get());
        basicItem(PDItems.LITHIUM_INGOT.get());
        basicItem(PDItems.LITHIUM_MILLINGS.get());
        basicItem(PDItems.MILLING_CHAMBER.get());
        basicItem(PDItems.RAW_BARBERTONITE.get());
        basicItem(PDItems.RAW_LITHIUM.get());

        // block items
        geoPipeItem(PDItems.GEO_PIPE_ITEM.get());
        blockItem(PDItems.GEO_GENERATOR_ITEM.get());
        blockItem(PDItems.VIBRATORY_MILL_ITEM.get());
    }

    private void geoPipeItem(Item item) {
        withExistingParent("geo_pipe", new ResourceLocation(PeculiarDevices.MODID, "block/geo_pipe"))
                .transforms()
                .transform(ItemDisplayContext.GUI)
                    .rotation(-150, 10, 0)
                    .scale(0.75f, 0.75f, 0.75f)
                    .end()
                .transform(ItemDisplayContext.GROUND)
                    .rotation(0, 0, 0)
                    .translation(0, 3, 0)
                    .scale(0.3f, 0.3f, 0.3f)
                    .end()
                .transform(ItemDisplayContext.FIXED)
                    .rotation(0, 0, 0)
                    .translation(0, 0, 0)
                    .scale(0.5f, 0.5f, 0.5f)
                    .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                    .rotation(75, 45, 0)
                    .translation(0, 2.5f, 0)
                    .scale(0.375f, 0.375f, 0.375f)
                    .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                    .rotation(0, 45, 0)
                    .translation(0, 0, 0)
                    .scale(0.4f, 0.4f, 0.4f)
                    .end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                    .rotation(0, 225, 0)
                    .translation(0, 0, 0)
                    .scale(0.4f, 0.4f, 0.4f)
                    .end();
    }

    private void blockItem(Item item) {
        withExistingParent(item.toString(), new ResourceLocation(PeculiarDevices.MODID, "block/" + item))
                .transforms()
                .transform(ItemDisplayContext.GUI)
                    .rotation(30, 225, 0)
                    .translation(0, 0, 0)
                    .scale(0.625f, 0.625f, 0.625f)
                    .end()
                .transform(ItemDisplayContext.GROUND)
                    .rotation(0, 0, 0)
                    .translation(0, 3, 0)
                    .scale(0.3f, 0.3f, 0.3f)
                    .end()
                .transform(ItemDisplayContext.FIXED)
                    .rotation(0, 0, 0)
                    .translation(0, 0, 0)
                    .scale(0.5f, 0.5f, 0.5f)
                    .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                    .rotation(75, 45, 0)
                    .translation(0, 2.5f, 0)
                    .scale(0.375f, 0.375f, 0.375f)
                    .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                    .rotation(0, 45, 0)
                    .translation(0, 0, 0)
                    .scale(0.4f, 0.4f, 0.4f)
                    .end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                    .rotation(0, 225, 0)
                    .translation(0, 0, 0)
                    .scale(0.4f, 0.4f, 0.4f)
                    .end()
        ;
    }
}


