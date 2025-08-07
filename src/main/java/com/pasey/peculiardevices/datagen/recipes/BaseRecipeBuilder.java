package com.pasey.peculiardevices.datagen.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pasey.peculiardevices.recipe.base.BaseRecipe;
import com.pasey.peculiardevices.registration.PDRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

public class BaseRecipeBuilder {
    private final RecipeCategory category;
    private final ItemStack result;
    private final List<ItemStack> outputs;
    private final List<Ingredient> ingredients;
    private final float experience;
    private final int craftingTime;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private final String group;
    private final RecipeSerializer<? extends BaseRecipe<?>> serializer;
    private final ResourceLocation id;

    public BaseRecipeBuilder(RecipeCategory recipeCategory, RecipeSerializer<? extends BaseRecipe<?>> pSerializer,
                             List<Ingredient> pIngredients, List<ItemStack> pOutputs, float pExperience,
                             int pCraftingTime, String pGroup, ResourceLocation pId) {
        category = recipeCategory;
        result = pOutputs.get(0);
        outputs = pOutputs;
        ingredients = pIngredients;
        experience = pExperience;
        craftingTime = pCraftingTime;
        serializer = pSerializer;
        group = pGroup;
        id = pId;
    }

    public static BaseRecipeBuilder milling(RecipeCategory pCategory, List<Ingredient> pIngredients, List<ItemStack> pOutputs, float pExperience, int pCraftingTime, ResourceLocation id) {
        return new BaseRecipeBuilder(pCategory, PDRecipes.MILLING_SERIALIZER.get(), pIngredients, pOutputs, pExperience, pCraftingTime, "milling", id);
    }

    public FinishedRecipe build() {
        return new FinishedRecipe() {
            @Override
            @ParametersAreNonnullByDefault
            public void serializeRecipeData(JsonObject pJson) {
                pJson.addProperty("category", category.toString());
                pJson.addProperty("craftingtime", craftingTime);
                pJson.addProperty("experience", experience);
                pJson.addProperty("group", group);

                JsonArray ingredients = new JsonArray();
                for (Ingredient ingredient : BaseRecipeBuilder.this.ingredients) {
                    ingredients.add(ingredient.toJson());
                }
                pJson.add("ingredients", ingredients);

                JsonArray outputs = new JsonArray();
                for (ItemStack itemStack : BaseRecipeBuilder.this.outputs) {
                    JsonObject outputJson = new JsonObject();
                    outputJson.addProperty("item",
                            Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).toString());
                    outputJson.addProperty("count", itemStack.getCount());

                    outputs.add(outputJson);
                }
                pJson.add("outputs", outputs);
            }

            @Override
            @NotNull
            public ResourceLocation getId() {
                return BaseRecipeBuilder.this.id;
            }

            @Override
            @NotNull
            public RecipeSerializer<?> getType() {
                return BaseRecipeBuilder.this.serializer;
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        };
    }
}
