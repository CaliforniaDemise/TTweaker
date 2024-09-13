package surreal.ttweaker.utils;

import com.google.common.base.Objects;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.item.ItemStack;

public class HashStrategies {

    public static final Hash.Strategy<ItemStack> ITEMSTACK_STRATEGY = new Hash.Strategy() {

        @Override
        public int hashCode(Object o) {
            if (!(o instanceof ItemStack)) return Objects.hashCode(o);
            ItemStack stack = (ItemStack) o;
            int i = stack.getItem().hashCode() << 13;
            i |= (stack.getMetadata() + 1) << 31;
            if (stack.hasTagCompound()) {
                i |= stack.getTagCompound().toString().hashCode() << 17;
            }
            return i;
        }

        @Override
        public boolean equals(Object o1, Object o2) {
            if (o1 == null || o2 == null) return false;
            if (!(o1 instanceof ItemStack) || !(o2 instanceof ItemStack)) return o1.equals(o2);
            ItemStack a = (ItemStack) o1;
            ItemStack b = (ItemStack) o2;
            if (a.isEmpty() || b.isEmpty()) return false;
            return a.getItem() == b.getItem() && (a.getMetadata() == b.getMetadata() || b.getMetadata() == Short.MAX_VALUE) && ((a.hasTagCompound() == b.hasTagCompound()) && (a.getTagCompound() == b.getTagCompound()));
        }
    };
}
