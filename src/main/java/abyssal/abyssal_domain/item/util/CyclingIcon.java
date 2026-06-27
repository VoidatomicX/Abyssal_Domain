package abyssal.abyssal_domain.item.util;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CyclingIcon {
    private final List<ItemConvertible> items;
    private int index = 0;
    private long lastUpdate = 0;

    private static final long INTERVAL = 5000;

    public CyclingIcon(List<ItemConvertible> items) {
        this.items = items;
    }

    public ItemStack get() {
        long now = System.currentTimeMillis();

        if (now - lastUpdate > INTERVAL) {
            lastUpdate = now;

            index++;
            if (index >= items.size()) index = 0;
        }

        return new ItemStack(items.get(index));
    }
}