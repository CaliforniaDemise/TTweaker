package surreal.ttweaker.integrations.pizzacraft.impl;

import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ShapelessBakewareRecipe extends BakewareRecipe {

    protected ShapelessBakewareRecipe(@Nonnull ItemStack output, Object[] input) {
        super(output, input);
    }

    public static ShapelessBakewareRecipe create(@Nonnull ItemStack output, Object... input) {
        if (output.isEmpty()) throw new RuntimeException("Output can't be empty");
        if (input.length == 0) throw new RuntimeException("There should at least be one input");
        if (input.length > 9) throw new RuntimeException("There can't be more than nine inputs");
        return new ShapelessBakewareRecipe(output, input);
    }

    @Override
    public boolean matches(InventoryCraftingImproved inv, World world) {
        boolean check = false;
        int count = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack invStack = inv.getStackInSlot(i);
            if (!invStack.isEmpty()) {
                boolean toCheck = false;
                for (Object ing : this.input) {
                    toCheck = isSame(invStack, ing);
                    if (toCheck) break;
                }
                if (!toCheck) return false;
                check = toCheck;
                count++;
            }
        }
        return check && count == this.input.length;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCraftingImproved inventoryCraftingImproved) {
        return this.output.copy();
    }

    @Override
    public int getRecipeSize() {
        return this.input.length;
    }
}
