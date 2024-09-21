package surreal.ttweaker.integrations.pizzacraft;

import com.tiviacz.pizzacraft.crafting.mortar.IMortarRecipe;
import com.tiviacz.pizzacraft.crafting.mortar.MortarRecipeManager;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import surreal.ttweaker.utils.CTUtils;

@SuppressWarnings("unused") // Used by GroovyScript / CraftTweaker
public class Mortar {

    // Inputs array can only have 2 objects.
    // Inputs allow ItemStack, String (ore), Block, Item.
    public static void addRecipe(ItemStack output, int duration, Object... inputs) {
        MortarRecipeManager manager = getManager();
        manager.addShapelessRecipe(output, duration, inputs);
    }

    public static void remove(ItemStack output) {
        MortarRecipeManager manager = getManager();
        for (IMortarRecipe recipe : manager.getRecipeList()) {
            if (ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), output)) {
                manager.removeRecipe(recipe);
            }
        }
    }

    public static void removeAll() {
        getManager().getRecipeList().clear();
    }

    @ZenRegister
    @ModOnly("pizzacraft")
    @ZenClass("mods.pizzacraft.Mortar")
    public static class CraftTweaker {

        @ZenMethod
        public static void addRecipe(IItemStack output, int duration, IIngredient[] inputs) {
            if (inputs.length == 0) {
                CraftTweakerAPI.getLogger().logError("Insufficient amount of inputs!");
                return;
            }
            else if (inputs.length > 2) {
                CraftTweakerAPI.getLogger().logError("Amount of inputs can only be between 1 and 9!");
                return;
            }
            Mortar.addRecipe(CraftTweakerMC.getItemStack(output), duration, CTUtils.getObjects(inputs));
        }

        @ZenMethod
        public static void remove(IItemStack output) {

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
