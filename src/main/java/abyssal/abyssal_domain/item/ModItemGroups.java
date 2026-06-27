package abyssal.abyssal_domain.item;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.item.util.CyclingIcon;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;


public class ModItemGroups {

    private static final CyclingIcon ITEMS_ICON = new CyclingIcon(List.of(
            ModItems.Gilded_Ruby,
            ModItems.Raw_Gilded_Ruby,
            ModItems.Gilded_Ruby_Sword,
            ModItems.Terminus_Est,
            ModItems.Scythe
    ));

    private static final CyclingIcon BLOCKS_ICON = new CyclingIcon(
            ModBlocks.ALL_BLOCKS.stream().map(b -> (ItemConvertible) b).toList()
    );

    public static final ItemGroup Gilded_Ruby_Group = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier(Abyssal_domain.MOD_ID, "gildeditems"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.gilded.items"))
                    .icon(ITEMS_ICON::get)
                    .entries((displayContext, entries) -> {

                        Registries.ITEM.forEach(item -> {
                            Identifier id = Registries.ITEM.getId(item);

                            if (id != null && id.getNamespace().equals(Abyssal_domain.MOD_ID)) {
                                entries.add(item);
                            }
                        });

                    })
                    .build()
    );


    public static final ItemGroup Gilded_Ruby_Blocks_Group = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier(Abyssal_domain.MOD_ID, "gildedblocks"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.gilded.blocks"))
                    .icon(BLOCKS_ICON::get)
                    .entries((displayContext, entries) -> {

                        ModBlocks.ALL_BLOCKS.forEach(block ->
                                entries.add(block.asItem())
                        );

                        ModBlocks.BLOCK_GILDED_RUBY.addAllBlocksToEntries(entries);
                        ModBlocks.GILDED_RUBY_BRICKS.addAllBlocksToEntries(entries);
                        ModBlocks.CHISELED_GILDED_RUBY_BLOCK.addAllBlocksToEntries(entries);
                        ModBlocks.Crepe_Myrtle_PLANKS.addAllBlocksToEntries(entries);

                    })
                    .build()
    );

    public static void registerItemGroups() {
        Abyssal_domain.LOGGER.info("Registering Item Groups for " + Abyssal_domain.MOD_ID);
    }
}
