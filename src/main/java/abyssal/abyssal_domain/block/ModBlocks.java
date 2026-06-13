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

    public static final Block Daedric_ore = registerBlock("daedric_ore",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE).sounds(BlockSoundGroup.NETHER_ORE)));



//Crêpe Myrtle Tree

    public static final Block Crepe_Myrtle_Log = registerBlock("crepe_myrtle_log",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.CHERRY_WOOD).sounds(BlockSoundGroup.CHERRY_WOOD)));

    public static final Block Crepe_Myrtle_Wood = registerBlock("crepe_myrtle_wood",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.CHERRY_WOOD).sounds(BlockSoundGroup.CHERRY_WOOD)));

    public static final Block Stripped_Crepe_Myrtle_Wood = registerBlock("stripped_crepe_myrtle_wood",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.CHERRY_WOOD).sounds(BlockSoundGroup.CHERRY_WOOD)));
    public static final Block Stripped_Crepe_Myrtle_Log = registerBlock("stripped_crepe_myrtle_log",
            new PillarBlock(FabricBlockSettings.copyOf(Blocks.CHERRY_WOOD).sounds(BlockSoundGroup.CHERRY_WOOD)));

    public static final Block Crepe_Myrtle_Leaves = registerBlock("crepe_myrtle_leaves",
            new LeavesBlock(FabricBlockSettings.copyOf(Blocks.CHERRY_LEAVES).sounds(BlockSoundGroup.CHERRY_LEAVES).nonOpaque()));




    public static final NightLibWoodenSet Crepe_Myrtle_PLANKS =
            new NightLibWoodenSet(
                    "abyssal_domain",
                    "crepe_myrtle_planks",
                    FabricBlockSettings.copyOf(Blocks.CHERRY_PLANKS),
                    WoodType.CHERRY,
                    BlockSetType.CHERRY
            );




    //Gilded Ruby Blocks

    public static final NightLibStoneSet BLOCK_GILDED_RUBY =
        new NightLibStoneSet(
                "abyssal_domain",
                "block_gilded_ruby",
                FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK),
                BlockSetType.STONE
        );


    public static final NightLibStoneSet DAEDRIC_BLOCK =
        new NightLibStoneSet(
                "abyssal_domain",
                "daedric_block",
                FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK),
                BlockSetType.STONE
        );


    //Gilded Ruby Bricks

    public static final NightLibStoneSet GILDED_RUBY_BRICKS =
            new NightLibStoneSet(
                    "abyssal_domain",
                    "gilded_ruby_bricks",
                    FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK),
                    BlockSetType.STONE
            );

//Chiseled Gilded Ruby Blocks
public static final NightLibStoneSet CHISELED_GILDED_RUBY_BLOCK =
        new NightLibStoneSet(
                "abyssal_domain",
                "chiseled_gilded_ruby_block",
                FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK),
                BlockSetType.STONE
        );





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
