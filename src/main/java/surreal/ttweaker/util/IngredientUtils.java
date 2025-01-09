package surreal.ttweaker.util;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

public class IngredientUtils {

    public static Ingredient toIngredient(Object obj) {
        if (obj instanceof Ingredient) return (Ingredient) obj;
        else if (obj instanceof ItemStack) {
            ItemStack stack = (ItemStack) obj;
            if (stack.isEmpty()) return Ingredient.EMPTY;
            else return Ingredient.fromStacks(stack);
        }
        else if (obj instanceof Item) return Ingredient.fromItem((Item) obj);
        else if (obj instanceof Block) {
            Block block = (Block) obj;
            Item item = Item.getItemFromBlock(block);
            if (item == Items.AIR) return Ingredient.EMPTY;
            return Ingredient.fromItem(item);
        }
        else if (obj instanceof String) {
            return new OreIngredient((String) obj);
        }
        return Ingredient.EMPTY;
    }
}
