package surreal.ttweaker.crafttweaker.integrations.pizzacraft;

import com.tiviacz.pizzacraft.crafting.bakeware.BaseShapelessOreRecipe;
import com.tiviacz.pizzacraft.crafting.bakeware.IBakewareRecipe;
import com.tiviacz.pizzacraft.crafting.bakeware.PizzaCraftingManager;
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

@ZenRegister
@ModOnly("pizzacraft")
@ZenClass("mods.pizzacraft.Bakeware")
@SuppressWarnings("unused") // Used by CraftTweaker
public class Bakeware {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] inputs) {
        if (inputs.length == 0) {
            CraftTweakerAPI.getLogger().logError("Insufficient amount of inputs!");
            return;
        }
        else if (inputs.length > 9) {
            CraftTweakerAPI.getLogger().logError("Amount of inputs can only be between 1 and 9!");
            return;
        }
        PizzaCraftingManager manager = getManager();
        manager.addRecipe(new BaseShapelessOreRecipe(CraftTweakerMC.getItemStack(output), CTUtils.getObjects(inputs)));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        PizzaCraftingManager manager = getManager();
        ItemStack out = CraftTweakerMC.getItemStack(output);
        for (IBakewareRecipe recipe : manager.getRecipeList()) {
            if (ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), out)) {
                manager.removeRecipe(recipe);
                return;
            }
        }
        CraftTweakerAPI.getLogger().logError("Could not find recipe with output " + output.getDisplayName());
    }

    @ZenMethod
    public static void removeAll() {
        PizzaCraftingManager manager = PizzaCraftingManager.getPizzaCraftingInstance();
        manager.getRecipeList().clear();
    }

    private static PizzaCraftingManager getManager() {
        return PizzaCraftingManager.getPizzaCraftingInstance();
    }
}
