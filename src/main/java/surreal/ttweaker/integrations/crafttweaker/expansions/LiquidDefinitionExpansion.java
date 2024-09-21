package surreal.ttweaker.integrations.crafttweaker.expansions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraftforge.fluids.FluidRegistry;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.liquid.ILiquidDefinition")
@SuppressWarnings("unused") // Used by CraftTweaker
public class LiquidDefinitionExpansion {

    @ZenMethod
    public static boolean hasBucket(ILiquidDefinition liquid) {
        return FluidRegistry.hasBucket(CraftTweakerMC.getFluid(liquid));
    }

    @ZenMethod
    public static boolean addBucket(ILiquidDefinition liquid) {
        return FluidRegistry.addBucketForFluid(CraftTweakerMC.getFluid(liquid));
    }
}
