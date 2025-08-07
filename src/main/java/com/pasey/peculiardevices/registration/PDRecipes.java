package com.pasey.peculiardevices.registration;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.recipe.MillingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PDRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PeculiarDevices.MODID);

    public static final RegistryObject<RecipeSerializer<MillingRecipe>> MILLING_SERIALIZER =
            SERIALIZERS.register("milling", () -> MillingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }

}
