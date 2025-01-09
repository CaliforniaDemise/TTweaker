package surreal.ttweaker.util;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JEIUtils {

    public static List<List<ItemStack>> objArr2List(Object[] input) {
        List<List<ItemStack>> out = new ArrayList<>();
        for (Object obj : input) {
            List<ItemStack> ing = Arrays.asList(IngredientUtils.toIngredient(obj).getMatchingStacks());
            out.add(ing);
        }
        return out;
    }
}
