
// Auto generated groovyscript example file
// MODS_LOADED: pizzacraft

log.info 'mod \'pizzacraft\' detected, running script'

// Knife:
// An ore dictionary to define items in it a "knife". This makes them usable in Chopping Board and other stuff.
ore('toolKnife').add(item('minecraft:diamond_sword'))

// Peel:
// An ore dictionary to define items in it a "peel". Allows you to get Pizzas with right click.
ore('toolPeel').add(item('minecraft:diamond_shovel'))

// Bakeware:
// Literally same with crafting table except without a way to replace recipes.

// mods.pizzacraft.bakeware.removeAll()

mods.pizzacraft.bakeware.shapedBuilder()
    .output(item('minecraft:nether_star'))
    .row('TXT')
    .row('X X')
    .row('!X!')
    .key('T', item('minecraft:tnt'))
    .key('X', item('minecraft:clay').reuse())
    .key('!', item('minecraft:tnt').transform({ _ -> item('minecraft:diamond') }))
    .register()

mods.pizzacraft.bakeware.shapedBuilder()
    .output(item('minecraft:clay_ball') * 3)
    .shape('S S',
           ' G ',
           'SWS')
    .key([S: ore('netherStar').reuse(), G: ore('ingotGold'), W: fluid('water') * 1000])
    .register()

mods.pizzacraft.bakeware.shapedBuilder()
    .output(item('minecraft:nether_star'))
    .row('!!!')
    .row('!S!')
    .row('!!!')
    .key([S: ore('netherStar').reuse(), '!': item('minecraft:tnt').transform(item('minecraft:diamond'))])
    .register()

mods.pizzacraft.bakeware.shapedBuilder()
    .output(item('minecraft:clay'))
    .row(' B')
    .key('B', item('minecraft:glass_bottle'))
    .register()

mods.pizzacraft.bakeware.shapedBuilder()
    .output(item('minecraft:clay'))
    .row('   ')
    .row(' 0 ')
    .row('   ')
    .key('0', item('minecraft:diamond_sword').withNbt([display:[Name:'Sword with Specific NBT data']]))
    .register()

mods.pizzacraft.bakeware.shapedBuilder()
    .output(item('minecraft:gold_block'))
    .shape([[item('minecraft:gold_ingot'),item('minecraft:gold_ingot'),item('minecraft:gold_ingot')],
           [null, null, null],
           [item('minecraft:gold_ingot'),item('minecraft:gold_ingot'),item('minecraft:gold_ingot')]])
    .register()

mods.pizzacraft.bakeware.shapedBuilder()
    .output(item('minecraft:clay'))
    .shape([[item('minecraft:gold_ingot'),null,item('minecraft:gold_ingot')],
           [null,item('minecraft:stone_pickaxe').transformDamage(2).whenAnyDamage(),null]])
    .register()

mods.pizzacraft.bakeware.shapedBuilder()
    .output(item('minecraft:clay'))
    .shape([[item('minecraft:cobblestone')],
           [item('minecraft:nether_star')],
           [item('minecraft:cobblestone')]])
    .register()

mods.pizzacraft.bakeware.shapelessBuilder()
    .output(item('minecraft:clay'))
    .input([item('minecraft:cobblestone'),item('minecraft:nether_star'),item('minecraft:gold_ingot')])
    .register()

mods.pizzacraft.bakeware.shapelessBuilder()
    .output(item('minecraft:clay'))
    .input([item('minecraft:diamond'),item('minecraft:gold_ingot'),item('minecraft:gold_ingot')])
    .register()

mods.pizzacraft.bakeware.shapelessBuilder()
    .output(item('minecraft:clay'))
    .input([item('minecraft:cobblestone'), item('minecraft:gold_ingot')])
    .register()


mods.pizzacraft.bakeware.addShapedRecipe(item('minecraft:coal'),[[null,item('minecraft:beef'),null],[item('minecraft:beef'),null,item('minecraft:beef')],[null,item('minecraft:beef'),null]])
mods.pizzacraft.bakeware.addShapelessRecipe(item('minecraft:coal'),[item('minecraft:feather')])

// Chopping Board:
// One input, one output.

mods.pizzacraft.choppingboard.removeByInput(item('pizzacraft:tomato'))
mods.pizzacraft.choppingboard.removeByOutput(item('pizzacraft:ham'))
// mods.pizzacraft.choppingboard.removeAll()

mods.pizzacraft.choppingboard.recipeBuilder()
    .input(item('minecraft:brick'))
    .output(item('minecraft:clay'))
    .register()

mods.pizzacraft.choppingboard.recipeBuilder()
    .input(ore('blockIron'))
    .output(item('minecraft:iron_ingot'))
    .register()


mods.pizzacraft.choppingboard.add(item('minecraft:apple'),item('minecraft:golden_apple'))

// Mortar:
// A machine that can get 4 inputs and 1 output. Shaped recipes needs to be put in order.

// mods.pizzacraft.mortar.removeAll()
mods.pizzacraft.mortar.removeByOutput(item('pizzacraft:seed_onion'))

mods.pizzacraft.mortar.recipeBuilder()
    .input(ore('dustRedstone'),item('minecraft:sand'))
    .duration(6)
    .output(item('minecraft:sand',1))
    .register()

mods.pizzacraft.mortar.recipeBuilder()
    .input(item('minecraft:bucket'),item('minecraft:obsidian'))
    .duration(8)
    .shaped()
    .output(item('minecraft:lava_bucket'))
    .register()


mods.pizzacraft.mortar.addShaped(item('minecraft:golden_apple'),4,[item('minecraft:apple'),ore('ingotGold'),ore('ingotGold'),ore('ingotGold')])
mods.pizzacraft.mortar.addShapeless(item('minecraft:string') * 3,4,[ore('wool')])
mods.pizzacraft.mortar.addShapeless(item('minecraft:spectral_arrow'),16,[item('minecraft:arrow'),ore('nuggetGold'),ore('nuggetGold'),ore('nuggetGold')])

