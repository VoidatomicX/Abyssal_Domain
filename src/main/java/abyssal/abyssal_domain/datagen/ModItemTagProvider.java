package abyssal.abyssal_domain.datagen;

import abyssal.abyssal_domain.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(ModBlocks.Crepe_Myrtle_PLANKS.planks.asItem());

        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
        .add(ModBlocks.Crepe_Myrtle_Log.asItem())
        .add(ModBlocks.Crepe_Myrtle_Wood.asItem())
        .add(ModBlocks.Crepe_Myrtle_PLANKS.planks.asItem())
        .add(ModBlocks.Stripped_Crepe_Myrtle_Log.asItem())
        .add(ModBlocks.Stripped_Crepe_Myrtle_Wood.asItem());


    }
}
