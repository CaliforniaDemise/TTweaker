package surreal.ttweaker.integration.pizzacraft;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.tiviacz.pizzacraft.crafting.chopping.ChoppingBoardRecipes;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("unused") // Used by GroovyScript / CraftTweaker
public class ChoppingBoard {

    public static void addRecipe(ItemStack output, ItemStack input) {
        getManager().addChoppingRecipe(input, output);
    }

    public static void addRecipe(ItemStack output, Ingredient input) {
        for (ItemStack stack : input.getMatchingStacks()) {
            addRecipe(output, stack);
        }
    }

    public static void addRecipe(ItemStack output, String ore) {
        for (ItemStack s : OreDictionary.getOres(ore)) {
            addRecipe(output, s);
        }
    }

    public static void removeByOutput(ItemStack output) {
        ChoppingBoardRecipes recipes = getManager();
        Map<ItemStack, ItemStack> map = recipes.getRecipes();
        map.entrySet().removeIf(entry -> ItemStack.areItemsEqual(output, entry.getValue()));
    }

    public static void removeByInput(ItemStack input) {
        ChoppingBoardRecipes manager = getManager();
        Map<ItemStack, ItemStack> map = manager.getRecipes();
        map.remove(input);
    }

    public static void removeByInput(Ingredient input) {
        for (ItemStack stack : input.getMatchingStacks()) {
            removeByInput(stack);
        }
    }

    public static void removeByInput(String ore) {
        for (ItemStack s : OreDictionary.getOres(ore)) {
            removeByInput(s);
        }
    }

    public static void removeAll() {
        getManager().getRecipes().clear();
    }

    private static ChoppingBoardRecipes getManager() {
        return ChoppingBoardRecipes.instance();
    }

    @RegistryDescription
    public static class GroovyScript extends VirtualizedRegistry<Pair<Ingredient, ItemStack>> {

        public GroovyScript() {
            super(Arrays.asList("choppingboard", "choppingBoard", "chopping_board"));
        }

        @Override
        @GroovyBlacklist
        public void onReload() {
            removeScripted().forEach(p -> ChoppingBoard.removeByInput(p.getLeft()));
            restoreFromBackup().forEach(p -> ChoppingBoard.addRecipe(p.getRight(), p.getLeft()));
        }

        @RecipeBuilderDescription(example = {
                @Example(".input(item('minecraft:brick')).output(item('minecraft:clay'))"),
                @Example(".input(ore('blockIron')).output(item('minecraft:iron_ingot'))")
        })
        public RecipeBuilder recipeBuilder() {
            return new RecipeBuilder();
        }

        @MethodDescription(type = MethodDescription.Type.ADDITION, example = @Example("item('minecraft:apple'),item('minecraft:golden_apple')"))
        public void add(@NotNull ItemStack output, @NotNull com.cleanroommc.groovyscript.api.IIngredient input) {
            GroovyLog.Msg msg = GroovyLog.msg("Error adding PizzaCraft Chopping Board recipe '{}'", output)
                    .error()
                    .add(output.isEmpty(), () -> "Output is null")
                    .add(input == com.cleanroommc.groovyscript.api.IIngredient.EMPTY, () -> "Given input shouldn't be empty");
            if (msg.postIfNotEmpty()) return;
            Ingredient ing = input.toMcIngredient();
            addScripted(Pair.of(ing, output));
            ChoppingBoard.addRecipe(output, ing);
        }

        @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 1500, example = @Example("item('pizzacraft:ham')"))
        public void removeByOutput(@NotNull ItemStack output) {
            if (output.isEmpty()) return;
            ChoppingBoardRecipes manager = getManager();
            Iterator<Map.Entry<ItemStack, ItemStack>> iterator = manager.getRecipes().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ItemStack, ItemStack> entry = iterator.next();
                if (ItemStack.areItemStacksEqual(entry.getValue(), output)) {
                    addBackup(Pair.of(Ingredient.fromStacks(entry.getKey()), entry.getValue()));
                    iterator.remove();
                }
            }
        }

        @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 1500, example = @Example("item('pizzacraft:tomato')"))
        public void removeByInput(com.cleanroommc.groovyscript.api.IIngredient input) {
            if (input == com.cleanroommc.groovyscript.api.IIngredient.EMPTY) return;
            if (input == com.cleanroommc.groovyscript.api.IIngredient.ANY) {
                this.removeAll();
                return;
            }
            ChoppingBoardRecipes manager = getManager();
            Iterator<Map.Entry<ItemStack, ItemStack>> iterator = manager.getRecipes().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ItemStack, ItemStack> entry = iterator.next();
                if (input.test(entry.getKey())) {
                    addBackup(Pair.of(Ingredient.fromStacks(entry.getKey()), entry.getValue()));
                    iterator.remove();
                }
            }
        }

        @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
        public void removeAll() {
            ChoppingBoardRecipes manager = getManager();
            Iterator<Map.Entry<ItemStack, ItemStack>> iterator = manager.getRecipes().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ItemStack, ItemStack> entry = iterator.next();
                addBackup(Pair.of(Ingredient.fromStacks(entry.getKey()), entry.getValue()));
                iterator.remove();
            }
        }

        @MethodDescription(type = MethodDescription.Type.QUERY)
        public SimpleObjectStream<Map.Entry<ItemStack, ItemStack>> streamRecipes() {
            return new SimpleObjectStream<>(getManager().getRecipes().entrySet()).setRemover(e -> getManager().getRecipes().entrySet().remove(e));
        }

        @Property(property = "input", valid = @Comp("1"))
        @Property(property = "output", valid = @Comp("1"))
        public class RecipeBuilder extends AbstractRecipeBuilder<Pair<Ingredient, ItemStack>> {

            @Override
            public String getErrorMsg() {
                return "Error adding PizzaCraft Chopping Board recipe";
            }

            @Override
            public void validate(GroovyLog.Msg msg) {
                validateItems(msg, 1, 1, 1, 1);
            }

            @Override
            @RecipeBuilderRegistrationMethod
            public @Nullable Pair<Ingredient, ItemStack> register() {
                if (!validate()) return null;
                Pair<Ingredient, ItemStack> pair = Pair.of(this.input.get(0).toMcIngredient(), this.output.get(0));
                ChoppingBoard.addRecipe(pair.getValue(), pair.getKey());
                GroovyScript.this.addScripted(pair);
                return pair;
            }
        }
    }

    @ZenRegister
    @ModOnly("pizzacraft")
    @ZenClass("mods.pizzacraft.ChoppingBoard")
    public static class CraftTweaker {

        @ZenMethod
        public static void addRecipe(IItemStack output, IIngredient input) {
            ChoppingBoard.addRecipe(CraftTweakerMC.getItemStack(output), CraftTweakerMC.getIngredient(input));
        }

        @ZenMethod
        public static void removeByOutput(IItemStack output) {
            ChoppingBoard.removeByOutput(CraftTweakerMC.getItemStack(output));
        }

        @ZenMethod
        public static void removeByInput(IIngredient ingredient) {
            ChoppingBoard.removeByInput(CraftTweakerMC.getIngredient(ingredient));
        }

        @ZenMethod
        public static void removeAll() {
            ChoppingBoard.removeAll();
        }
    }
}
