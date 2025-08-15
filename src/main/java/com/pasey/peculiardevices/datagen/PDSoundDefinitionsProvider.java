package com.pasey.peculiardevices.datagen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.registration.PDSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class PDSoundDefinitionsProvider extends SoundDefinitionsProvider {
    protected PDSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, PeculiarDevices.MODID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(PDSoundEvents.MINERAL_PROBE_HIT.get(), definition()
                .with(sound(ResourceLocation.fromNamespaceAndPath(PeculiarDevices.MODID, "mineral_probe_hit"))));

        this.add(PDSoundEvents.MINERAL_PROBE_MISS.get(), definition()
                .with(sound(ResourceLocation.fromNamespaceAndPath(PeculiarDevices.MODID, "mineral_probe_miss"))));
    }
}
