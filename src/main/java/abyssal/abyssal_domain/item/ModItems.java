package abyssal.abyssal_domain.item;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.item.custom.*;
import abyssal.abyssal_domain.item.custom.trident.OraxiaItem;
import abyssal.abyssal_domain.item.custom.trident.VorunaItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import static abyssal.abyssal_domain.Abyssal_domain.MOD_ID;

public class ModItems {

    public static final Item Gilded_Ruby = registerItem("gilded_ruby",
            new Item(new FabricItemSettings()));
    public static final Item Raw_Gilded_Ruby = registerItem("raw_gilded_ruby",
            new Item(new FabricItemSettings()));

    public static final Item Daedric_Ingot = registerItem("daedric_ingot",
            new Item(new FabricItemSettings()));
    public static final Item Raw_Daedric = registerItem("raw_daedric",
            new Item(new FabricItemSettings()));

    //public static final TestGlint GREEN_GLINT =
       //     (TestGlint) registerItem("green_glint",
      //              new TestGlint(0x00FF00));

    //public static final TestGlint BLUE_GLINT =
     //       (TestGlint) registerItem("blue_glint",
      //              new TestGlint(0x0000FF));

    //public static final TestGlint RED_GLINT =
     //       (TestGlint) registerItem("red_glint",
     //               new TestGlint(0xFF0000));

    public static final Item GOOB_BUCKET = Registry.register(
            Registries.ITEM,
            new Identifier(MOD_ID, "goob_bucket"),
            new EntityBucketItem(
                    ModEntities.GOOBICHTHYS,
                    Fluids.LAVA,
                    SoundEvents.ITEM_BUCKET_EMPTY_LAVA,
                    new Item.Settings().maxCount(1)
            )
    );

    public static final Item FAN_OF_UNYIELDING_WINDS = registerItem(
            "fan_of_unyielding_winds",
            new FanOfUnyieldingWinds(new FabricItemSettings().maxCount(1))
    );



    public static final Item Terminus_Est = registerItem(
            "terminus_est",
            new Terminus_Est(ToolMaterials.NETHERITE,5,-3.3f, new FabricItemSettings()));



    public static final Item Gilded_Ruby_Sword = registerItem("gilded_ruby_sword",
            new SwordItem(ModToolMaterial.GILDEDRUBY,3,-2.4f, new FabricItemSettings()));

    public static final Item Scythe = registerItem("scythe",
            new Scythe(ToolMaterials.NETHERITE,4,-2.4f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Battleaxe = registerItem("gilded_ruby_battleaxe",
            new BattleAxeItem(ModToolMaterial.GILDEDRUBY,6,-3.2f, new FabricItemSettings()));

    public static final Item Oraxia = registerItem("oraxia",
            new OraxiaItem(new FabricItemSettings().maxCount(1)));

    public static final Item Voruna = registerItem("voruna",
            new VorunaItem( new FabricItemSettings().maxCount(1)));

    public static final Item Grappling_Hook = registerItem("grappling_hook",
            new GrapplingHook());

    public static final Item Chain_of_Judgment = registerItem("chain_of_judgment",
            new ChainOfJudgment(ToolMaterials.NETHERITE, 6, -2.6f, new FabricItemSettings().maxCount(1)));

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
            new SwordItem(ModToolMaterial.DAEDRIC,3,-2.4f, new FabricItemSettings()));

    public static final Item Daedric_Battleaxe = registerItem("daedric_battleaxe",
            new BattleAxeItem(ModToolMaterial.DAEDRIC,5,-3.2f, new FabricItemSettings()));

    public static final Item Daedric_Greatsword = registerItem("daedric_greatsword",
            new GreatSwordItem(ModToolMaterial.DAEDRIC,5,-3f, new FabricItemSettings()));

    public static final Item Daedric_Hoe = registerItem("daedric_hoe",
            new HoeItem(ModToolMaterial.DAEDRIC,-3,0f, new FabricItemSettings()));

    public static final Item Daedric_Axe = registerItem("daedric_axe",
            new AxeItem(ModToolMaterial.DAEDRIC,-4,-2.9f, new FabricItemSettings()));

    public static final Item Daedric_Shovel = registerItem("daedric_shovel",
            new ShovelItem(ModToolMaterial.DAEDRIC,-1,-3f, new FabricItemSettings()));

    public static final Item Daedric_Pickaxe = registerItem("daedric_pickaxe",
            new PickaxeItem(ModToolMaterial.DAEDRIC,-2,-2.8f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Greatsword = registerItem("gilded_ruby_greatsword",
            new GreatSwordItem(ModToolMaterial.GILDEDRUBY,5,-3f, new FabricItemSettings()));



    private static Item registerItem(String name, Item item) {
    return Registry.register(Registries.ITEM, new Identifier(Abyssal_domain.MOD_ID, name), item);
}

    public static void registerModItems() {
        Abyssal_domain.LOGGER.info("Registering Mod Items for " + Abyssal_domain.MOD_ID);
    }
}
