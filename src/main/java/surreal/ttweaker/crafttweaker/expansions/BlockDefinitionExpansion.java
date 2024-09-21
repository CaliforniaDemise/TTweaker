package surreal.ttweaker.crafttweaker.expansions;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockDefinition;
import crafttweaker.api.item.IItemDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;
import surreal.ttweaker.utils.CTUtils;

@ZenRegister
@ZenExpansion("crafttweaker.block.IBlockDefinition")
@SuppressWarnings("unused") // Used by CraftTweaker
public class BlockDefinitionExpansion {

    @ZenMethod
    public static IItemDefinition toItem(IBlockDefinition definition) {
        return CTUtils.getItem(Item.getItemFromBlock(CraftTweakerMC.getBlock(definition)));
    }

    @ZenMethod
    public static IItemStack toItemstack(IBlockDefinition definition, int meta) {

        Block block = Block.getBlockFromName(definition.getId());

        if (block == null || block == Blocks.AIR) {
            CraftTweakerAPI.logError("Couldn't find block named: " + definition.getId());
            return null;
        }

        return CraftTweakerMC.getIItemStack(new ItemStack(block, 1, meta));
    }
}
