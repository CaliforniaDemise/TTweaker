// Brewing Stand Fuel
// Remove or add fuels to Brewing Stand.

brewingfuel.remove(item('minecraft:blaze_powder'))
// brewingfuel.removeAll()

brewingfuel.add(item('minecraft:feather'))
brewingfuel.add(ore('blockCoal'), 3600)
brewingfuel.add(item('minecraft:coal'), 400)