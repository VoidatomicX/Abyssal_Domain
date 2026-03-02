package abyssal.abyssal_domain.item;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
public static final ItemGroup Gilded_Ruby_Group = Registry.register(Registries.ITEM_GROUP,
        new Identifier(Abyssal_domain.MOD_ID, "gilded"),
        FabricItemGroup.builder().displayName(Text.translatable("itemgroup.gilded"))
                .icon(() -> new ItemStack(ModItems.Gilded_Ruby)).entries((displayContext, entries) -> {
                    entries.add(ModItems.Gilded_Ruby);
                    entries.add(ModItems.Raw_Gilded_Ruby);
                    entries.add(ModBlocks.Deepslate_Gilded_Block_Ore);
                    entries.add(ModBlocks.Gilded_Block_Ore);
                    entries.add(ModBlocks.Gilded_Ruby_Bars);
                    entries.add(ModItems.Gilded_Ruby_Sword);
                    entries.add(ModItems.Gilded_Ruby_Axe);
                    entries.add(ModItems.Gilded_Ruby_Pickaxe);
                    entries.add(ModItems.Gilded_Ruby_Shovel);
                    entries.add(ModItems.Gilded_Ruby_Hoe);
                    entries.add(ModItems.Terminus_Est);
                    entries.add(ModItems.FAN_OF_UNYIELDING_WINDS);

                    ModBlocks.CHISELED_GILDED_RUBY_BLOCK_SET.addAllBlocksToEntries(entries);
                    ModBlocks.BLOCK_GILDED_RUBY_SET.addAllBlocksToEntries(entries);
                    ModBlocks.GILDE_RUBY_BRICKS.addAllBlocksToEntries(entries);
                }).build());


    public static void registerItemGroups() {
        Abyssal_domain.LOGGER.info("Registering Item Groups for " + Abyssal_domain.MOD_ID);
    }
}
