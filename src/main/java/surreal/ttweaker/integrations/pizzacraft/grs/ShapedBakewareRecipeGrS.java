package surreal.ttweaker.integrations.pizzacraft.grs;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapedBakewareRecipe;

public class ShapedBakewareRecipeGrS extends ShapedBakewareRecipe {

    protected ShapedBakewareRecipeGrS(@NotNull ItemStack output, int height, int width, Object[] input) {
        super(output, height, width, input);
    }

    public static ShapedBakewareRecipeGrS create(@NotNull ItemStack output, String[] recipeMap, Object... input) {
        if (recipeMap.length == 0) throw new RuntimeException("Map array can't empty");
        if (recipeMap.length > 3) throw new RuntimeException("Map array can't be larger than 3");
        if ((input.length & 1) == 1) throw new RuntimeException("Problem has occurred while trying to read given inputs");
        Int2ObjectMap<Object> inputMap = new Int2ObjectOpenHashMap<>();
        for (int i = 0; i < input.length; i += 2) {
            char c = (char) input[i];
            if (c == ' ') throw new RuntimeException("<space> character is already mapped to empty stack");
            Object o = input[i + 1];
            inputMap.put(c, o);
        }
        int height = recipeMap.length;
        int width = recipeMap[0].length();
        Object[] inputs = new Object[height * width];
        for (int h = 0; h < height; h++) {
            String map = recipeMap[h];
            if (map.length() != width) throw new RuntimeException("Recipe map strings should have the same length");
            for (int w = 0; w < width; w++) {
                int slot = h + (w + (h * (width - 1)));
                char c = map.charAt(w);
                if (c == ' ') inputs[slot] = null;
                else inputs[slot] = inputMap.get(c);
            }
        }
        return new ShapedBakewareRecipeGrS(output, height, width, inputs);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCraftingImproved inv) {
        NonNullList<ItemStack> list = super.getRemainingItems(inv);
        int start = -1;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack s = inv.getStackInSlot(i);
            if (isSame(s, this.input[0])) {
                start = i;
                break;
            }
        }
        int invSlot;
        for (int h = 0; h < this.height; h++) {
            for (int w = 0; w < this.width; w++) {
                int slot = h + (w + (h * (width - 1)));
                invSlot = start + slot;
                ItemStack invStack = inv.getStackInSlot(invSlot);
                Object inputObj = this.input[slot];
                if (!invStack.isEmpty() && isSame(invStack, inputObj)) {
                    IIngredient ing = (IIngredient) inputObj;
                    list.set(invSlot, ing.applyTransform(invStack));
                }
            }
        }
        return list;
    }

    @Override
    public Object[] getInput() {
        Object[] objects = new Object[this.input.length];
        for (int i = 0; i < this.input.length; i++) {
            Object o = this.input[i];
            if (o == null) objects[i] = null;
            else objects[i] = ((IIngredient) o).toMcIngredient();
        }
        return objects;
    }

    @Override
    protected boolean isSame(ItemStack stack, Object input) {
        if (input == null) return stack.isEmpty();
        return ((IIngredient) input).test(stack);
    }
}