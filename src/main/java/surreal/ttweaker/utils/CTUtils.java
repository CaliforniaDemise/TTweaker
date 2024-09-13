package surreal.ttweaker.utils;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import crafttweaker.mc1120.item.MCItemDefinition;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;

public class CTUtils {

    public static IItemDefinition getItem(Item item) {
        return new MCItemDefinition(item.getRegistryName().toString(), item);
    }

    public static Ingredient[] getIngredients(IIngredient[] ingredients) {
        Ingredient[] ings = new Ingredient[ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            ings[i] = CraftTweakerMC.getIngredient(ingredients[i]);
        }
        return ings;
    }

    public static Object[] getObjects(IIngredient[] ingredients) {
        Object[] inputs = new Object[ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            IIngredient ing = ingredients[i];
            if (ing instanceof IOreDictEntry) inputs[i] = ((IOreDictEntry) ing).getName();
            else inputs[i] = CraftTweakerMC.getItemStack(ing);
        }
        return inputs;
    }
}
