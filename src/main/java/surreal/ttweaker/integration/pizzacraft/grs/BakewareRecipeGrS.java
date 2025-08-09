package surreal.ttweaker.integration.pizzacraft.grs;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.vanilla.CraftingInfo;
import com.cleanroommc.groovyscript.compat.vanilla.CraftingRecipe;
import com.cleanroommc.groovyscript.compat.vanilla.ICraftingRecipe;
import com.cleanroommc.groovyscript.core.mixin.InventoryCraftingAccess;
import com.cleanroommc.groovyscript.core.mixin.SlotCraftingAccess;
import com.cleanroommc.groovyscript.sandbox.ClosureHelper;
import com.tiviacz.pizzacraft.gui.inventory.InventoryCraftingImproved;
import groovy.lang.Closure;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import surreal.ttweaker.integration.pizzacraft.impl.BakewareRecipe;

public abstract class BakewareRecipeGrS extends BakewareRecipe implements ICraftingRecipe {

    @Nullable protected final Closure<Void> recipeAction;
    @Nullable protected final Closure<ItemStack> recipeFunction;

    protected BakewareRecipeGrS(@NotNull ItemStack output, Object[] input, @Nullable Closure<Void> recipeAction, @Nullable Closure<ItemStack> recipeFunction) {
        super(output, input);
        this.recipeFunction = recipeFunction;
        this.recipeAction = recipeAction;
    }

    @Override
    public @NotNull ItemStack getCraftingResult(@NotNull InventoryCraftingImproved inv) {
        ItemStack output = this.output.copy();
        if (recipeFunction == null || input.length == 0) return output;
        CraftingRecipe.InputList inputs = new CraftingRecipe.InputList();
        for (CraftingRecipe.SlotMatchResult slotMatchResult : getMatchingList(inv)) {
            ItemStack givenInput = slotMatchResult.getGivenInput();
            inputs.add(givenInput);
        }
        // Call recipe function
        ItemStack recipeFunctionResult = ClosureHelper.call(recipeFunction, output, inputs, new CraftingInfo(inv, getPlayerFromInventory(inv)));
        return recipeFunctionResult == null ? output : recipeFunctionResult;
    }

    @Override
    public @NotNull ItemStack getRecipeOutput() {
        return output;
    }

    @Nullable
    @Override
    public Closure<Void> getRecipeAction() {
        return recipeAction;
    }

    @Nullable
    @Override
    public Closure<ItemStack> getRecipeFunction() {
        return recipeFunction;
    }

    public boolean matches(@Nullable IIngredient expectedInput, ItemStack givenInput) {
        return expectedInput == null || expectedInput.test(givenInput);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCraftingImproved inv) {
        NonNullList<ItemStack> result = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (CraftingRecipe.SlotMatchResult matchResult : getMatchingList(inv)) {
            ItemStack input = matchResult.getGivenInput();
            ItemStack remainder = matchResult.getRecipeIngredient().applyTransform(input.copy());
            if (remainder == null) remainder = ItemStack.EMPTY;
            else if (input.getCount() > 1 && !remainder.isEmpty() && ItemStack.areItemsEqual(input, remainder) && ItemStack.areItemStackTagsEqual(input, remainder)) {
                remainder.setCount(1);
            }
            result.set(matchResult.getSlotIndex(), remainder);
        }
        return result;
    }

    @Override
    public boolean matches(InventoryCraftingImproved inv, World world) {
        return !getMatchingList(inv).isEmpty();
    }

    @NotNull
    public abstract CraftingRecipe.MatchList getMatchingList(InventoryCrafting inv);

    // TODO
    private static EntityPlayer getPlayerFromInventory(InventoryCrafting inventory) {
        Container eventHandler = ((InventoryCraftingAccess) inventory).getEventHandler();
        if (eventHandler != null) {
            for (Slot slot : eventHandler.inventorySlots) {
                if (slot instanceof SlotCraftingAccess) {
                    return ((SlotCraftingAccess) slot).getPlayer();
                }
            }
        }
        return null;
    }

    @Override
    public int getRecipeSize() {
        return this.input.length;
    }
}
