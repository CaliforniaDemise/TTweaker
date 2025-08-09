package surreal.ttweaker.integration.pizzacraft;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import com.tiviacz.pizzacraft.crafting.mortar.IMortarRecipe;
import com.tiviacz.pizzacraft.crafting.mortar.MortarRecipeManager;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import surreal.ttweaker.integration.pizzacraft.crt.ShapedMortarRecipeCrT;
import surreal.ttweaker.integration.pizzacraft.crt.ShapelessMortarRecipeCrT;
import surreal.ttweaker.integration.pizzacraft.grs.ShapedMortarRecipeGrS;
import surreal.ttweaker.integration.pizzacraft.grs.ShapelessMortarRecipeGrS;
import surreal.ttweaker.integration.pizzacraft.impl.ShapedMortarRecipe;
import surreal.ttweaker.integration.pizzacraft.impl.ShapelessMortarRecipe;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused") // Used by GroovyScript / CraftTweaker
public class Mortar {

    // Allows 4 inputs
    // They should be putted in same order as the recipe.
    public static void addShaped(ItemStack output, int duration, Object... inputs) {
        MortarRecipeManager manager = getManager();
        manager.addRecipe(ShapedMortarRecipe.create(output, duration, inputs));
    }

    // Allows 4 inputs
    public static void addShapeless(ItemStack output, int duration, Object... inputs) {
        MortarRecipeManager manager = getManager();
        manager.addRecipe(ShapelessMortarRecipe.create(output, duration, inputs));
    }

    public static void remove(ItemStack output) {
        MortarRecipeManager manager = getManager();
        manager.getRecipeList().removeIf(r -> ItemStack.areItemStacksEqual(r.getRecipeOutput(), output));
    }

    public static void removeAll() {
        getManager().getRecipeList().clear();
    }

    @RegistryDescription
    public static class GroovyScript extends VirtualizedRegistry<IMortarRecipe> {

        public GroovyScript() {
            super(Collections.singletonList("mortar"));
        }

        @Override
        @GroovyBlacklist
        public void onReload() {
            removeScripted().forEach(getManager()::removeRecipe);
            restoreFromBackup().forEach(getManager()::addRecipe);
        }

        @RecipeBuilderDescription(example = {
                @Example(".input(ore('dustRedstone'),item('minecraft:sand')).duration(6).output(item('minecraft:sand',1))"),
                @Example(".input(item('minecraft:bucket'),item('minecraft:obsidian')).duration(8).shaped().output(item('minecraft:lava_bucket'))")
        })
        public RecipeBuilder recipeBuilder() {
            return new RecipeBuilder();
        }

        @MethodDescription(type = MethodDescription.Type.ADDITION, example = {
                @Example("item('minecraft:golden_apple'),4,[item('minecraft:apple'),ore('ingotGold'),ore('ingotGold'),ore('ingotGold')]"),
        })
        public void addShaped(@NotNull ItemStack output, int duration, @NotNull List<com.cleanroommc.groovyscript.api.IIngredient> inputs) {
            GroovyLog.Msg msg = GroovyLog.msg("Error adding PizzaCraft Shaped Mortar recipe '{}'", output)
                    .error()
                    .add(output.isEmpty(), () -> "Output is null")
                    .add(duration < 1, () -> "Duration should be larger than 0")
                    .add(inputs.size() > 4, () -> "Amount of inputs should be at most 4");
            if (msg.postIfNotEmpty()) return;
            MortarRecipeManager manager = getManager();
            ShapedMortarRecipeGrS recipe = ShapedMortarRecipeGrS.create(output, duration, inputs.toArray());
            manager.addRecipe(recipe);
            addScripted(recipe);
        }

        @MethodDescription(type = MethodDescription.Type.ADDITION, example = {
                @Example("item('minecraft:spectral_arrow'),16,[item('minecraft:arrow'),ore('nuggetGold'),ore('nuggetGold'),ore('nuggetGold')]"),
                @Example("item('minecraft:string') * 3,4,[ore('wool')]")
        })
        public void addShapeless(@NotNull ItemStack output, int duration, @NotNull List<com.cleanroommc.groovyscript.api.IIngredient> inputs) {
            GroovyLog.Msg msg = GroovyLog.msg("Error adding PizzaCraft Shapeless Mortar recipe '{}'", output)
                    .error()
                    .add(output.isEmpty(), () -> "Output is null")
                    .add(duration < 1, () -> "Duration needs be larger than 0")
                    .add(inputs.size() > 4, () -> "Amount of inputs should be at most 4");
            if (msg.postIfNotEmpty()) return;
            MortarRecipeManager manager = getManager();
            ShapelessMortarRecipeGrS recipes = ShapelessMortarRecipeGrS.create(output, duration, inputs.toArray());
            manager.addRecipe(recipes);
            addScripted(recipes);
        }

