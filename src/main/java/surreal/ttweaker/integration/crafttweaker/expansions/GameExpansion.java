package surreal.ttweaker.integration.crafttweaker.expansions;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethodStatic;

@ZenRegister
@ZenExpansion("crafttweaker.game.IGame")
@SuppressWarnings("unused") // Used by CraftTweaker
public class GameExpansion {

    @ZenMethodStatic
    public static IBlockDefinition getBlock(String name) {
        return CraftTweakerMC.getBlockDefinition(Block.getBlockFromName(name));
    }
}