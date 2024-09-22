package surreal.ttweaker.core;

import com.tiviacz.pizzacraft.init.ModItems;
import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;
import surreal.ttweaker.integrations.pizzacraft.impl.BakewareRecipe;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapedBakewareRecipe;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapelessBakewareRecipe;
import surreal.ttweaker.integrations.pizzacraft.jei.BakewareWrapper;
import surreal.ttweaker.integrations.pizzacraft.jei.ShapedBakewareWrapper;
import surreal.ttweaker.integrations.vanilla.BrewingFuel;
import surreal.ttweaker.utils.ItemStackMap;

import java.util.List;
import java.util.Map;

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

    // PizzaCraft
    public static Map<ItemStack, ItemStack> PizzaCraft$createMap() {
        return new ItemStackMap<>(ItemStack.EMPTY);
    }

    public static boolean PizzaCraft$isPeel(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() == ModItems.PEEL) return true;
        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (int id : oreIds) {
            if (id == OreDictionary.getOreID("toolPeel")) {
                return true;
            }
        }
        return false;
    }

    public static boolean PizzaCraft$isKnife(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() == ModItems.KNIFE) return true;
        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (int id : oreIds) {
            if (id == OreDictionary.getOreID("toolKnife")) {
                return true;
            }
        }
        return false;
    }

    public static void PizzaCraft$MortarOnTake(TileEntityMortarAndPestle te, List<ItemStack> list) {
        World world = te.getWorld();
        BlockPos pos = te.getPos();
        ItemStackHandler inv = te.getInventory();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack invStack = inv.getStackInSlot(i);
            if (invStack.isEmpty()) continue;
            invStack.shrink(1);
            ItemStack remaining = list.get(i);
            if (!remaining.isEmpty()) {
                remaining = inv.insertItem(i, remaining, false);
            }
            if (!remaining.isEmpty() && !world.isRemote) {
                EntityItem entity = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, remaining);
                world.spawnEntity(entity);
            }
        }
    }

    public static void PizzaCraft$handleBakewareRecipes(IModRegistry registry, String category) {
        registry.handleRecipes(BakewareRecipe.class, (recipe) -> {
            if (recipe instanceof ShapedBakewareRecipe) return new ShapedBakewareWrapper(registry.getJeiHelpers(), recipe, recipe.getInput());
            else return new BakewareWrapper(registry.getJeiHelpers(), recipe, recipe.getInput());
        }, category);
    }

    public static void PizzaCraft$setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients, ICraftingGridHelper helper) {
        if (wrapper instanceof ShapedBakewareRecipe) {
            ShapedBakewareRecipe recipe = (ShapedBakewareRecipe) wrapper;
            helper.setInputs(layout.getItemStacks(), ingredients.getInputs(VanillaTypes.ITEM), recipe.getRecipeWidth(), recipe.getRecipeHeight());
        }
    }
}
