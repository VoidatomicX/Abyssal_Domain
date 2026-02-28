package abyssal.abyssal_domain.block;

import abyssal.abyssal_domain.Abyssal_domain;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks{

    public static final Block Block_Gilded_Ruby = registerBlock("block_gilded_ruby",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Chiseled_Gilded_Ruby_Block = registerBlock("chiseled_gilded_ruby_block",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Gilded_Ruby_Bricks = registerBlock("gilded_ruby_bricks",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Gilded_Ruby_Bars = registerBlock("gilded_ruby_bars",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).sounds(BlockSoundGroup.NETHERITE)));

    public static final Block Deepslate_Gilded_Block_Ore = registerBlock("deepslate_gilded_block_ore",
            new Block(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_DIAMOND_ORE).sounds(BlockSoundGroup.DEEPSLATE)));

    public static final Block Gilded_Block_Ore = registerBlock("gilded_block_ore",
            new Block(FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE).sounds(BlockSoundGroup.STONE)));




    private static void addItemsToNaturalBlocksTabItemGroup(FabricItemGroupEntries entries) {

        entries.add(Block_Gilded_Ruby);
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
