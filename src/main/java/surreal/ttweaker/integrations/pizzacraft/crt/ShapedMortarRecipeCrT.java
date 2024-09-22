package surreal.ttweaker.integrations.pizzacraft.crt;

import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapedMortarRecipe;

public class ShapedMortarRecipeCrT extends ShapedMortarRecipe {

    protected ShapedMortarRecipeCrT(ItemStack output, int duration, Object[] inputs) {
        super(output, duration, inputs);
    }

    public static ShapedMortarRecipeCrT create(IItemStack output, int duration, Object... inputs) {
        if (output.isEmpty()) throw new RuntimeException("Output can't be empty");
        if (inputs.length == 0) throw new RuntimeException("There should at least be one input");
        if (inputs.length > 4) throw new RuntimeException("There can't be more than four inputs");
        if (duration <= 0) throw new RuntimeException("Duration can't be less than or equal to 0");
        return new ShapedMortarRecipeCrT(CraftTweakerMC.getItemStack(output), duration, inputs);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(TileEntityMortarAndPestle te) {
        NonNullList<ItemStack> list = super.getRemainingItems(te);
        ItemStackHandler inv = te.getInventory();
        for (int i = 0; i < this.input.length; i++) {
            ItemStack invStack = inv.getStackInSlot(i);
            Object input = this.input[i];
            IIngredient ing = (IIngredient) input;
            if (ing.hasNewTransformers() && isSame(invStack, ing)) {
                list.set(i, CraftTweakerMC.getItemStack(ing.applyNewTransform(CraftTweakerMC.getIItemStack(invStack))));
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
