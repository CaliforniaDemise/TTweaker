package surreal.ttweaker.integration.pizzacraft.jei;

import com.tiviacz.pizzacraft.crafting.bakeware.IBakewareRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import surreal.ttweaker.integration.pizzacraft.impl.ShapedBakewareRecipe;

public class ShapedBakewareWrapper extends BakewareWrapper implements IShapedCraftingRecipeWrapper {

    private final ShapedBakewareRecipe recipe;

    public ShapedBakewareWrapper(IJeiHelpers helpers, IBakewareRecipe recipe, Object[] input) {
        super(helpers, recipe, input);
        this.recipe = (ShapedBakewareRecipe) recipe;
    }

    @Override
    public int getWidth() {
        return this.recipe.getRecipeWidth();
    }

    @Override
    public int getHeight() {
        return this.recipe.getRecipeHeight();
    }
}
