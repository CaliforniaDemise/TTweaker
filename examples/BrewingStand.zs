import mods.ttweaker.BrewingFuel;

// Adds a fuel that burns for 20 ticks
BrewingFuel.addFuel(<minecraft:stick>);

// You can specify time too
BrewingFuel.addFuel(<minecraft:dirt>, 50);

// Removes all fuels (blaze powder)
BrewingFuel.clear();
