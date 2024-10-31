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

// Removing Recipes
Bakeware.remove(<pizzacraft:raw_pizza_0>); // Removes recipe based on output
Bakeware.removeAll(); // Removes all the default recipes

# Mortar Recipes 1 Output -> 1-4 inputs
// Shaped Recipes. Inputs should have the same order with the recipe inputs. Up to 4 inputs.
Mortar.addShaped(<minecraft:cobblestone>, 6, [<minecraft:stone> | <minecraft:stone:1> | <minecraft:stone:3> | <minecraft:stone:5>, <pizzacraft:onion_slice>]);

// Shapeless Recipes. Up to 4 inputs
Mortar.addShapeless(<minecraft:wool:1>, 6, [<minecraft:wool>, <minecraft:dirt>, <minecraft:stone>]);
Mortar.addShapeless(<minecraft:golden_apple>, 4, [<minecraft:apple>, <minecraft:water_bucket>.noReturn() | <liquid:water> * 1000]);
Mortar.addRecipe(<minecraft:stone>, 8, [<minecraft:diamond> | <minecraft:dirt>, <ore:ingotGold>, <minecraft:coal>, <minecraft:cobblestone>]);

// Removing Recipes
Mortar.remove(<pizzacraft:flour_corn>); // Removes recipe based on output
Mortar.removeAll(); // Removes all the default recipes

# Chopping Board Recipes 1 Output -> 1 Input
ChoppingBoard.addRecipe(<minecraft:wool>, <minecraft:pumpkin> | <minecraft:brewing_stand>); // Adds a recipe. Allows OR-Ingredients
ChoppingBoard.addRecipe(<minecraft:iron_ingot>, <ore:plankWood>);

ChoppingBoard.removeByOutput(<pizzacraft:onion_slice>); // Removes recipe based on output
ChoppingBoard.removeByInput(<ore:cropTomato> | <pizzacraft:cucumber>); // Removes recipe based on input. Allows OR-Ingredients

ChoppingBoard.removeAll(); // Removes all the default recipes

# Custom peels and knives
<ore:toolPeel>.add(<minecraft:stone_sword>);
<ore:toolKnife>.add(<minecraft:diamond_axe>);