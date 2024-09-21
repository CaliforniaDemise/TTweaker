package surreal.ttweaker.integrations.psi;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.psi.api.PsiAPI;

// TODO Handle support for Official Psi, it needs more work.
@SuppressWarnings("unused") // Used by CraftTweaker
public class Trick {

    @ZenRegister
    @ModOnly("psi")
    @ZenClass("mods.psi.Trick")
    public static class CraftTweaker {
        @ZenMethod
        public static void addRecipe(String trick, IIngredient input, IItemStack output, IItemStack minAssembly) {
            PsiAPI.registerTrickRecipe(trick, CraftTweakerMC.getIngredient(input), CraftTweakerMC.getItemStack(output), CraftTweakerMC.getItemStack(minAssembly));
        }

        @ZenMethod
        public static void remove(IItemStack output) {
            ItemStack stack = CraftTweakerMC.getItemStack(output);
            PsiAPI.trickRecipes.removeIf(recipe -> recipe.getOutput().equals(stack));
        }

        @ZenMethod
        public static void removeAll() {
            PsiAPI.trickRecipes.clear();
        }
    }

    public static class GroovyScript {
    }
}
