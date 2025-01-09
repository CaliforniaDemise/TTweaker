package surreal.ttweaker.integration.pizzacraft.impl;

import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class ShapedMortarRecipe extends MortarRecipe {

    protected ShapedMortarRecipe(ItemStack output, int duration, Object[] inputs) {
        super(output, duration, inputs);
    }

    public static ShapedMortarRecipe create(ItemStack output, int duration, Object[] inputs) {
        if (output.isEmpty()) throw new RuntimeException("Output can't be empty");
        if (inputs.length == 0) throw new RuntimeException("There should at least be one input");
        if (inputs.length > 4) throw new RuntimeException("There can't be more than four inputs");
        if (duration <= 0) throw new RuntimeException("Duration can't be less than or equal to 0");
        return new ShapedMortarRecipe(output, duration, inputs);
    }

    @Override
    public boolean matches(TileEntityMortarAndPestle te, World world) {
        ItemStackHandler inv = te.getInventory();
        boolean check = true;
        for (int i = 0; i < this.input.length; i++) {
            ItemStack invStack = inv.getStackInSlot(i);
            Object ing = this.input[i];
            check &= isSame(invStack, ing);
        }
        return check;
    }
}
