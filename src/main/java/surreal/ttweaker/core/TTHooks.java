package surreal.ttweaker.core;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import surreal.ttweaker.integration.vanilla.BrewingFuel;

@SuppressWarnings("unused") // Used by transformers
public class TTHooks {

    public static final String OWNER = "surreal/ttweaker/core/TTHooks";

    // Brewing Stand Fuels
    public static int BrewingFuel$handleFuel(ItemStack fuel) {
        return BrewingFuel.getBurnTime(fuel);
    }

    public static boolean BrewingFuel$isFuel(ItemStack fuel) {
        return BrewingFuel.hasKey(fuel);
    }
}
