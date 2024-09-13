package surreal.ttweaker.core;

import com.tiviacz.pizzacraft.init.ModItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraftforge.oredict.OreDictionary;
import surreal.ttweaker.crafttweaker.BrewingFuel;
import surreal.ttweaker.utils.ItemStackMap;

import javax.annotation.Nonnull;
import java.util.Map;

public class TTHooks {

    public static Map<ItemStack, ItemStack> createMap() {
        return new ItemStackMap<>(ItemStack.EMPTY);
    }

    public static boolean isPeel(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() == ModItems.PEEL) return true;
        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (int id : oreIds) {
            if (id == OreDictionary.getOreID("toolPeel")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isKnife(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() == ModItems.KNIFE) return true;
        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (int id : oreIds) {
            if (id == OreDictionary.getOreID("toolKnife")) {
                return true;
            }
        }
        return false;
    }

    public static void handleFuel(TileEntityBrewingStand tile, ItemStack fuel) {
        int time = BrewingFuel.getTime(fuel);
        if (time > 0) {
            tile.setField(1, time);
            fuel.shrink(1);
            tile.markDirty();
        }
    }

    public static boolean isFuel(ItemStack fuel) {
        return BrewingFuel.hasKey(fuel);
    }

    public static class FuelSlot extends Slot {

        public FuelSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return isFuel(stack);
        }

        @Override
        public int getSlotStackLimit() {
            return 64;
        }
    }

    /*
    *
    *             methodVisitor.visitLabel(label5);
            methodVisitor.visitLineNumber(393, label5);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", "BLAZE_POWDER", "Lnet/minecraft/item/Item;");
            Label label6 = new Label();
            methodVisitor.visitJumpInsn(IF_ACMPNE, label6);
            methodVisitor.visitInsn(ICONST_1);
            Label label7 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label7);
            methodVisitor.visitLabel(label6);
            methodVisitor.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"net/minecraft/item/Item"}, 0, null);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitLabel(label7);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
            methodVisitor.visitInsn(IRETURN);
    * */
}
