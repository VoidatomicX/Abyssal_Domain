package abyssal.abyssal_domain.datagen;

import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateModelGenerator.BlockTexturePool gildedrubypool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.Block_Gilded_Ruby);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.Deepslate_Gilded_Block_Ore);
        BlockStateModelGenerator.BlockTexturePool gildedrubybrickspool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.Gilded_Ruby_Bricks);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.Gilded_Block_Ore);
        BlockStateModelGenerator.BlockTexturePool chiseledgildedrubyspool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.Chiseled_Gilded_Ruby_Block);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.Gilded_Ruby_Bars);

        gildedrubypool.stairs(ModBlocks.Gilded_Ruby_Stairs);
        gildedrubypool.slab(ModBlocks.Gilded_Ruby_Slab);
        gildedrubypool.wall(ModBlocks.Gilded_Ruby_Wall);
        gildedrubypool.button(ModBlocks.Gilded_Ruby_Button);
        gildedrubypool.pressurePlate(ModBlocks.Gilded_Ruby_Pressure_Plate);

        chiseledgildedrubyspool.stairs(ModBlocks.Chiseled_Gilded_Ruby_Stairs);
        chiseledgildedrubyspool.slab(ModBlocks.Chiseled_Gilded_Ruby_Slab);
        chiseledgildedrubyspool.wall(ModBlocks.Chiseled_Gilded_Ruby_Wall);
        chiseledgildedrubyspool.button(ModBlocks.Chiseled_Gilded_Ruby_Button);
        chiseledgildedrubyspool.pressurePlate(ModBlocks.Chiseled_Gilded_Ruby_Pressure_Plate);

        gildedrubybrickspool.stairs(ModBlocks.Gilded_Ruby_Bricks_Stairs);
        gildedrubybrickspool.slab(ModBlocks.Gilded_Ruby_Bricks_Slab);
        gildedrubybrickspool.wall(ModBlocks.Gilded_Ruby_Bricks_Wall);
        gildedrubybrickspool.button(ModBlocks.Gilded_Ruby_Bricks_Button);
        gildedrubybrickspool.pressurePlate(ModBlocks.Gilded_Ruby_Bricks_Pressure_Plate);


    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.Gilded_Ruby, Models.GENERATED);
        itemModelGenerator.register(ModItems.Raw_Gilded_Ruby, Models.GENERATED);

        itemModelGenerator.register(ModItems.Gilded_Ruby_Pickaxe, Models.HANDHELD);
        itemModelGenerator.register(ModItems.Gilded_Ruby_Axe, Models.HANDHELD);
        itemModelGenerator.register(ModItems.Gilded_Ruby_Hoe, Models.HANDHELD);
        itemModelGenerator.register(ModItems.Gilded_Ruby_Shovel, Models.HANDHELD);
    }
}
