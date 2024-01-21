// IGame //
// Returns IBlockDefinition from string.
game.getBlock("minecraft:dirt"); // game is IGame

// IWorld //
// Removes the block.
world.setBlockToAir(pos); // world is IWorld, pos is IBlockPos

// Returns an array of loaded entities
world.getLoadedEntities(); // world is IWorld

// ILiquidStack //
// Returns the liquid stack as IData.
lstack.toData(); // lstack is ILiquidStack, data is IData

// Static method
// Loads liquid from data. Keywords are: FluidName (name of the fluid), Amount (fluid amount), Tag (fluids data)
ILiquidStack.fromData(data); // data is IData

// ILiquidDefinition //
// Returns true if liquid can be filled on bucket.
liquid.hasBucket(); // liquid is ILiquidDefinition

// Makes liquid be able to filled to bucket.
liquid.addBucket(); // liquid is ILiquidDefinition

// IItemDefinition //
// Returns IBlockDefinition version of itself if it's a block.
item.toBlock(); // item is IItemDefinition

// Returns IBlockState with the specified meta if it's a block.
item.toBlockState(2); // item is IItemDefinition

// IBlockDefinition //
// Returns IItemDefinition version of itself if it has an item.
block.toItem(); // block is IBlockDefintion

// Returns IItemStack with the specified metadata if it has an item.
block.toItemStack(2); // block is IBlockDefinition
