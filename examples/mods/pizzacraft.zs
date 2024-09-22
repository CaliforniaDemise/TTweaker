import mods.pizzacraft.Bakeware;
import mods.pizzacraft.Mortar;
import mods.pizzacraft.ChoppingBoard;

import crafttweaker.item.IIngredient;

# Bakeware Recipes. Works like Crafting Table recipes.
// Shaped Recipes
// For some reason, you need to cast inputs to IIngredient[][]. If you know why CrT thinks it's 'IAny[]' please create an issue or pull a PR about it.
Bakeware.addShaped(<minecraft:furnace>, [[<ore:ingotBrick>, <ore:ingotBrick>, <ore:ingotBrick>], [<ore:ingotBrick>, null, <ore:ingotBrick>], [<ore:ingotIron>, <ore:ingotIron>, <ore:ingotIron>]] as IIngredient[][]);
Bakeware.addShaped(<minecraft:wool>, "AAA", "BBB", 'A', <ore:ingotIron>, 'B', <ore:logWood>); // Suggested way
Bakeware.addShaped(<minecraft:dirt> * 8, "AAA", "ABA", "AAA", 'A', <minecraft:stone:*>, 'B', <minecraft:water_bucket>.noReturn());

// Shapeless Recipes
Bakeware.addShapeless(<minecraft:stone>, [<ore:ingotGold> | <minecraft:stone>, <ore:ingotBrick>, <ore:ingotGold> | <minecraft:stone>]);
Bakeware.addShapeless(<minecraft:dirt>, [<minecraft:potato>.transformReplace(<minecraft:poisonous_potato>)]);
Bakeware.addRecipe(<minecraft:dirt>, [<ore:ingotIron>, <minecraft:stone>]); // Same with addShapeless. Default Bakeware recipes are shapeless that's why this is here.

// Removing recipes
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