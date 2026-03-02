package abyssal.abyssal_domain.item;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.item.custom.Terminus_Est;
import abyssal.abyssal_domain.item.custom.FanOfUnyieldingWinds;
import abyssal.abyssal_domain.item.custom.ParticleSwitcher;
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

    public static final Item PARTICLE_SWITCHER = registerItem("particle_switcher",
            new ParticleSwitcher(new FabricItemSettings()));

    //public static final TestGlint GREEN_GLINT =
       //     (TestGlint) registerItem("green_glint",
      //              new TestGlint(0x00FF00));

    //public static final TestGlint BLUE_GLINT =
     //       (TestGlint) registerItem("blue_glint",
      //              new TestGlint(0x0000FF));

    //public static final TestGlint RED_GLINT =
     //       (TestGlint) registerItem("red_glint",
     //               new TestGlint(0xFF0000));

    public static final Item FAN_OF_UNYIELDING_WINDS = registerItem(
            "fan_of_unyielding_winds",
            new FanOfUnyieldingWinds(new FabricItemSettings().maxCount(1))
    );
    public static final Item Terminus_Est = registerItem(
            "terminus_est",
            new Terminus_Est(ToolMaterials.NETHERITE,5,-3.3f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Sword = registerItem("gilded_ruby_sword",
            new SwordItem(ToolMaterials.NETHERITE,4,-2.4f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Hoe = registerItem("gilded_ruby_hoe",
            new HoeItem(ToolMaterials.NETHERITE,-3,0f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Axe = registerItem("gilded_ruby_axe",
            new AxeItem(ToolMaterials.NETHERITE,5,-2.9f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Shovel = registerItem("gilded_ruby_shovel",
            new ShovelItem(ToolMaterials.NETHERITE,1,-3f, new FabricItemSettings()));

    public static final Item Gilded_Ruby_Pickaxe = registerItem("gilded_ruby_pickaxe",
            new PickaxeItem(ToolMaterials.NETHERITE,1,-2.8f, new FabricItemSettings()));

private static Item registerItem(String name, Item item) {
    return Registry.register(Registries.ITEM, new Identifier(Abyssal_domain.MOD_ID, name), item);
}

    public static void registerModItems() {
        Abyssal_domain.LOGGER.info("Registering Mod Items for " + Abyssal_domain.MOD_ID);
    }
}
