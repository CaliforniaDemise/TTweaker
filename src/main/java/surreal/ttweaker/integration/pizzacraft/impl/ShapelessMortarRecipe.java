package surreal.ttweaker.integration.pizzacraft.impl;

import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ShapelessMortarRecipe extends MortarRecipe {

    protected ShapelessMortarRecipe(@Nonnull ItemStack output, int duration, Object[] inputs) {
        super(output, duration, inputs);
    }

    public static ShapelessMortarRecipe create(@Nonnull ItemStack output, int duration, Object... inputs) {
        if (output.isEmpty()) throw new RuntimeException("Output can't be empty");
        if (inputs.length == 0) throw new RuntimeException("There should at least be one input");
        if (inputs.length > 4) throw new RuntimeException("There can't be more than four inputs");
        if (duration <= 0) throw new RuntimeException("Duration can't be less than or equal to 0");
        return new ShapelessMortarRecipe(output, duration, inputs);
    }

    @Override
    public boolean matches(TileEntityMortarAndPestle te, World world) {
        ItemStackHandler handler = te.getInventory();
        boolean check = false;
        int count = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack invStack = handler.getStackInSlot(i);
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
}
