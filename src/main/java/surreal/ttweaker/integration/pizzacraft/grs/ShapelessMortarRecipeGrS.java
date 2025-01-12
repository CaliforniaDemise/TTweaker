package surreal.ttweaker.integration.pizzacraft.grs;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import surreal.ttweaker.integration.pizzacraft.impl.ShapelessMortarRecipe;

import javax.annotation.Nonnull;

public class ShapelessMortarRecipeGrS extends ShapelessMortarRecipe {

    protected ShapelessMortarRecipeGrS(@Nonnull ItemStack output, int duration, Object[] inputs) {
        super(output, duration, inputs);
    }

    public static ShapelessMortarRecipeGrS create(ItemStack output, int duration, Object... inputs) {
        if (output.isEmpty()) throw new RuntimeException("Output can't be empty");
        if (inputs.length == 0) throw new RuntimeException("There should at least be one input");
        if (inputs.length > 4) throw new RuntimeException("There can't be more than four inputs");
        if (duration <= 0) throw new RuntimeException("Duration can't be less than or equal to 0");
        return new ShapelessMortarRecipeGrS(output, duration, inputs);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(TileEntityMortarAndPestle te) {
        NonNullList<ItemStack> list = super.getRemainingItems(te);
        ItemStackHandler handler = te.getInventory();
        boolean[] check = new boolean[this.input.length];
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack invStack = handler.getStackInSlot(i);
            if (!invStack.isEmpty()) {
                for (int ingI = 0; ingI < this.input.length; ingI++) {
                    Object ing = this.input[ingI];
                    IIngredient ingredient = (IIngredient) ing;
                    if (!check[ingI] && this.isSame(invStack, ing)) {
                        check[ingI] = true;
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
