package surreal.ttweaker.integrations.pizzacraft.impl;

import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ShapelessBakewareRecipe extends BakewareRecipe {

    protected ShapelessBakewareRecipe(ItemStack output, Object[] input) {
        super(output, input);
    }

    public static ShapelessBakewareRecipe create(ItemStack output, Object... input) {
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
