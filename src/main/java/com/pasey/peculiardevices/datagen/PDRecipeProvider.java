package com.pasey.peculiardevices.datagen;

import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.datagen.recipes.BaseRecipeBuilder;
import com.pasey.peculiardevices.registration.PDItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


public class PDRecipeProvider extends RecipeProvider {
    public static final List<ItemLike> CHROMIUM_SMELTABLES = List.of(
            PDItems.CHROMIUM_MILLINGS.get()
    );
    public static final List<ItemLike> COPPER_SMELTABLES = List.of(
            PDItems.COPPER_MILLINGS.get()
    );
    public static final List<ItemLike> GOLD_SMELTABLES = List.of(
            PDItems.GOLD_MILLINGS.get()
    );
    public static final List<ItemLike> IRON_SMELTABLES = List.of(
            PDItems.IRON_MILLINGS.get()
    );
    private static final List<ItemLike> LITHIUM_SMELTABLES = List.of(
            PDItems.RAW_LITHIUM.get(),
            PDItems.LITHIUM_MILLINGS.get()
    );

    public PDRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        // blasting and smelting
        PDOreSmelting(consumer, CHROMIUM_SMELTABLES, RecipeCategory.MISC, PDItems.CHROMIUM_INGOT.get(), 0.25f, 200, "chromium_ingot");
        PDOreBlasting(consumer, CHROMIUM_SMELTABLES, RecipeCategory.MISC, PDItems.CHROMIUM_INGOT.get(), 0.25f, 100, "chromium_ingot");

        PDOreSmelting(consumer, COPPER_SMELTABLES, RecipeCategory.MISC, Items.COPPER_INGOT, 0.25f, 200, "copper_ingot");
        PDOreBlasting(consumer, COPPER_SMELTABLES, RecipeCategory.MISC, Items.COPPER_INGOT, 0.25f, 100, "copper_ingot");

        PDOreSmelting(consumer, GOLD_SMELTABLES, RecipeCategory.MISC, Items.GOLD_INGOT, 0.25f, 200, "gold_ingot");
        PDOreBlasting(consumer, GOLD_SMELTABLES, RecipeCategory.MISC, Items.GOLD_INGOT, 0.25f, 100, "gold_ingot");

        PDOreSmelting(consumer, IRON_SMELTABLES, RecipeCategory.MISC, Items.IRON_INGOT, 0.25f, 200, "iron_ingot");
        PDOreBlasting(consumer, IRON_SMELTABLES, RecipeCategory.MISC, Items.IRON_INGOT, 0.25f, 100, "iron_ingot");

        PDOreSmelting(consumer, LITHIUM_SMELTABLES, RecipeCategory.MISC, PDItems.LITHIUM_INGOT.get(), 0.25f, 200, "lithium_ingot");
        PDOreBlasting(consumer, LITHIUM_SMELTABLES, RecipeCategory.MISC, PDItems.LITHIUM_INGOT.get(), 0.25f, 100, "lithium_ingot");

        // milling
        PDMilling(consumer, List.of(PDItems.RAW_BARBERTONITE.get()), List.of(new ItemStack(PDItems.CHROMIUM_MILLINGS.get(), 1)), 0.2f, 200);
        PDMilling(consumer, List.of(Items.RAW_COPPER), List.of(new ItemStack(PDItems.COPPER_MILLINGS.get(), 2)), 0.1f, 200);
        PDMilling(consumer, List.of(Items.RAW_GOLD), List.of(new ItemStack(PDItems.GOLD_MILLINGS.get(), 2)), 0.1f, 200);
        PDMilling(consumer, List.of(Items.RAW_IRON), List.of(new ItemStack(PDItems.IRON_MILLINGS.get(), 2)), 0.1f, 200);
        PDMilling(consumer, List.of(PDItems.RAW_LITHIUM.get()), List.of(new ItemStack(PDItems.LITHIUM_MILLINGS.get(), 2)), 0.1f, 200);

