package surreal.ttweaker.utils;

import it.unimi.dsi.fastutil.Hash;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class HashStrategies {

    public static final Hash.Strategy<ItemStack> ITEMSTACK_STRATEGY = new Hash.Strategy<ItemStack>() {

        @Override
        public int hashCode(ItemStack stack) {
            int i = stack.getItem().hashCode() << 13;
            i |= (stack.getMetadata() + 1) << 31;
            if (stack.hasTagCompound()) {
                i |= Objects.requireNonNull(stack.getTagCompound()).hashCode() << 17;
            }
            return i;
        }

        @Override
        public boolean equals(ItemStack a, ItemStack b) {
            if (a == null || b == null) return false;
            if (a.isEmpty() || b.isEmpty()) return false;
            return a.getItem() == b.getItem() && (a.getMetadata() == b.getMetadata() || b.getMetadata() == Short.MAX_VALUE) && ((a.hasTagCompound() == b.hasTagCompound()) && (a.getTagCompound() == b.getTagCompound()));
        }
    };
}
