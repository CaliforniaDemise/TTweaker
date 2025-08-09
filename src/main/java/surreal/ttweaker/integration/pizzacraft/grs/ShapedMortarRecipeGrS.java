package surreal.ttweaker.integration.pizzacraft.grs;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import surreal.ttweaker.integration.pizzacraft.impl.ShapedMortarRecipe;

public class ShapedMortarRecipeGrS extends ShapedMortarRecipe {

    protected ShapedMortarRecipeGrS(ItemStack output, int duration, Object[] inputs) {
        super(output, duration, inputs);
    }

    public static ShapedMortarRecipeGrS create(@NotNull ItemStack output, int duration, Object... inputs) {
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
