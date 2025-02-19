package surreal.ttweaker.integration.pizzacraft.impl;

import com.tiviacz.pizzacraft.crafting.bakeware.IBakewareRecipe;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

/*
* Reimplementation of IBakewareRecipe. BaseShapelessRecipe sucks.
* Input can be Item, Block, ItemStack, Ingredient and String (ore).
*/
public abstract class BakewareRecipe implements IBakewareRecipe {

    protected final ItemStack output;
    protected final Object[] input;

    protected BakewareRecipe(ItemStack output, Object[] input) {
        this.output = output;
        this.input = input;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    public Object[] getInput() {
        return this.input;
    }

    // Stack shouldn't be empty
    protected boolean isSame(ItemStack stack, Object input) {
        if (stack.isEmpty()) {
            return input instanceof ItemStack && ((ItemStack) input).isEmpty();
        }
        if (input instanceof Item) {
            return stack.getItem().equals(input);
        }
        else if (input instanceof Block) {
            Item item = stack.getItem();
            Block block = Blocks.AIR;
            if (item instanceof ItemBlock) block = ((ItemBlock) item).getBlock();
            else if (item instanceof ItemBlockSpecial) block = ((ItemBlockSpecial) item).getBlock();
            return block != Blocks.AIR && block.equals(input);
        }
        else if (input instanceof ItemStack) {
            return ItemStack.areItemStacksEqual(stack, (ItemStack) input);
        }
        else if (input instanceof String) {
            int id = OreDictionary.getOreID((String) input);
            int[] stackIds = OreDictionary.getOreIDs(stack);
            for (int i : stackIds) {
                if (i == id) return true;
            }
            return false;
        }
        else if (input instanceof Ingredient) {
            Ingredient ing = (Ingredient) input;
            return ing.apply(stack);
        }

        return false;
    }
}
