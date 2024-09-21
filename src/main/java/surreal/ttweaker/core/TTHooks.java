package surreal.ttweaker.core;

import com.tiviacz.pizzacraft.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraftforge.oredict.OreDictionary;
import surreal.ttweaker.crafttweaker.BrewingFuel;
import surreal.ttweaker.utils.ItemStackMap;

import java.util.Map;

@SuppressWarnings("unused") // Used by transformers
public class TTHooks {


    // Brewing Stand Fuels
    public static void BrewingFuel$handleFuel(TileEntityBrewingStand tile, ItemStack fuel) {
        int time = BrewingFuel.getTime(fuel);
        if (time > 0) {
            tile.setField(1, time);
            fuel.shrink(1);
            tile.markDirty();
        }
    }

    public static boolean BrewingFuel$isFuel(ItemStack fuel) {
        return BrewingFuel.hasKey(fuel);
    }

    // PizzaCraft
    public static Map<ItemStack, ItemStack> PizzaCraft$createMap() {
        return new ItemStackMap<>(ItemStack.EMPTY);
    }

    public static boolean PizzaCraft$isPeel(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() == ModItems.PEEL) return true;
        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (int id : oreIds) {
            if (id == OreDictionary.getOreID("toolPeel")) {
                return true;
            }
        }
        return false;
    }

    public static boolean PizzaCraft$isKnife(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() == ModItems.KNIFE) return true;
        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (int id : oreIds) {
            if (id == OreDictionary.getOreID("toolKnife")) {
                return true;
            }
        }
        return false;
    }
}
