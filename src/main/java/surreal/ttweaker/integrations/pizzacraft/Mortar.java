package surreal.ttweaker.integrations.pizzacraft;

import com.tiviacz.pizzacraft.crafting.mortar.MortarRecipeManager;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import surreal.ttweaker.integrations.pizzacraft.crt.ShapedMortarRecipeCrT;
import surreal.ttweaker.integrations.pizzacraft.crt.ShapelessMortarRecipeCrT;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapedMortarRecipe;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapelessMortarRecipe;

@SuppressWarnings("unused") // Used by GroovyScript / CraftTweaker
public class Mortar {

    // Allows 4 inputs
    // They should be putted in same order as the recipe.
    public static void addShaped(ItemStack output, int duration, Object... inputs) {
        MortarRecipeManager manager = getManager();
        manager.addRecipe(ShapedMortarRecipe.create(output, duration, inputs));
    }

    // Allows 4 inputs
    public static void addShapeless(ItemStack output, int duration, Object... inputs) {
        MortarRecipeManager manager = getManager();
        manager.addRecipe(ShapelessMortarRecipe.create(output, duration, inputs));
    }

    public static void remove(ItemStack output) {
        MortarRecipeManager manager = getManager();
        manager.getRecipeList().removeIf(r -> ItemStack.areItemStacksEqual(r.getRecipeOutput(), output));
    }

    public static void removeAll() {
        getManager().getRecipeList().clear();
    }

    @ZenRegister
    @ModOnly("pizzacraft")
    @ZenClass("mods.pizzacraft.Mortar")
    public static class CraftTweaker {

        @ZenMethod
        public static void addShaped(IItemStack output, int duration, IIngredient[] inputs) {
            MortarRecipeManager manager = getManager();
            manager.addRecipe(ShapedMortarRecipeCrT.create(output, duration, (Object[]) inputs));
        }

        @ZenMethod
        public static void addShapeless(IItemStack output, int duration, IIngredient[] inputs) {
            addRecipe(output, duration, inputs);
        }

        @Deprecated
        @ZenMethod
        public static void addRecipe(IItemStack output, int duration, IIngredient[] inputs) {
            MortarRecipeManager manager = getManager();
            manager.addRecipe(ShapelessMortarRecipeCrT.create(output, duration, (Object[]) inputs));
        }

        @ZenMethod
        public static void remove(IItemStack output) {
            Mortar.remove(CraftTweakerMC.getItemStack(output));
        }

        @ZenMethod
        public static void removeAll() {
            getManager().getRecipeList().clear();
        }
    }

    private static MortarRecipeManager getManager() {
        return MortarRecipeManager.getMortarManagerInstance();
    }
}
