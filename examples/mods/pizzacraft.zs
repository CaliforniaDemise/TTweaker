import mods.pizzacraft.Bakeware;
import mods.pizzacraft.Mortar;
import mods.pizzacraft.ChoppingBoard;

// Bakeware Recipes
Bakeware.addRecipe(<minecraft:dirt>, [<ore:ingotIron>, <minecraft:stone>]); // Adds a recipe. Up to 9 inputs. Does not allow OR-Ingredients
Bakeware.remove(<pizzacraft:raw_pizza_0>); // Removes recipe based on output
Bakeware.removeAll(); // Removes all the default recipes

// Mortar Recipes
Mortar.addRecipe(<minecraft:stone>, 8, [<minecraft:diamond>, <ore:ingotGold>]); // Adds a recipe. Up to 2 inputs. Does not allow OR-Ingredients
Mortar.remove(<pizzacraft:flour_corn>); // Removes recipe based on output
Mortar.removeAll(); // Removes all the default recipes

// Chopping Board Recipes
ChoppingBoard.addRecipe(<minecraft:wool>, <minecraft:pumpkin> | <minecraft:brewing_stand>); // Adds a recipe. Allows OR-Ingredients
ChoppingBoard.addRecipe(<minecraft:iron_ingot>, <ore:plankWood>);

ChoppingBoard.removeByOutput(<pizzacraft:onion_slice>); // Removes recipe based on output
ChoppingBoard.removeByInput(<ore:cropTomato> | <pizzacraft:cucumber>); // Removes recipe based on input. Allows OR-Ingredients

ChoppingBoard.removeAll(); // Removes all the default recipes

// Custom peels and knives
<ore:toolPeel>.add(<minecraft:stone_sword>);
<ore:toolKnife>.add(<minecraft:diamond_axe>);