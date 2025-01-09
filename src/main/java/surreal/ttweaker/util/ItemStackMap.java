package surreal.ttweaker.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.item.ItemStack;

public class ItemStackMap<V> extends Object2ObjectOpenCustomHashMap<ItemStack, V> {

    private final V defaultValue;

    public ItemStackMap(V defaultValue) {
        super(HashStrategies.ITEMSTACK_STRATEGY);
        this.defaultValue = defaultValue;
    }

    @Override
    public V get(Object k) {
        V value = super.get(k);
        return value == null ? this.defaultValue : value;
    }
}
