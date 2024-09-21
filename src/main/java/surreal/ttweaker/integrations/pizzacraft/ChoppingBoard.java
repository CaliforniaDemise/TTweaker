package surreal.ttweaker.integrations.pizzacraft;

import com.tiviacz.pizzacraft.crafting.chopping.ChoppingBoardRecipes;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Map;

@SuppressWarnings("unused") // Used by GroovyScript / CraftTweaker
public class ChoppingBoard {

    public static void addRecipe(ItemStack output, ItemStack input) {
        getManager().addChoppingRecipe(input, output);
    }

    public static void addRecipe(ItemStack output, Ingredient input) {
        for (ItemStack stack : input.getMatchingStacks()) {
            addRecipe(stack, output);
        }
    }

    public static void addRecipe(ItemStack output, String ore) {
        for (ItemStack s : OreDictionary.getOres(ore)) {
            addRecipe(s, output);
        }
    }

    public static void removeByOutput(ItemStack output) {
        ChoppingBoardRecipes recipes = getManager();
        Map<ItemStack, ItemStack> map = recipes.getRecipes();
        map.entrySet().removeIf(entry -> ItemStack.areItemStacksEqual(output, entry.getValue()));
    }

    public static void removeByInput(ItemStack input) {
        ChoppingBoardRecipes manager = getManager();
        Map<ItemStack, ItemStack> map = manager.getRecipes();
        map.remove(input);
    }

    public static void removeByInput(Ingredient input) {
        for (ItemStack stack : input.getMatchingStacks()) {
            removeByInput(stack);
        }
    }

    public static void removeByInput(String ore) {
        for (ItemStack s : OreDictionary.getOres(ore)) {
            removeByInput(s);
        }
    }

    public static void removeAll() {
        getManager().getRecipes().clear();
    }

    private static ChoppingBoardRecipes getManager() {
        return ChoppingBoardRecipes.instance();
    }

    @ZenRegister
    @ModOnly("pizzacraft")
    @ZenClass("mods.pizzacraft.ChoppingBoard")
    public static class CraftTweaker {

        @ZenMethod
        public static void addRecipe(IItemStack output, IIngredient input) {
            ChoppingBoard.addRecipe(CraftTweakerMC.getItemStack(output), CraftTweakerMC.getIngredient(input));
        }

        @ZenMethod
        public static void removeByOutput(IItemStack output) {
            ChoppingBoard.removeByOutput(CraftTweakerMC.getItemStack(output));
        }

        @ZenMethod
        public static void removeByInput(IIngredient ingredient) {
            ChoppingBoard.removeByInput(CraftTweakerMC.getIngredient(ingredient));
        }

        @ZenMethod
        public static void removeAll() {
            ChoppingBoard.removeAll();
        }
    }
}
