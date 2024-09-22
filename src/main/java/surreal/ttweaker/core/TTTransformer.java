package surreal.ttweaker.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import surreal.ttweaker.core.transformers.BrewingFuelTransformer;
import surreal.ttweaker.core.transformers.PizzaCraftTransformer;

// 3 T's
@SuppressWarnings("unused") // Loads remotely
public class TTTransformer implements IClassTransformer, Opcodes {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        switch (transformedName) {
            case "net.minecraft.tileentity.TileEntityBrewingStand": return BrewingFuelTransformer.transformTileEntityBrewingStand(basicClass);
            case "net.minecraft.inventory.ContainerBrewingStand": return BrewingFuelTransformer.transformContainerBrewingStand$Fuel(basicClass);

            case "com.tiviacz.pizzacraft.proxy.CommonProxy": return PizzaCraftTransformer.transformCommonProxy(basicClass);
            case "com.tiviacz.pizzacraft.handlers.CommonEventHandler": return PizzaCraftTransformer.transformCommonEventHandler(basicClass);
            case "com.tiviacz.pizzacraft.blocks.BlockPizza": return PizzaCraftTransformer.transformBlockPizza(basicClass);
            case "com.tiviacz.pizzacraft.blocks.BlockChoppingBoard": return PizzaCraftTransformer.transformBlockChoppingBoard(basicClass);
            case "com.tiviacz.pizzacraft.crafting.chopping.ChoppingBoardRecipes": return PizzaCraftTransformer.transformChoppingBoardRecipes(basicClass);
            case "com.tiviacz.pizzacraft.crafting.chopping.ChoppingBoardUtils": return PizzaCraftTransformer.transformChoppingBoardUtils(basicClass);
            case "com.tiviacz.pizzacraft.crafting.mortar.MortarRecipeUtils": return PizzaCraftTransformer.transformMortarRecipeUtils(basicClass);
            case "com.tiviacz.pizzacraft.compat.jei.PizzaCraftPlugin": return PizzaCraftTransformer.transformPizzaCraftPlugin(basicClass);
//            case "com.tiviacz.pizzacraft.compat.jei.bakeware.BakewareRecipeCategory": return PizzaCraftTransformer.transformBakewareRecipeCategory(basicClass);
        }
        return basicClass;
    }
}
