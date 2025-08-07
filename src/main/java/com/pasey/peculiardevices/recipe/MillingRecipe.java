package com.pasey.peculiardevices.recipe;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.recipe.base.BaseRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class MillingRecipe extends BaseRecipe<MillingRecipe> {
    public MillingRecipe(NonNullList<Ingredient> input, NonNullList<ItemStack> output, int craftingTime, float experience, ResourceLocation id) {
        super(input, output, craftingTime, experience, id);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<MillingRecipe> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<MillingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = new ResourceLocation(PeculiarDevices.MODID, "milling");
    }

    public static class Serializer extends BaseRecipe.Serializer<MillingRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public Serializer() {
            super(MillingRecipe::new);
        }
    }
}

