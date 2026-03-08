package abyssal.abyssal_domain.datagen;

import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {




        blockStateModelGenerator.registerLog(ModBlocks.Crepe_Myrtle_Log).log(ModBlocks.Crepe_Myrtle_Log).wood(ModBlocks.Crepe_Myrtle_Wood);
        blockStateModelGenerator.registerLog(ModBlocks.Stripped_Crepe_Myrtle_Log).log(ModBlocks.Stripped_Crepe_Myrtle_Log).wood(ModBlocks.Stripped_Crepe_Myrtle_Wood);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.Crepe_Myrtle_Leaves);



    }
    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.Gilded_Ruby, Models.GENERATED);
        itemModelGenerator.register(ModItems.Raw_Gilded_Ruby, Models.GENERATED);

        itemModelGenerator.register(ModItems.Gilded_Ruby_Pickaxe, Models.HANDHELD);
        itemModelGenerator.register(ModItems.Gilded_Ruby_Axe, Models.HANDHELD);
        itemModelGenerator.register(ModItems.Gilded_Ruby_Hoe, Models.HANDHELD);
        itemModelGenerator.register(ModItems.Gilded_Ruby_Shovel, Models.HANDHELD);

        itemModelGenerator.register(ModItems.Goobichthys_Spawn_Egg,
                new Model(Optional.of(new Identifier("item/template_spawn_egg")), Optional.empty()));
    }
}
