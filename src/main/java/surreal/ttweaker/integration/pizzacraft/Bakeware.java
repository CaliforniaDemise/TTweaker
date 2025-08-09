package surreal.ttweaker.integration.pizzacraft;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.ingredient.IngredientHelper;
import com.cleanroommc.groovyscript.registry.AbstractCraftingRecipeBuilder;
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.tiviacz.pizzacraft.crafting.bakeware.IBakewareRecipe;
import com.tiviacz.pizzacraft.crafting.bakeware.PizzaCraftingManager;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import surreal.ttweaker.integration.pizzacraft.crt.ShapedBakewareRecipeCrT;
import surreal.ttweaker.integration.pizzacraft.crt.ShapelessBakewareRecipeCrT;
import surreal.ttweaker.integration.pizzacraft.grs.BakewareRecipeGrS;
import surreal.ttweaker.integration.pizzacraft.grs.ShapedBakewareRecipeGrS;
import surreal.ttweaker.integration.pizzacraft.grs.ShapelessBakewareRecipeGrS;
import surreal.ttweaker.integration.pizzacraft.impl.ShapedBakewareRecipe;
import surreal.ttweaker.integration.pizzacraft.impl.ShapelessBakewareRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused") // Used by GroovyScript / CraftTweaker
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
        System.arraycopy(args, i, inputs, i, args.length - i);
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

    @RegistryDescription
    public static class GroovyScript extends VirtualizedRegistry<IBakewareRecipe> {

        public GroovyScript() {
            super(Collections.singletonList("bakeware"));
        }

        @Override
        @GroovyBlacklist
        public void onReload() {
            removeScripted().forEach(getManager()::removeRecipe);
            restoreFromBackup().forEach(getManager()::addRecipe);
        }


        @RecipeBuilderDescription(example = {
                @Example(".output(item('minecraft:nether_star')).row('TXT').row('X X').row('!X!').key('T', item('minecraft:tnt')).key('X', item('minecraft:clay').reuse()).key('!', item('minecraft:tnt').transform({ _ -> item('minecraft:diamond') }))"),
                @Example(".output(item('minecraft:clay_ball') * 3).shape('S S', ' G ', 'SWS').key([S: ore('netherStar').reuse(), G: ore('ingotGold'), W: fluid('water') * 1000])"),
                @Example(".output(item('minecraft:nether_star')).row('!!!').row('!S!').row('!!!').key([S: ore('netherStar').reuse(), '!': item('minecraft:tnt').transform(item('minecraft:diamond'))])"),
                @Example(".output(item('minecraft:clay')).row(' B').key('B', item('minecraft:glass_bottle'))"),
                @Example(".output(item('minecraft:clay')).row('   ').row(' 0 ').row('   ').key('0', item('minecraft:diamond_sword').withNbt([display:[Name:'Sword with Specific NBT data']]))"),
                @Example(".output(item('minecraft:gold_block')).shape([[item('minecraft:gold_ingot'),item('minecraft:gold_ingot'),item('minecraft:gold_ingot')],[null, null, null],[item('minecraft:gold_ingot'),item('minecraft:gold_ingot'),item('minecraft:gold_ingot')]])"),
                @Example(".output(item('minecraft:clay')).shape([[item('minecraft:gold_ingot'),null,item('minecraft:gold_ingot')],[null,item('minecraft:stone_pickaxe').transformDamage(2).whenAnyDamage(),null]])"),
                @Example(".output(item('minecraft:clay')).shape([[item('minecraft:cobblestone')],[item('minecraft:nether_star')],[item('minecraft:cobblestone')]])")
        })
        public ShapedBuilder shapedBuilder() {
            return new ShapedBuilder();
        }

        @RecipeBuilderDescription(example = {
                @Example(".output(item('minecraft:clay')).input([item('minecraft:cobblestone'),item('minecraft:nether_star'),item('minecraft:gold_ingot')])"),
                @Example(".output(item('minecraft:clay')).input([item('minecraft:diamond'),item('minecraft:gold_ingot'),item('minecraft:gold_ingot')])"),
                @Example(".output(item('minecraft:clay')).input([item('minecraft:cobblestone'), item('minecraft:gold_ingot')])"),
        })
        public ShapelessBuilder shapelessBuilder() {
            return new ShapelessBuilder();
        }

        @MethodDescription(type = MethodDescription.Type.ADDITION, example = {
                @Example("item('minecraft:coal'),[[null,item('minecraft:beef'),null],[item('minecraft:beef'),null,item('minecraft:beef')],[null,item('minecraft:beef'),null]]")
        })
        public void addShapedRecipe(@NotNull ItemStack output, List<List<com.cleanroommc.groovyscript.api.IIngredient>> inputs) {
            shapedBuilder()
                    .matrix(inputs)
                    .output(output)
                    .register();
        }

        @MethodDescription(type = MethodDescription.Type.ADDITION, example = {
                @Example("item('minecraft:coal'),[item('minecraft:feather')]")
        })
        public void addShapelessRecipe(@NotNull ItemStack output, List<com.cleanroommc.groovyscript.api.IIngredient> inputs) {
            shapelessBuilder()
                    .input(inputs)
                    .output(output)
                    .register();
        }

//        @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 1500)
        public boolean removeByOutput(@NotNull ItemStack output) {
            if (output.isEmpty()) return false;
            PizzaCraftingManager manager = getManager();
            Iterator<IBakewareRecipe> iterator = manager.getRecipeList().iterator();
            boolean removed = false;
            while (iterator.hasNext()) {
                IBakewareRecipe recipe = iterator.next();
                if (ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), output)) {
                    addBackup(recipe);
                    iterator.remove();
                    removed = true;
                }
            }
            return removed;
        }

        @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
        public void removeAll() {
            PizzaCraftingManager manager = getManager();
            Iterator<IBakewareRecipe> iterator = manager.getRecipeList().iterator();
            while (iterator.hasNext()) {
                IBakewareRecipe recipe = iterator.next();
                addBackup(recipe);
                iterator.remove();
            }
        }

        public class ShapedBuilder extends AbstractCraftingRecipeBuilder.AbstractShaped<IBakewareRecipe> {

            public ShapedBuilder() {
                super(3, 3);
            }

            @Override
            public String getRecipeNamePrefix() {
                return "groovyscript_bakeware_shaped";
            }

            @Override
            @RecipeBuilderRegistrationMethod
            public IBakewareRecipe register() {
                validateName();
                GroovyLog.Msg msg = GroovyLog.msg("Error adding PizzaCraft Shaped Bakeware recipe '{}'", this.name).error()
                        .add((keyBasedMatrix == null || keyBasedMatrix.length == 0) && (ingredientMatrix == null || ingredientMatrix.isEmpty()), () -> "No matrix was defined")
                        .add(keyBasedMatrix != null && ingredientMatrix != null, () -> "A key based matrix AND a ingredient based matrix was defined. This is not allowed!")
                        .add(IngredientHelper.isEmpty(this.output), () -> "Output must not be empty");
                if (msg.postIfNotEmpty()) return null;
                BakewareRecipeGrS recipe = null;
                if (keyBasedMatrix != null) {
                    recipe = validateShape(msg, errors, keyBasedMatrix, keyMap, ((width1, height1, ingredients) -> new ShapedBakewareRecipeGrS(output, height1, width1, mirrored, ingredients.toArray(new Object[0]), recipeAction, recipeFunction)));
                } else if (ingredientMatrix != null) {
                    recipe = validateShape(msg, ingredientMatrix, ((width1, height1, ingredients) -> new ShapedBakewareRecipeGrS(output.copy(), height1, width1, mirrored, ingredients.toArray(new Object[0]), recipeAction, recipeFunction)));
                }
                if (recipe != null) {
                    handleReplace();
                    msg.add(ReloadableRegistryManager.hasNonDummyRecipe(this.name), () -> "a recipe with that name already exists! Either replace or remove the recipe first");
                    if (msg.postIfNotEmpty()) return null;
                    getManager().addRecipe(recipe);
                    GroovyScript.this.addScripted(recipe);
                }
                return recipe;
            }
        }

        public class ShapelessBuilder extends AbstractCraftingRecipeBuilder.AbstractShapeless<IBakewareRecipe> {

            public ShapelessBuilder() {
                super(3, 3);
            }

            @Override
            public String getRecipeNamePrefix() {
                return "groovyscript_bakeware_shapeless";
            }

            @Override
            @RecipeBuilderRegistrationMethod
            public IBakewareRecipe register() {
                validateName();
                IngredientHelper.trim(ingredients);
                GroovyLog.Msg msg = GroovyLog.msg("Error adding Minecraft Shapeless Crafting recipe '{}'", this.name);
                if (msg.add(IngredientHelper.isEmpty(this.output), () -> "Output must not be empty")
                        .add(ingredients.isEmpty(), () -> "inputs must not be empty")
                        .add(ingredients.size() > width * height, () -> "maximum inputs are " + (width * height) + " but found " + ingredients.size())
                        .error()
                        .postIfNotEmpty()) {
                    return null;
                }
                handleReplace();
                msg.add(ReloadableRegistryManager.hasNonDummyRecipe(this.name), () -> "a recipe with that name already exists! Either replace or remove the recipe first");
                if (msg.postIfNotEmpty()) return null;
                BakewareRecipeGrS recipe = new ShapelessBakewareRecipeGrS(output.copy(), ingredients.toArray(new Object[0]), recipeAction, recipeFunction);
                getManager().addRecipe(recipe);
                GroovyScript.this.addScripted(recipe);
                return recipe;
            }
        }
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
