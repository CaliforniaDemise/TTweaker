package surreal.ttweaker.integration.crafttweaker.expansions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenMethodStatic;

@ZenRegister
@ZenExpansion("crafttweaker.liquid.ILiquidStack")
@SuppressWarnings("unused") // Used by CraftTweaker
public class LiquidStackExpansion {

    @ZenMethod
    public static IData toData(ILiquidStack liquidStack) {
        NBTTagCompound nbt = new NBTTagCompound();
        CraftTweakerMC.getLiquidStack(liquidStack).writeToNBT(nbt);
        return CraftTweakerMC.getIData(nbt);
    }

    @ZenMethodStatic
    public static ILiquidStack fromData(IData data) {
        NBTTagCompound tag = CraftTweakerMC.getNBTCompound(data);
        return CraftTweakerMC.getILiquidStack(FluidStack.loadFluidStackFromNBT(tag));
    }
}
