package surreal.ttweaker.crafttweaker.integrations.pizzacraft;

import com.tiviacz.pizzacraft.crafting.chopping.ChoppingBoardRecipes;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Iterator;
import java.util.Map;

// TODO Make Chopping Board "faster". It's using map, at least make it use the map properly.
@ZenRegister
@ModOnly("pizzacraft")
@ZenClass("mods.pizzacraft.ChoppingBoard")
public class ChoppingBoard {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input) {
        for (IItemStack in : input.getItems()) {
            getManager().addChoppingRecipe(CraftTweakerMC.getItemStack(in), CraftTweakerMC.getItemStack(output));
        }
    }

    @ZenMethod
    public static void removeByOutput(IItemStack output) {
        ChoppingBoardRecipes recipes = getManager();
        Map<ItemStack, ItemStack> map = recipes.getRecipes();
        ItemStack o = CraftTweakerMC.getItemStack(output);
        Iterator<Map.Entry<ItemStack, ItemStack>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ItemStack, ItemStack> entry = iterator.next();
            if (ItemStack.areItemStacksEqual(o, entry.getValue())) {
                iterator.remove();
                break;
            }
        }
    }

    @ZenMethod
    public static void removeByInput(IIngredient input) {
        ChoppingBoardRecipes manager = getManager();
        Map<ItemStack, ItemStack> map = manager.getRecipes();
        for (IItemStack i : input.getItemArray()) {
            ItemStack stack = CraftTweakerMC.getItemStack(i);
            map.remove(stack);
        }
    }

    @ZenMethod
    public static void removeAll() {
        getManager().getRecipes().clear();
    }

    private static ChoppingBoardRecipes getManager() {
        return ChoppingBoardRecipes.instance();
    }
}
