package abyssal.abyssal_domain.item;

import abyssal.abyssal_domain.Abyssal_domain;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item Gilded_Ruby = registerItem("gilded_ruby", new Item(new FabricItemSettings()));
    public static final Item Raw_Gilded_Ruby = registerItem("raw_gilded_ruby", new Item(new FabricItemSettings()));












    private static void addItemsToIngredintTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(Gilded_Ruby);
        entries.add(Raw_Gilded_Ruby);
    }


private static Item registerItem(String name, Item item) {
    return Registry.register(Registries.ITEM, new Identifier(Abyssal_domain.MOD_ID, name), item);
}

    public static void registerModItems() {
        Abyssal_domain.LOGGER.info("Registering Mod Items for " + Abyssal_domain.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredintTabItemGroup);
    }
}
