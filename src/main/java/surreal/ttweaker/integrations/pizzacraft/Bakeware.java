package surreal.ttweaker.integrations.pizzacraft;

import com.tiviacz.pizzacraft.crafting.bakeware.PizzaCraftingManager;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import surreal.ttweaker.integrations.pizzacraft.crt.ShapedBakewareRecipeCrT;
import surreal.ttweaker.integrations.pizzacraft.crt.ShapelessBakewareRecipeCrT;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapedBakewareRecipe;
import surreal.ttweaker.integrations.pizzacraft.impl.ShapelessBakewareRecipe;
import surreal.ttweaker.utils.CTUtils;

import java.util.ArrayList;
import java.util.List;

// TODO Handle IItemTransformerNews (applyNewTransform) on getRemainingItems
@SuppressWarnings("unused") // Used by CraftTweaker
public class Bakeware {

    public static void addShapedRecipe(ItemStack output, Object... args) {
        List<String> list = new ArrayList<>(3);
        int i;
        for (i = 0; i < 3; i++) {
            Object obj = args[i];
            if (obj instanceof String) list.add((String) obj);
            else break;
        }
        String[] recipeMap = list.toArray(new String[0]);
        Object[] inputs = new Object[args.length - i];
        if (args.length - i >= 0) System.arraycopy(args, i, inputs, i, args.length - i);
        PizzaCraftingManager manager = getManager();
        manager.addRecipe(ShapedBakewareRecipe.create(output, recipeMap, inputs));
    }

    public static void addShapelessRecipe(ItemStack output, Object... inputs) {
        PizzaCraftingManager manager = getManager();
        manager.addRecipe(ShapelessBakewareRecipe.create(output, inputs));
    }

    public static void remove(ItemStack output) {
        PizzaCraftingManager manager = getManager();
        manager.getRecipeList().removeIf(recipe -> ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), output));
    }

    public static void removeAll() {
        getManager().getRecipeList().clear();
    }

    @ZenRegister
    @ModOnly("pizzacraft")
    @ZenClass("mods.pizzacraft.Bakeware")
    public static class CraftTweaker {

        @ZenMethod
        public static void addShaped(IItemStack output, IIngredient[][] inputs) {
            int height = inputs.length;
            int width = inputs[0].length;
            String[] recipeMap = new String[height];
            int i = 0;
            Object2IntMap<IIngredient> sexo = new Object2IntOpenHashMap<>();
            sexo.defaultReturnValue(-1);
            StringBuilder builder = new StringBuilder();
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    IIngredient ing = inputs[h][w];
                    if (ing == null) {
                        builder.append(' ');
                        continue;
                    }
                    int a = sexo.getInt(ing);
                    if (a == -1) {
                        sexo.put(ing, i);
                        a = i;
                        i++;
                    }
                    builder.append((char) a);
                }
                recipeMap[h] = builder.toString();
                builder = new StringBuilder();
            }
            Object[] obj = new Object[recipeMap.length + sexo.size() * 2];
            for (int f = 0; f < recipeMap.length; f++) {
                obj[f] = recipeMap[f];
            }
            int g = 0;
            for (Object2IntMap.Entry<IIngredient> entry : sexo.object2IntEntrySet()) {
                obj[recipeMap.length + g] = (char) entry.getIntValue();
                obj[recipeMap.length + (g + 1)] = entry.getKey();
                g += 2;
            }
            addShaped(output, obj);
        }

        @ZenMethod
        public static void addShaped(IItemStack output, Object... args) {
            List<String> list = new ArrayList<>(3);
            int i = 0;
            while (i < 3) { // Can't just check if object is string because CraftTweaker makes characters strings for whatever the fuck reason.
                Object obj = args[i];
                if (obj instanceof String && ((String) obj).length() > 1) list.add((String) obj);
                else break;
                i++;
            }
            String[] recipeMap = list.toArray(new String[0]);
            Object[] inputs = new Object[args.length - recipeMap.length];
            for (int a = recipeMap.length; a < args.length; a += 2) {
                int b = a - recipeMap.length;
                char fuckYouCrT = args[a] instanceof String ? ((String) args[a]).charAt(0) : (char) args[a];
                inputs[b] = fuckYouCrT;
                inputs[b + 1] = args[a + 1];
            }
            PizzaCraftingManager manager = getManager();
            manager.addRecipe(ShapedBakewareRecipeCrT.create(output, recipeMap, inputs));
        }

        @ZenMethod
        public static void addShapeless(IItemStack output, IIngredient[] inputs) {
            addRecipe(output, inputs);
        }

        @Deprecated
        @ZenMethod
        public static void addRecipe(IItemStack output, IIngredient[] inputs) {
            PizzaCraftingManager manager = getManager();
            manager.addRecipe(ShapelessBakewareRecipeCrT.create(output, (Object[]) inputs));
        }

        @ZenMethod
        public static void remove(IItemStack output) {
            Bakeware.remove(CraftTweakerMC.getItemStack(output));
        }

        @ZenMethod
        public static void removeAll() {
            Bakeware.removeAll();
        }
    }

    private static PizzaCraftingManager getManager() {
        return PizzaCraftingManager.getPizzaCraftingInstance();
    }
}