        public boolean remove(IMortarRecipe recipe) {
            boolean b = getManager().getRecipeList().remove(recipe);
            if (b) addBackup(recipe);
            return b;
        }

        @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 1500, example = @Example("item('pizzacraft:seed_onion')"))
        public void removeByOutput(@NotNull ItemStack output) {
            if (output.isEmpty()) return;
            MortarRecipeManager manager = getManager();
            Iterator<IMortarRecipe> iterator = manager.getRecipeList().iterator();
            while (iterator.hasNext()) {
                IMortarRecipe recipe = iterator.next();
                if (ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), output)) {
                    addBackup(recipe);
                    iterator.remove();
                }
            }
        }

        @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 1500, example = @Example(commented = true))
        public void removeAll() {
            MortarRecipeManager manager = getManager();
            Iterator<IMortarRecipe> iterator = manager.getRecipeList().iterator();
            while (iterator.hasNext()) {
                IMortarRecipe recipe = iterator.next();
                addBackup(recipe);
                iterator.remove();
            }
        }


        @MethodDescription(type = MethodDescription.Type.QUERY)
        public SimpleObjectStream<IMortarRecipe> streamRecipes() {
            return new SimpleObjectStream<>(getManager().getRecipeList()).setRemover(this::remove);
        }

        @Property(property = "input", valid = {@Comp(value = "1", type = Comp.Type.GTE), @Comp(value = "4", type = Comp.Type.LTE)})
        @Property(property = "output", valid = @Comp("1"))
        public class RecipeBuilder extends AbstractRecipeBuilder<IMortarRecipe> {

            protected boolean shaped = false;
            protected int duration = 1;

            public RecipeBuilder shaped() {
                this.shaped = true;
                return this;
            }

            public RecipeBuilder duration(int duration) {
                this.duration = duration;
                return this;
            }


            @Override
            public String getErrorMsg() {
                return "Error adding PizzaCraft Chopping Board recipe";
            }

            @Override
            public void validate(GroovyLog.Msg msg) {
                validateItems(msg, 1, 4, 1, 1);
                msg.add(duration < 1, "Duration needs to be larger than 0");
            }

            @Override
            @RecipeBuilderRegistrationMethod
            public @Nullable IMortarRecipe register() {
                if (!validate()) return null;
                com.cleanroommc.groovyscript.api.IIngredient[] ingredients = new com.cleanroommc.groovyscript.api.IIngredient[this.input.size()];
                for (int i = 0; i < ingredients.length; i++) {
                    ingredients[i] = this.input.get(i);
                }
                IMortarRecipe recipe = shaped ? ShapedMortarRecipeGrS.create(this.output.get(0), this.duration, ingredients) : ShapelessMortarRecipeGrS.create(this.output.get(0), this.duration, ingredients);
                getManager().addRecipe(recipe);
                GroovyScript.this.addScripted(recipe);
                return recipe;
            }
        }
    }

    @ZenRegister
    @ModOnly("pizzacraft")
    @ZenClass("mods.pizzacraft.Mortar")
    public static class CraftTweaker {

        @ZenMethod
        public static void addShaped(IItemStack output, int duration, IIngredient[] inputs) {
            MortarRecipeManager manager = getManager();
            manager.addRecipe(ShapedMortarRecipeCrT.create(output, duration, (Object[]) inputs));
        }

        @ZenMethod
        public static void addShapeless(IItemStack output, int duration, IIngredient[] inputs) {
            addRecipe(output, duration, inputs);
        }

        @Deprecated
        @ZenMethod
        public static void addRecipe(IItemStack output, int duration, IIngredient[] inputs) {
            MortarRecipeManager manager = getManager();
            manager.addRecipe(ShapelessMortarRecipeCrT.create(output, duration, (Object[]) inputs));
        }

        @ZenMethod
        public static void remove(IItemStack output) {
            Mortar.remove(CraftTweakerMC.getItemStack(output));
        }

        @ZenMethod
        public static void removeAll() {
            getManager().getRecipeList().clear();
        }
    }

    private static MortarRecipeManager getManager() {
        return MortarRecipeManager.getMortarManagerInstance();
    }
}
