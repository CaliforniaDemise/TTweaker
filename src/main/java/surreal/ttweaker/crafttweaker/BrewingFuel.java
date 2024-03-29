package surreal.ttweaker.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import surreal.ttweaker.utils.ComparableStack;
import surreal.ttweaker.utils.HashStrategies;

import java.util.Map;

@ZenClass("mods.ttweaker.BrewingFuel")
public class BrewingFuel {

    private static final Map<ItemStack, Integer> map = new Object2IntOpenCustomHashMap<>(HashStrategies.ITEMSTACK_STRATEGY);

    @ZenMethod
    public static void addFuel(IItemStack stack) {
        addFuel(stack, 20);
    }

    @ZenMethod
    public static void addFuel(IItemStack stack, int fuelAmount) {

        if (stack == null) {
            CraftTweakerAPI.logError("[TTweaker - Brewing Fuel]: The given stack is null!");
            return;
        }

        if (fuelAmount <= 0) {
            CraftTweakerAPI.logError("[TTweaker - Brewing Fuel]: The given amount is smaller or equal to 0!");
            return;
        }

        map.put(CraftTweakerMC.getItemStack(stack), fuelAmount);
    }

    @ZenMethod
    public static void clear() {
        map.clear();
    }

    // INTERNAL //
    public static int getTime(ItemStack stack) {
        return map.get(stack);
    }

    public static boolean hasKey(ItemStack stack) {
        ComparableStack c = new ComparableStack(stack);
        return map.containsKey(stack);
    }


    static {
        map.put(new ItemStack(Items.BLAZE_POWDER), 20);
    }

}
