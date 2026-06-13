package abyssal.abyssal_domain.item;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.item.custom.*;
import abyssal.abyssal_domain.item.custom.trident.OraxiaItem;
import abyssal.abyssal_domain.item.custom.trident.VorunaItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item Gilded_Ruby = registerItem("gilded_ruby",
            new Item(new FabricItemSettings()));
    public static final Item Raw_Gilded_Ruby = registerItem("raw_gilded_ruby",
            new Item(new FabricItemSettings()));

    public static final Item Daedric_Ingot = registerItem("daedric_ingot",
            new Item(new FabricItemSettings()));
    public static final Item Raw_Daedric = registerItem("raw_daedric",
            new Item(new FabricItemSettings()));



    public static final Item GOOB_BUCKET = Registry.register(
            Registries.ITEM,
            new Identifier(Abyssal_domain.MOD_ID,"goob_bucket"),
            new GoobBucketItem(new Item.Settings().maxCount(1))
    );

    public static final Item FAN_OF_UNYIELDING_WINDS = registerItem(
            "fan_of_unyielding_winds",
            new FanOfUnyieldingWinds(ToolMaterials.NETHERITE,-3,-2.2f, new FabricItemSettings()));

    public static final Item Terminus_Est = registerItem(
            "terminus_est",
            new Terminus_Est(ToolMaterials.NETHERITE,5,-3.3f, new FabricItemSettings()));



    public static final Item Gilded_Ruby_Sword = registerItem("gilded_ruby_sword",
            new SwordItem(ModToolMaterial.GILDEDRUBY,4,-2.4f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Battleaxe = registerItem("gilded_ruby_battleaxe",
            new AxeItem(ModToolMaterial.GILDEDRUBY,6,-3.2f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Greatsword = registerItem("gilded_ruby_greatsword",
            new SwordItem(ModToolMaterial.GILDEDRUBY,7,-3f, new FabricItemSettings()));

    public static final Item Oraxia = registerItem("oraxia",
            new OraxiaItem(new FabricItemSettings()));

    public static final Item Voruna = registerItem("voruna",
            new VorunaItem( new FabricItemSettings()));

    public static final Item Gilded_Ruby_Hoe = registerItem("gilded_ruby_hoe",
            new HoeItem(ModToolMaterial.GILDEDRUBY,-3,0f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Axe = registerItem("gilded_ruby_axe",
            new AxeItem(ModToolMaterial.GILDEDRUBY,5,-2.9f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Shovel = registerItem("gilded_ruby_shovel",
            new ShovelItem(ModToolMaterial.GILDEDRUBY,1,-3f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Pickaxe = registerItem("gilded_ruby_pickaxe",
            new PickaxeItem(ModToolMaterial.GILDEDRUBY,1,-2.8f, new FabricItemSettings()));




    public static final Item Goobichthys_Spawn_Egg = registerItem("goobichthys_spawn_egg",
            new SpawnEggItem(ModEntities.GOOBICHTHYS, 0xab6518,0x3b260f, new FabricItemSettings()));


    public static final Item Daedric_Sword = registerItem("daedric_sword",
            new SwordItem(ModToolMaterial.DAEDRIC,4,-2.4f, new FabricItemSettings()));

    public static final Item Daedric_Battleaxe = registerItem("daedric_battleaxe",
            new AxeItem(ModToolMaterial.DAEDRIC,6,-3.2f, new FabricItemSettings()));

    public static final Item Daedric_Greatsword = registerItem("daedric_greatsword",
            new SwordItem(ModToolMaterial.DAEDRIC,7,-3f, new FabricItemSettings()));

    public static final Item Daedric_Hoe = registerItem("daedric_hoe",
            new HoeItem(ModToolMaterial.DAEDRIC,-3,0f, new FabricItemSettings()));

    public static final Item Daedric_Axe = registerItem("daedric_axe",
            new AxeItem(ModToolMaterial.DAEDRIC,5,-2.9f, new FabricItemSettings()));

    public static final Item Daedric_Shovel = registerItem("daedric_shovel",
            new ShovelItem(ModToolMaterial.DAEDRIC,1,-3f, new FabricItemSettings()));

    public static final Item Daedric_Pickaxe = registerItem("daedric_pickaxe",
            new PickaxeItem(ModToolMaterial.DAEDRIC,1,-2.8f, new FabricItemSettings()));




    private static Item registerItem(String name, Item item) {
    return Registry.register(Registries.ITEM, new Identifier(Abyssal_domain.MOD_ID, name), item);
}

    public static void registerModItems() {
        Abyssal_domain.LOGGER.info("Registering Mod Items for " + Abyssal_domain.MOD_ID);
    }
}
