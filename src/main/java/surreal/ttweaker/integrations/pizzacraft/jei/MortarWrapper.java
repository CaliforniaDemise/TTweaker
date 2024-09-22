package surreal.ttweaker.integrations.pizzacraft.jei;

import com.tiviacz.pizzacraft.crafting.mortar.IMortarRecipe;
import com.tiviacz.pizzacraft.util.TextUtils;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapedMortarRecipe;
import surreal.ttweaker.utils.JEIUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class MortarWrapper implements IRecipeWrapper {
    private static final ResourceLocation SHAPED_LOC = TextUtils.setResourceLocation("textures/gui/jei/mortar_and_pestle_shaped.png");

    protected final IJeiHelpers jeiHelpers;
    protected final IMortarRecipe recipe;
    protected final Object[] input;

    private final IDrawable shaped_2;
    private final IDrawable shaped_3;
    private final IDrawable shaped_4;

    public MortarWrapper(IJeiHelpers helpers, IMortarRecipe recipe, Object[] input) {
        this.jeiHelpers = helpers;
        this.recipe = recipe;
        this.input = input;
        this.shaped_2 = helpers.getGuiHelper().createDrawable(SHAPED_LOC, 10, 55, 44, 13);
        this.shaped_3 = helpers.getGuiHelper().createDrawable(SHAPED_LOC, 10, 2, 44, 16);
        this.shaped_4 = helpers.getGuiHelper().createDrawable(SHAPED_LOC, 10, 2, 44, 47);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> list = JEIUtils.objArr2List(this.input);
        ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getRecipeOutput());
        ingredients.setInputLists(VanillaTypes.ITEM, list);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (this.recipe instanceof ShapedMortarRecipe) {
            int size = this.recipe.getRecipeSize();
            switch (size) {
                case 2: this.shaped_2.draw(minecraft, 10, 4); break;
                case 3: this.shaped_3.draw(minecraft, 10, 4); break;
                case 4: this.shaped_4.draw(minecraft, 10, 4); break;
                default: break;
            }
        }
    }

    @Nonnull
    @Override
    public List<String> getTooltipStrings(int x, int y) {
        int size = this.recipe.getRecipeSize();
        if (size == 1) return IRecipeWrapper.super.getTooltipStrings(x, y);
        if ((x >= 10 && x <= (size == 2 ? 54 : 22) || (size > 2 && x >= 42 && x <= 54)) && ((y >= 4 && y <= 16) || (size == 4 && y >= 38 && y <= 51 && x >= 42))) {
            return Collections.singletonList(I18n.format("pizzacraft.jei.shaped"));
        }
        return IRecipeWrapper.super.getTooltipStrings(x, y);
    }
}