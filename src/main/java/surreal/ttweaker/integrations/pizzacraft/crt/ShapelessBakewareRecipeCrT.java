package surreal.ttweaker.integrations.pizzacraft.crt;

import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapelessBakewareRecipe;

public class ShapelessBakewareRecipeCrT extends ShapelessBakewareRecipe {

    protected ShapelessBakewareRecipeCrT(ItemStack output, Object[] input) {
        super(output, input);
    }

    public static ShapelessBakewareRecipeCrT create(IItemStack output, Object... inputs) {
        return new ShapelessBakewareRecipeCrT(CraftTweakerMC.getItemStack(output), inputs);
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
                    if (!inputCheck[ingI] && ingredient.hasNewTransformers() && this.isSame(invStack, ing)) {
                        inputCheck[ingI] = true;
                        ItemStack remaining = CraftTweakerMC.getItemStack(ingredient.applyNewTransform(CraftTweakerMC.getIItemStack(invStack)));
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
            objects[i] = CraftTweakerMC.getIngredient((IIngredient) o);
        }
        return objects;
    }

    @Override
    protected boolean isSame(ItemStack stack, Object input) {
        Ingredient ing = CraftTweakerMC.getIngredient((IIngredient) input);
        return super.isSame(stack, ing);
    }
}
