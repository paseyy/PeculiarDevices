package com.pasey.peculiardevices.recipe.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public abstract class BaseRecipe<T extends BaseRecipe<T>> implements Recipe<SimpleContainer> {
    protected final NonNullList<Ingredient> inputItems;
    protected final NonNullList<ItemStack> outputItems;
    protected final int craftingTime;
    protected final float experience;
    private final ResourceLocation id;

    public BaseRecipe(NonNullList<Ingredient> input, NonNullList<ItemStack> output, int craftingTime, float experience, ResourceLocation id) {
        this.inputItems = input;
        this.outputItems = output;
        this.craftingTime = craftingTime;
        this.experience = experience;
        this.id = id;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide())
            return false;

        return inputItems.get(0).test(pContainer.getItem(0));
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return id;
    }

    @Override
    @NotNull
    public abstract RecipeSerializer<?> getSerializer();

    @Override
    @NotNull
    public abstract RecipeType<T> getType();

    @Override
    @NotNull
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    public NonNullList<ItemStack> getOutputs() {
        return outputItems;
    }

    public int getCraftingTime() {
        return craftingTime;
    }

    public static class Serializer<T extends BaseRecipe<T>> implements RecipeSerializer<T> {
        private final RecipeFactory<T> recipeFactory;

        public Serializer(RecipeFactory<T> recipeFactory) {
            this.recipeFactory = recipeFactory;
        }

        @Override
        @NotNull
        @ParametersAreNonnullByDefault
        public T fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            int craftingTime = GsonHelper.getAsInt(pSerializedRecipe, "craftingtime");

            float experience = GsonHelper.getAsFloat(pSerializedRecipe, "experience");

            JsonArray result = GsonHelper.getAsJsonArray(pSerializedRecipe, "outputs");
            NonNullList<ItemStack> outputs = NonNullList.create();
            for (var item : result) {
                outputs.add(itemStackFromJson(item.getAsJsonObject()));
            }

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.create();
            for (var item : ingredients) {
                inputs.add(Ingredient.fromJson(item));
            }

            return recipeFactory.create(inputs, outputs, craftingTime, experience, pRecipeId);
        }

        @Override
        @ParametersAreNonnullByDefault
        public @Nullable T fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            float experience = pBuffer.readFloat();
            int craftingTime = pBuffer.readInt();

            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromNetwork(pBuffer));

            NonNullList<ItemStack> outputs = NonNullList.withSize(pBuffer.readInt(), ItemStack.EMPTY);
            outputs.replaceAll(ignored -> pBuffer.readItem());

            return recipeFactory.create(inputs, outputs, craftingTime, experience, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, T pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());
            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeInt(pRecipe.outputItems.size());
            for(ItemStack itemStack : pRecipe.getOutputs()) {
                pBuffer.writeItem(itemStack);
            }

            pBuffer.writeInt(pRecipe.craftingTime);
            pBuffer.writeFloat(pRecipe.experience);
        }

        public interface RecipeFactory<T extends BaseRecipe<?>> {
            T create(NonNullList<Ingredient> input, NonNullList<ItemStack> output, int craftingTime, float experience, ResourceLocation id);
        }
    }

    public static ItemStack itemStackFromJson(JsonObject json) {
            String itemId = GsonHelper.getAsString(json, "item");
            Item item = ITEMS.getValue(new ResourceLocation(itemId));
            if (item == null) {
                throw new JsonSyntaxException("Unknown item '" + itemId + "'");
            }
            int count = GsonHelper.getAsInt(json, "count", 1);

        return new ItemStack(item, count);
    }
}
