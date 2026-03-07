package abyssal.abyssal_domain.block;

import abyssal.abyssal_domain.Abyssal_domain;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import nightlib.bases.NightLibStoneSet;
import nightlib.bases.NightLibWoodenSet;

public class ModBlocks{

    public static final Block Gilded_Ruby_Bars = registerBlock("gilded_ruby_bars",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Deepslate_Gilded_Block_Ore = registerBlock("deepslate_gilded_block_ore",
            new Block(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_DIAMOND_ORE).sounds(BlockSoundGroup.DEEPSLATE)));

    public static final Block Gilded_Block_Ore = registerBlock("gilded_block_ore",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE).sounds(BlockSoundGroup.STONE)));

    public static final Block Block_Gilded_Ruby = registerBlock("block_gilded_ruby",
            new Block(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Chiseled_Gilded_Ruby_Block = registerBlock("chiseled_gilded_ruby_block",
            new Block(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Gilded_Ruby_Bricks = registerBlock("gilded_ruby_bricks",
            new Block(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Crepe_Myrtle_Leaves = registerBlock("crepe_myrtle_leaves",
            new LeavesBlock(FabricBlockSettings.copyOf(Blocks.CHERRY_LEAVES).sounds(BlockSoundGroup.CHERRY_LEAVES).nonOpaque()));

    public static final Block Crepe_Myrtle_Log = registerBlock("crepe_myrtle_log",
            new Block(FabricBlockSettings.copyOf(Blocks.CHERRY_WOOD).sounds(BlockSoundGroup.CHERRY_WOOD)));

//Gilded Ruby Blocks
    public static final Block Gilded_Ruby_Stairs = registerBlock("gilded_ruby_stairs",
            new StairsBlock(ModBlocks.Block_Gilded_Ruby.getDefaultState(), FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));
    public static final Block Gilded_Ruby_Slab = registerBlock("gilded_ruby_slab",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Gilded_Ruby_Button = registerBlock("gilded_ruby_button",
            new ButtonBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE), BlockSetType.IRON, 10, true));
    public static final Block Gilded_Ruby_Pressure_Plate = registerBlock("gilded_ruby_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
                    FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE), BlockSetType.IRON));

    public static final Block Gilded_Ruby_Wall = registerBlock("gilded_ruby_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));


    //Gilded Ruby Bricks
    public static final Block Gilded_Ruby_Bricks_Stairs = registerBlock("gilded_ruby_bricks_stairs",
            new StairsBlock(ModBlocks.Block_Gilded_Ruby.getDefaultState(), FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));
    public static final Block Gilded_Ruby_Bricks_Slab = registerBlock("gilded_ruby_bricks_slab",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Gilded_Ruby_Bricks_Button = registerBlock("gilded_ruby_bricks_button",
            new ButtonBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE), BlockSetType.IRON, 10, true));
    public static final Block Gilded_Ruby_Bricks_Pressure_Plate = registerBlock("gilded_ruby_bricks_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
                    FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE), BlockSetType.IRON));

    public static final Block Gilded_Ruby_Bricks_Wall = registerBlock("gilded_ruby_bricks_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));


//Chiseled Gilded Ruby Blocks
    public static final Block Chiseled_Gilded_Ruby_Stairs = registerBlock("chiseled_gilded_ruby_stairs",
            new StairsBlock(ModBlocks.Block_Gilded_Ruby.getDefaultState(), FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));
    public static final Block Chiseled_Gilded_Ruby_Slab = registerBlock("chiseled_gilded_ruby_slab",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Chiseled_Gilded_Ruby_Button = registerBlock("chiseled_gilded_ruby_button",
            new ButtonBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE), BlockSetType.IRON, 10, true));
    public static final Block Chiseled_Gilded_Ruby_Pressure_Plate = registerBlock("chiseled_gilded_ruby_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
                    FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE), BlockSetType.IRON));

    public static final Block Chiseled_Gilded_Ruby_Wall = registerBlock("chiseled_gilded_ruby_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));




    //public static final Block Block_Gilded_Ruby = registerBlock("block_gilded_ruby",
            //new Block(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));
    //public static final Block Block_Gilded_Ruby = registerBlock("block_gilded_ruby",
            //new Block(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.NETHERITE)));


    private static void addItemsToNaturalBlocksTabItemGroup(FabricItemGroupEntries entries) {

    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Abyssal_domain.MOD_ID, name), block);
    }

    public static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(Abyssal_domain.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        Abyssal_domain.LOGGER.info("Regsitering ModBlocks for " +Abyssal_domain.MOD_ID);
    }
}
