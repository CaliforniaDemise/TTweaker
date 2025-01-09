package surreal.ttweaker.integration.pizzacraft.jei;

import com.tiviacz.pizzacraft.crafting.bakeware.IBakewareRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import surreal.ttweaker.integration.pizzacraft.impl.BakewareRecipe;
import surreal.ttweaker.integration.pizzacraft.impl.ShapedBakewareRecipe;
import surreal.ttweaker.util.JEIUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class BakewareWrapper implements IRecipeWrapper {

    protected final IJeiHelpers jeiHelpers;
    protected final IBakewareRecipe recipe;
    protected final Object[] input;

    public BakewareWrapper(IJeiHelpers helpers, IBakewareRecipe recipe, Object[] input) {
        this.jeiHelpers = helpers;
        this.recipe = recipe;
        this.input = input;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<List<ItemStack>> list = JEIUtils.objArr2List(this.input);
        ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getRecipeOutput());
        ingredients.setInputLists(VanillaTypes.ITEM, list);
    }

    public static IRecipeWrapperFactory<BakewareRecipe> factory(IJeiHelpers helpers) {
        return new Factory<>(helpers);
    }

    private static class Factory<T extends BakewareRecipe> implements IRecipeWrapperFactory<T> {

        private final IJeiHelpers helpers;

        public Factory(IJeiHelpers helpers) {
            this.helpers = helpers;
        }

        @NotNull
        @Override
        public IRecipeWrapper getRecipeWrapper(@NotNull T recipe) {
            if (recipe instanceof ShapedBakewareRecipe) return new ShapedBakewareWrapper(this.helpers, recipe, recipe.getInput());
            else return new BakewareWrapper(helpers, recipe, recipe.getInput());
        }
    }
}
