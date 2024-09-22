package surreal.ttweaker.integrations.pizzacraft.crt;

import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapelessMortarRecipe;

import javax.annotation.Nonnull;

public class ShapelessMortarRecipeCrT extends ShapelessMortarRecipe {

    protected ShapelessMortarRecipeCrT(@Nonnull ItemStack output, int duration, Object[] inputs) {
        super(output, duration, inputs);
    }

    public static ShapelessMortarRecipeCrT create(IItemStack output, int duration, Object... inputs) {
        if (output.isEmpty()) throw new RuntimeException("Output can't be empty");
        if (inputs.length == 0) throw new RuntimeException("There should at least be one input");
        if (inputs.length > 4) throw new RuntimeException("There can't be more than four inputs");
        if (duration <= 0) throw new RuntimeException("Duration can't be less than or equal to 0");
        return new ShapelessMortarRecipeCrT(CraftTweakerMC.getItemStack(output), duration, inputs);
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
                    if (!check[ingI] && ingredient.hasNewTransformers() && this.isSame(invStack, ing)) {
                        check[ingI] = true;
                        ItemStack remaining = CraftTweakerMC.getItemStack(ingredient.applyNewTransform(CraftTweakerMC.getIItemStack(invStack)));
                        list.set(i, remaining);
                    }
                }
            }
        }
        return list;
    }

    @Override
    protected boolean isSame(ItemStack stack, Object input) {
        Ingredient ing = CraftTweakerMC.getIngredient((IIngredient) input);
        return super.isSame(stack, ing);
    }
}
