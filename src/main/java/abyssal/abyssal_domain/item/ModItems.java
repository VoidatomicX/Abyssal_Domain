package abyssal.abyssal_domain.item;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.item.custom.ParticleSwitcher;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item Gilded_Ruby = registerItem("gilded_ruby", new Item(new FabricItemSettings()));
    public static final Item Raw_Gilded_Ruby = registerItem("raw_gilded_ruby", new Item(new FabricItemSettings()));

    public static final Item PARTICLE_SWITCHER = registerItem("particle_switcher", new ParticleSwitcher(new FabricItemSettings()));


    public static final Item Gilded_Ruby_Sword = registerItem("gilded_ruby_sword",
            new SwordItem(ToolMaterials.NETHERITE,2,2f, new FabricItemSettings()));









    private static void addItemsToIngredintTabItemGroup(FabricItemGroupEntries entries) {
    }


private static Item registerItem(String name, Item item) {
    return Registry.register(Registries.ITEM, new Identifier(Abyssal_domain.MOD_ID, name), item);
}

    public static void registerModItems() {
        Abyssal_domain.LOGGER.info("Registering Mod Items for " + Abyssal_domain.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredintTabItemGroup);
    }
}
