package surreal.ttweaker.integrations.pizzacraft;

import com.tiviacz.pizzacraft.crafting.bakeware.BaseShapelessOreRecipe;
import com.tiviacz.pizzacraft.crafting.bakeware.IBakewareRecipe;
import com.tiviacz.pizzacraft.crafting.bakeware.PizzaCraftingManager;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import surreal.ttweaker.utils.CTUtils;

@SuppressWarnings("unused") // Used by CraftTweaker
public class Bakeware {

    // Inputs array can only have 9 objects.
    // Inputs allow ItemStack, String (ore), Block, Item.
    public static void addRecipe(ItemStack output, Object... inputs) {
        PizzaCraftingManager manager = getManager();
        manager.addRecipe(new BaseShapelessOreRecipe(output, inputs));
    }

    public static void remove(ItemStack output) {
        PizzaCraftingManager manager = getManager();
        for (IBakewareRecipe recipe : manager.getRecipeList()) {
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
    @ZenClass("mods.pizzacraft.Bakeware")
    public static class CraftTweaker {

        @ZenMethod
        public static void addRecipe(IItemStack output, IIngredient[] inputs) {
            Bakeware.addRecipe(CraftTweakerMC.getItemStack(output), CTUtils.getObjects(inputs));
        }

        @ZenMethod
        public static void remove(IItemStack output) {
            Bakeware.remove(CraftTweakerMC.getItemStack(output));
        }

        @ZenMethod
        public static void removeAll() {
            Bakeware.removeAll();
        }
    }

    private static PizzaCraftingManager getManager() {
        return PizzaCraftingManager.getPizzaCraftingInstance();
    }
}
