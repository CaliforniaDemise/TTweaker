package surreal.ttweaker.integration.pizzacraft.grs;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import surreal.ttweaker.integration.pizzacraft.impl.ShapedMortarRecipe;

public class ShapedMortarRecipeGrS extends ShapedMortarRecipe {

    protected ShapedMortarRecipeGrS(ItemStack output, int duration, Object[] inputs) {
        super(output, duration, inputs);
    }

    public static ShapedMortarRecipeGrS create(ItemStack output, int duration, Object... inputs) {
        if (output.isEmpty()) throw new RuntimeException("Output can't be empty");
        if (inputs.length == 0) throw new RuntimeException("There should at least be one input");
        if (inputs.length > 4) throw new RuntimeException("There can't be more than four inputs");
        if (duration <= 0) throw new RuntimeException("Duration can't be less than or equal to 0");
        return new ShapedMortarRecipeGrS(output, duration, inputs);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(TileEntityMortarAndPestle te) {
        NonNullList<ItemStack> list = super.getRemainingItems(te);
        ItemStackHandler inv = te.getInventory();
        for (int i = 0; i < this.input.length; i++) {
            ItemStack invStack = inv.getStackInSlot(i);
            Object input = this.input[i];
            IIngredient ing = (IIngredient) input;
            if (isSame(invStack, ing)) {
                list.set(i, ing.applyTransform(invStack));
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
