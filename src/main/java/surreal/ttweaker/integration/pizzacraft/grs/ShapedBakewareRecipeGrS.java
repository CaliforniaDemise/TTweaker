package surreal.ttweaker.integration.pizzacraft.grs;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.CraftingRecipe;
import groovy.lang.Closure;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShapedBakewareRecipeGrS extends BakewareRecipeGrS {

    protected final int height, width;
    protected final boolean mirrored;

    public ShapedBakewareRecipeGrS(@NotNull ItemStack output, int height, int width, boolean mirrored, Object[] input, @Nullable Closure<Void> recipeAction, @Nullable Closure<ItemStack> recipeFunction) {
        super(output, input, recipeAction, recipeFunction);
        this.height = height;
        this.width = width;
        this.mirrored = mirrored;
    }

    @Override
    public @NotNull CraftingRecipe.MatchList getMatchingList(InventoryCrafting inv) {
        for (int x = 0; x <= inv.getWidth() - width; x++) {
            for (int y = 0; y <= inv.getHeight() - height; ++y) {
                CraftingRecipe.MatchList matches = checkMatch(inv, x, y, false);
                if (!matches.isEmpty()) return matches;
                if (mirrored) {
                    matches = checkMatch(inv, x, y, true);
                    if (!matches.isEmpty()) return matches;
                }
            }
        }
        return CraftingRecipe.MatchList.EMPTY;
    }

    protected CraftingRecipe.MatchList checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
        CraftingRecipe.MatchList matches = new CraftingRecipe.MatchList();
        for (int x = 0; x < inv.getWidth(); x++) {
            for (int y = 0; y < inv.getHeight(); y++) {
                int subX = x - startX;
                int subY = y - startY;
                IIngredient target = IIngredient.EMPTY;
                if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
                    if (mirror) {
                        target = (IIngredient) input[width - subX - 1 + subY * width];
                    } else {
                        target = (IIngredient) input[subX + subY * width];
                    }
                }
                if (target == null) target = IIngredient.EMPTY;
                ItemStack itemStack = inv.getStackInRowAndColumn(x, y);
                if (target.test(itemStack)) {
                    if (!itemStack.isEmpty()) {
                        matches.addMatch(target, itemStack, x + y * inv.getWidth());
                    }
                } else {
                    return CraftingRecipe.MatchList.EMPTY;
                }
            }
        }

        return matches;
    }
}
