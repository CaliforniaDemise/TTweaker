package surreal.ttweaker.integrations.pizzacraft;

import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
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
import org.apache.commons.lang3.tuple.Pair;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        map.entrySet().removeIf(entry -> ItemStack.areItemsEqual(output, entry.getValue()));
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

    public static class GroovyScript extends VirtualizedRegistry<Pair<ItemStack, ItemStack>> {

        @Override
        public void onReload() {
            removeScripted().forEach(p -> ChoppingBoard.removeByInput(p.getLeft()));
            restoreFromBackup().forEach(p -> ChoppingBoard.addRecipe(p.getRight(), p.getLeft()));
        }

        public void addRecipe(ItemStack output, com.cleanroommc.groovyscript.api.IIngredient input) {
            for (ItemStack in : input.getMatchingStacks()) {
                ChoppingBoard.addRecipe(output, in);
                addScripted(Pair.of(in, output));
            }
        }

        public void removeByOutput(ItemStack output) {
            ChoppingBoardRecipes manager = getManager();
            Iterator<Map.Entry<ItemStack, ItemStack>> iterator = manager.getRecipes().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ItemStack, ItemStack> entry = iterator.next();
                if (ItemStack.areItemStacksEqual(entry.getValue(), output)) {
                    addBackup(Pair.of(entry.getKey(), entry.getValue()));
                    iterator.remove();
                }
            }
        }

        public void removeByInput(com.cleanroommc.groovyscript.api.IIngredient input) {
            ChoppingBoardRecipes manager = getManager();
            Iterator<Map.Entry<ItemStack, ItemStack>> iterator = manager.getRecipes().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ItemStack, ItemStack> entry = iterator.next();
                if (input.test(entry.getKey())) {
                    addBackup(Pair.of(entry.getKey(), entry.getValue()));
                    iterator.remove();
                }
            }
        }

        public void removeAll() {
            ChoppingBoardRecipes manager = getManager();
            Iterator<Map.Entry<ItemStack, ItemStack>> iterator = manager.getRecipes().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ItemStack, ItemStack> entry = iterator.next();
                addBackup(Pair.of(entry.getKey(), entry.getValue()));
                iterator.remove();
            }
        }
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
