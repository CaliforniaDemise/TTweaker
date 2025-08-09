package surreal.ttweaker.integration.vanilla;

import com.cleanroommc.groovyscript.api.documentation.annotations.Example;
import com.cleanroommc.groovyscript.api.documentation.annotations.MethodDescription;
import com.cleanroommc.groovyscript.api.documentation.annotations.RegistryDescription;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import surreal.ttweaker.util.HashStrategies;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused") // Used by GroovyScript / CraftTweaker
public class BrewingFuel {

    private static final Object2IntOpenCustomHashMap<ItemStack> map = new Object2IntOpenCustomHashMap<>(HashStrategies.ITEMSTACK_STRATEGY);

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
        return !stack.isEmpty() && !map.isEmpty() && map.containsKey(stack);
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

    @RegistryDescription
    public static class GroovyScript extends VirtualizedRegistry<GroovyScript.FuelEntry> {

        public GroovyScript() {
            super(Arrays.asList("brewingfuel", "brewingFuel", "brewing_fuel"));
        }

        @Override
        public void onReload() {
            removeScripted().forEach(e -> BrewingFuel.remove(e.stack));
            restoreFromBackup().forEach(e -> BrewingFuel.addEntry(e.stack, e.fuel));
            System.out.println(map);
        }

        @MethodDescription(type = MethodDescription.Type.ADDITION, example = {
                @Example("ore('blockCoal'), 3600"),
                @Example("item('minecraft:coal'), 400")
        })
        public void add(com.cleanroommc.groovyscript.api.IIngredient ingredient, int fuel) {
            for (ItemStack stack : ingredient.getMatchingStacks()) {
                BrewingFuel.addEntry(stack, fuel);
                addScripted(new FuelEntry(stack, fuel));
            }
        }

        @MethodDescription(type = MethodDescription.Type.ADDITION, example = {
                @Example("item('minecraft:feather')")
        })
        public void add(com.cleanroommc.groovyscript.api.IIngredient ingredient) {
            for (ItemStack stack : ingredient.getMatchingStacks()) {
                BrewingFuel.addEntry(stack);
                addScripted(new FuelEntry(stack, 20));
            }
        }

        @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 1500, example = @Example("item('minecraft:blaze_powder')"))
        public void remove(com.cleanroommc.groovyscript.api.IIngredient ingredient) {
            for (ItemStack stack : ingredient.getMatchingStacks()) {
                int fuel = BrewingFuel.getBurnTime(stack);
                if (fuel == 0) continue;
                BrewingFuel.remove(stack);
                addBackup(new FuelEntry(stack, fuel));
            }
        }

        @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
        public void removeAll() {
             Iterator<Object2IntMap.Entry<ItemStack>> iterator = BrewingFuel.map.object2IntEntrySet().iterator();
             while (iterator.hasNext()) {
                 Object2IntMap.Entry<ItemStack> entry = iterator.next();
                 addBackup(new FuelEntry(entry.getKey(), entry.getIntValue()));
                 iterator.remove();
             }
        }

        public static class FuelEntry {
            public final ItemStack stack;
            public final int fuel;
            public FuelEntry(ItemStack stack, int fuel) {
                this.stack = stack;
                this.fuel = fuel;
            }
        }
    }

    static {
        map.put(new ItemStack(Items.BLAZE_POWDER), 20);
    }

}
