package surreal.ttweaker.core;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import surreal.ttweaker.integration.vanilla.BrewingFuel;

@SuppressWarnings("unused") // Used by transformers
public class TTHooks {

    // Brewing Stand Fuels
    public static void BrewingFuel$handleFuel(TileEntityBrewingStand tile, ItemStack fuel) {
        int time = BrewingFuel.getBurnTime(fuel);
        if (time > 0) {
            tile.setField(1, time);
            fuel.shrink(1);
            tile.markDirty();
        }
    }

    public static boolean BrewingFuel$isFuel(ItemStack fuel) {
        return BrewingFuel.hasKey(fuel);
    }
}
