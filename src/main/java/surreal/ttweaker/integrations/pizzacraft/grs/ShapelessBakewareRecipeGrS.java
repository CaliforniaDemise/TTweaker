package surreal.ttweaker.integrations.pizzacraft.grs;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapelessBakewareRecipe;

public class ShapelessBakewareRecipeGrS extends ShapelessBakewareRecipe {

    protected ShapelessBakewareRecipeGrS(ItemStack output, Object[] input) {
        super(output, input);
    }

    public static ShapelessBakewareRecipeGrS create(@NotNull ItemStack output, Object... inputs) {
        return new ShapelessBakewareRecipeGrS(output, inputs);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCraftingImproved inv) {
        NonNullList<ItemStack> list = super.getRemainingItems(inv);
        boolean[] inputCheck = new boolean[this.input.length];
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack invStack = inv.getStackInSlot(i);
            if (!invStack.isEmpty()) {
                for (int ingI = 0; ingI < this.input.length; ingI++) {
                    Object ing = this.input[ingI];
                    IIngredient ingredient = (IIngredient) ing;
                    if (!inputCheck[ingI] && this.isSame(invStack, ing)) {
                        inputCheck[ingI] = true;
                        ItemStack remaining = ingredient.applyTransform(invStack);
                        list.set(i, remaining);
                    }
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
            objects[i] = ((IIngredient) o).toMcIngredient();
        }
        return objects;
    }

    @Override
    protected boolean isSame(ItemStack stack, Object input) {
        return ((IIngredient) input).test(stack);
    }
}
