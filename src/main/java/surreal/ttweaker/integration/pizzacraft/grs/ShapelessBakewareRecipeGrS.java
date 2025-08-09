package surreal.ttweaker.integration.pizzacraft.grs;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.CraftingRecipe;
import groovy.lang.Closure;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapelessBakewareRecipeGrS extends BakewareRecipeGrS {

    public ShapelessBakewareRecipeGrS(@NotNull ItemStack output, Object[] input, @Nullable Closure<Void> recipeAction, @Nullable Closure<ItemStack> recipeFunction) {
        super(output, input, recipeAction, recipeFunction);
    }

    @Override
    public @NotNull CraftingRecipe.MatchList getMatchingList(InventoryCrafting inv) {
        CraftingRecipe.MatchList matches = new CraftingRecipe.MatchList();

        List<Pair<ItemStack, Integer>> givenInputs = new ArrayList<>();
        // collect all items from the crafting matrix
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                givenInputs.add(Pair.of(itemstack, i));
            }
        }
        // check if expected and given inputs have the same count
        if (givenInputs.isEmpty() || givenInputs.size() != input.length) return CraftingRecipe.MatchList.EMPTY;
        List<IIngredient> input = new ArrayList<>();
        for (Object in : this.input) input.add((IIngredient) in);
        // go through each expected input and try to match it to a given input
        Iterator<IIngredient> ingredientIterator = input.iterator();
        main:
        while (ingredientIterator.hasNext()) {
            IIngredient ingredient = ingredientIterator.next();

            Iterator<Pair<ItemStack, Integer>> pairIterator = givenInputs.iterator();
            while (pairIterator.hasNext()) {
                Pair<ItemStack, Integer> pair = pairIterator.next();
                if (matches(ingredient, pair.getLeft())) {
                    // expected input matches given input so both get removed, so they don't get checked again
                    matches.addMatch(ingredient, pair.getLeft(), pair.getRight());
                    ingredientIterator.remove();
                    pairIterator.remove();
                    if (givenInputs.isEmpty()) break main;
                    // skip to next expected ingredient
                    continue main;
                }
            }
            // at this point no given input could be matched for this expected input so return false
            return CraftingRecipe.MatchList.EMPTY;
        }
        return input.isEmpty() ? matches : CraftingRecipe.MatchList.EMPTY;
    }
}
