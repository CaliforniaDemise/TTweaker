package surreal.ttweaker.crafttweaker.expansions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockDefinition;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.item.IItemDefinition")
@SuppressWarnings("unused") // Used by CraftTweaker
public class ItemDefinitionExpansion {

    @ZenMethod
    public static IBlockDefinition toBlock(IItemDefinition definition) {
        return CraftTweakerMC.getBlockDefinition(Block.getBlockFromItem(CraftTweakerMC.getItem(definition)));
    }

    @ZenMethod
    public static IBlockState toBlockState(IItemDefinition definition, int meta) {
        return toBlock(definition).getStateFromMeta(meta);
    }
}