        // crafting
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PDItems.HEAT_EXCHANGE_UNIT.get())
                .pattern("G C")
                .pattern("G C")
                .pattern("G C")
                .define('G', Items.GOLD_INGOT)
                .define('C', Items.COPPER_INGOT)
                .unlockedBy(getHasName(PDItems.LITHIUM_INGOT.get()), has(PDItems.LITHIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PDItems.LITHIUM_BATTERY.get())
                .pattern(" L ")
                .pattern("LRL")
                .pattern(" L ")
                .define('L', PDItems.LITHIUM_INGOT.get())
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(PDItems.LITHIUM_INGOT.get()), has(PDItems.LITHIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PDItems.GEO_DEVICE_FRAME_ITEM.get())
                .pattern("CSC")
                .pattern("SBS")
                .pattern("CSC")
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.STONE)
                .define('B', PDItems.LITHIUM_BATTERY.get())
                .unlockedBy(getHasName(PDItems.LITHIUM_INGOT.get()), has(PDItems.LITHIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PDItems.GEO_GENERATOR_ITEM.get())
                .pattern(" H ")
                .pattern("PFP")
                .pattern("CSC")
                .define('H', PDItems.HEAT_EXCHANGE_UNIT.get())
                .define('P', PDItems.GEO_PIPE_ITEM.get())
                .define('F', PDItems.GEO_DEVICE_FRAME_ITEM.get())
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.STONE)
                .unlockedBy(getHasName(PDItems.LITHIUM_INGOT.get()), has(PDItems.LITHIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PDItems.GEO_PIPE_ITEM.get())
                .pattern(" C ")
                .pattern("CBC")
                .pattern(" C ")
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.WATER_BUCKET)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PDItems.MILLING_CHAMBER.get())
                .pattern(" I ")
                .pattern("IGI")
                .pattern(" I ")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GRAVEL)
                .unlockedBy(getHasName(PDItems.LITHIUM_INGOT.get()), has(PDItems.LITHIUM_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PDItems.VIBRATORY_MILL_ITEM.get())
                .pattern("GMG")
                .pattern("PFP")
                .pattern("CSC")
                .define('G', Items.GLASS)
                .define('M', PDItems.MILLING_CHAMBER.get())
                .define('P', Items.PISTON)
                .define('F', PDItems.GEO_DEVICE_FRAME_ITEM.get())
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.STONE)
                .unlockedBy(getHasName(PDItems.LITHIUM_INGOT.get()), has(PDItems.LITHIUM_INGOT.get()))
                .save(consumer);
    }


    protected static void PDMilling(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients,
                                    List<ItemStack> pOutputs, float pExperience,
                                    int pCraftingTime) {
        List<Ingredient> ingredientList = itemLikeListAsIngredientList(pIngredients);
        String ingredientNames = itemLikeListAsString(pIngredients);
        String outputNames = itemListAsString(pOutputs);

        pFinishedRecipeConsumer.accept(
                BaseRecipeBuilder
                        .milling(RecipeCategory.MISC, ingredientList, pOutputs, pExperience, pCraftingTime,
                            new ResourceLocation(
                                PeculiarDevices.MODID,outputNames + "_from_milling_" + ingredientNames))
                        .build()
        );
    }

    protected static void PDOreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients,
                                        RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme,
                                        String pGroup) {
        PDOreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void PDOreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients,
                                        RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime,
                                        String pGroup) {
        PDOreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void PDOreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer,
                                       RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer,
                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                       float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup)
                    .unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, PeculiarDevices.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    protected static String itemListAsString(List<ItemStack> items) {
        return items.stream()
                .map(ItemStack::getItem)
                .map(ForgeRegistries.ITEMS::getKey)
                .filter(Objects::nonNull)
                .map(ResourceLocation::getPath)
                .reduce((String a, String b) -> "_and_" + b).orElseThrow();
    }

    protected static String itemLikeListAsString(List<ItemLike> items) {
        return items.stream()
                .map(RecipeProvider::getItemName)
                .reduce((String a, String b) -> a + "_and_" + b).orElseThrow();
    }

    protected static List<Ingredient> itemLikeListAsIngredientList(List<ItemLike> items) {
        List<Ingredient> result = new java.util.ArrayList<>(Collections.emptyList());
        for (ItemLike itemLike : items) {
            result.add(Ingredient.of(itemLike));
        }
        return result;
    }
}
