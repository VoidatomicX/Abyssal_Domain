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

        addDrop(ModBlocks.Gilded_Block_Ore, copperLikeOreDrops(ModBlocks.Gilded_Block_Ore, ModItems.Raw_Gilded_Ruby));
        addDrop(ModBlocks.Deepslate_Gilded_Block_Ore, copperLikeOreDrops(ModBlocks.Gilded_Block_Ore, ModItems.Raw_Gilded_Ruby));

        addDrop(ModBlocks.Daedric_ore, daedricLikeOreDrops(ModBlocks.Daedric_ore, ModItems.Raw_Daedric));

        addDrop(ModBlocks.Crepe_Myrtle_Log);
        addDrop(ModBlocks.Crepe_Myrtle_Wood);
        addDrop(ModBlocks.Stripped_Crepe_Myrtle_Log);
        addDrop(ModBlocks.Stripped_Crepe_Myrtle_Wood);


        addDrop(ModBlocks.Crepe_Myrtle_Leaves, leavesDrops(ModBlocks.Crepe_Myrtle_Leaves, ModBlocks.Gilded_Ruby_Bars, 0.0025f));
    }
    public LootTable.Builder copperLikeOreDrops(Block drop, Item item) {
        return dropsWithSilkTouch(drop, this.applyExplosionDecay(drop,
                ItemEntry.builder(item)
                        .apply(SetCountLootFunction
                                .builder(UniformLootNumberProvider
                                        .create(1.0F, 3.0F)))
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))));
    }

    public LootTable.Builder daedricLikeOreDrops(Block drop, Item item) {
        return dropsWithSilkTouch(drop, this.applyExplosionDecay(drop,
                ItemEntry.builder(item)
                        .apply(SetCountLootFunction
                                .builder(UniformLootNumberProvider
                                        .create(1.0F, 2.0F)))
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))));
    }

}
