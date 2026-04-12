package abyssal.abyssal_domain.item;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.entity.ModEntities;
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
                        Registries.ITEM.forEach(item -> {
                            Identifier id = Registries.ITEM.getId(item);

                            if (id != null && id.getNamespace().equals(Abyssal_domain.MOD_ID)) {
                                entries.add(item);
                            }
                        });


                        ModBlocks.BLOCK_GILDED_RUBY.addAllBlocksToEntries(entries);

                        ModBlocks.GILDED_RUBY_BRICKS.addAllBlocksToEntries(entries);

                        ModBlocks.CHISELED_GILDED_RUBY_BLOCK.addAllBlocksToEntries(entries);

                        entries.add(ModBlocks.Crepe_Myrtle_Leaves);
                        entries.add(ModBlocks.Crepe_Myrtle_Log);
                        entries.add(ModBlocks.Crepe_Myrtle_Wood);
                        entries.add(ModBlocks.Stripped_Crepe_Myrtle_Log);
                        entries.add(ModBlocks.Stripped_Crepe_Myrtle_Wood);

                        ModBlocks.Crepe_Myrtle_PLANKS.addAllBlocksToEntries(entries);

                    }).build());


    public static void registerItemGroups() {
        Abyssal_domain.LOGGER.info("Registering Item Groups for " + Abyssal_domain.MOD_ID);
    }
}
