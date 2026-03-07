package abyssal.abyssal_domain.datagen;

import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    protected ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.Chiseled_Gilded_Ruby_Block);
        addDrop(ModBlocks.Chiseled_Gilded_Ruby_Slab, slabDrops(ModBlocks.Chiseled_Gilded_Ruby_Slab));
        addDrop(ModBlocks.Chiseled_Gilded_Ruby_Stairs);
        addDrop(ModBlocks.Chiseled_Gilded_Ruby_Wall);
        addDrop(ModBlocks.Chiseled_Gilded_Ruby_Button);
        addDrop(ModBlocks.Chiseled_Gilded_Ruby_Pressure_Plate);
        addDrop(ModBlocks.Block_Gilded_Ruby);
        addDrop(ModBlocks.Gilded_Ruby_Bricks_Slab, slabDrops(ModBlocks.Gilded_Ruby_Bricks_Slab));
        addDrop(ModBlocks.Gilded_Ruby_Bricks_Stairs);
        addDrop(ModBlocks.Gilded_Ruby_Bricks_Wall);
        addDrop(ModBlocks.Gilded_Ruby_Bricks_Button);
        addDrop(ModBlocks.Gilded_Ruby_Bricks_Pressure_Plate);
        addDrop(ModBlocks.Gilded_Ruby_Bricks);
        addDrop(ModBlocks.Gilded_Ruby_Slab, slabDrops(ModBlocks.Gilded_Ruby_Slab));
        addDrop(ModBlocks.Gilded_Ruby_Stairs);
        addDrop(ModBlocks.Gilded_Ruby_Wall);
        addDrop(ModBlocks.Gilded_Ruby_Button);
        addDrop(ModBlocks.Gilded_Ruby_Pressure_Plate);

        addDrop(ModBlocks.Gilded_Block_Ore, copperLikeOreDrops(ModBlocks.Gilded_Block_Ore, ModItems.Raw_Gilded_Ruby));
        addDrop(ModBlocks.Deepslate_Gilded_Block_Ore, copperLikeOreDrops(ModBlocks.Gilded_Block_Ore, ModItems.Raw_Gilded_Ruby));
    }
    public LootTable.Builder copperLikeOreDrops(Block drop, Item item) {
        return dropsWithSilkTouch(drop, this.applyExplosionDecay(drop,
                ItemEntry.builder(item)
                        .apply(SetCountLootFunction
                                .builder(UniformLootNumberProvider
                                        .create(1.0F, 3.0F)))
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))));
    }

}
