package surreal.ttweaker.integration.pizzacraft.impl;

import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ShapedBakewareRecipe extends BakewareRecipe {

    protected final int height, width;

    // Inputs length needs to be always 9.
    protected ShapedBakewareRecipe(@Nonnull ItemStack output, int height, int width, Object[] input) {
        super(output, input);
        this.height = height;
        this.width = width;
    }

    public static ShapedBakewareRecipe create(@Nonnull ItemStack output, String[] recipeMap, Object... input) {
        if (output.isEmpty()) throw new RuntimeException("Output can't be empty");
        if (recipeMap.length == 0) throw new RuntimeException("Map array can't empty");
        if (recipeMap.length > 3) throw new RuntimeException("Map array can't be larger than 3");
        if (input.length == 0) throw new RuntimeException("There should at least be one input");
        if (input.length > 18) throw new RuntimeException("https://www.youtube.com/watch?v=BNxlx0zVDwk");
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
                if (c == ' ') inputs[slot] = ItemStack.EMPTY;
                else inputs[slot] = inputMap.get(c);
            }
        }
        return new ShapedBakewareRecipe(output, height, width, inputs);
    }

    @Override
    public boolean matches(InventoryCraftingImproved inv, World world) {
        int start = -1;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack s = inv.getStackInSlot(i);
            if (isSame(s, this.input[0])) {
                start = i;
                break;
            }
            else if (!s.isEmpty()) return false;
        }
        if (start == -1 || (start % 3) + this.width > 3 || (start / 3) + this.height > 3) return false;
        int invSlot = -1;
        for (int h = 0; h < this.height; h++) {
            for (int w = 0; w < this.width; w++) {
                int slot = h + (w + (h * (width - 1)));
                invSlot = start + slot;
                if (invSlot >= inv.getSizeInventory()) return false;
                ItemStack invStack = inv.getStackInSlot(invSlot);
                Object inputObj = this.input[slot];
                if (!isSame(invStack, inputObj)) return false;
            }
        }
        invSlot++;
        for (; invSlot < inv.getSizeInventory(); ++invSlot) {
            if (!inv.getStackInSlot(invSlot).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCraftingImproved inv) {
        return this.output.copy();
    }

    @Override
    public int getRecipeSize() {
        return this.input.length;
    }

    public int getRecipeHeight() {
        return this.height;
    }

    public int getRecipeWidth() {
        return this.width;
    }
}
