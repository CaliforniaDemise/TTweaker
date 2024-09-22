package surreal.ttweaker.integrations.pizzacraft.jei;

import com.tiviacz.pizzacraft.crafting.bakeware.IBakewareRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import surreal.ttweaker.utils.JEIUtils;

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
}
