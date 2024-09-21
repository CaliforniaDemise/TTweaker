package surreal.ttweaker.integrations.vanilla;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import surreal.ttweaker.utils.HashStrategies;

import java.util.Map;

@SuppressWarnings("unused") // Used by GroovyScript / CraftTweaker
public class BrewingFuel {

    private static final Map<ItemStack, Integer> map = new Object2IntOpenCustomHashMap<>(HashStrategies.ITEMSTACK_STRATEGY);

    public static void addEntry(ItemStack stack, int amount) {
        map.put(stack, amount);
    }

    public static void addEntry(Ingredient input, int amount) {
        for (ItemStack stack : input.getMatchingStacks()) {
            addEntry(stack, amount);
        }
    }

    public static void addEntry(String ore, int amount) {
        for (ItemStack stack : OreDictionary.getOres(ore)) {
            addEntry(stack, amount);
        }
    }

    public static void addEntry(ItemStack stack) {
        addEntry(stack, 20);
    }

    public static void addEntry(Ingredient input) {
        addEntry(input, 20);
    }

    public static void addEntry(String ore) {
        addEntry(ore, 20);
    }

    public static void remove(ItemStack stack) {
        map.remove(stack);
    }

    public static void remove(Ingredient input) {
        for (ItemStack stack : input.getMatchingStacks()) {
            remove(stack);
        }
    }

    public static void remove(String ore) {
        for (ItemStack stack : OreDictionary.getOres(ore)) {
            remove(stack);
        }
    }

    public static void removeAll() {
        map.clear();
    }

    public static int getBurnTime(ItemStack stack) {
        return map.get(stack);
    }

    public static boolean hasKey(ItemStack stack) {
        return !stack.isEmpty() && map.containsKey(stack);
    }

    @ZenRegister
    @ZenClass("mods.ttweaker.BrewingFuel")
    public static class CraftTweaker {

        @ZenMethod
        public static void addFuel(IIngredient input) {
            addFuel(input, 20);
        }

        @ZenMethod
        public static void addFuel(IIngredient input, int fuelAmount) {
            if (input == null) {
                CraftTweakerAPI.logError("[TTweaker - Brewing Fuel]: The given stack is null.");
                return;
            }
            if (fuelAmount <= 0) {
                CraftTweakerAPI.logError("[TTweaker - Brewing Fuel]: The given amount is smaller or equal to 0.");
                return;
            }
            BrewingFuel.addEntry(CraftTweakerMC.getIngredient(input), fuelAmount);
        }

        @ZenMethod
        public static void remove(IIngredient input) {
            BrewingFuel.remove(CraftTweakerMC.getIngredient(input));
        }

        @Deprecated // Use removeAll
        @ZenMethod
        public static void clear() {
            map.clear();
        }

        @ZenMethod
        public static void removeAll() {
            map.clear();
        }

        @ZenMethod
        public static int getBurnTime(IItemStack stack) {
            return BrewingFuel.getBurnTime(CraftTweakerMC.getItemStack(stack));
        }
    }

    public static class GroovyScript {

    }

    static {
        map.put(new ItemStack(Items.BLAZE_POWDER), 20);
    }

}
