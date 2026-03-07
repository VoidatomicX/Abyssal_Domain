package abyssal.abyssal_domain.datagen;

import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.item.ModItemGroups;
import abyssal.abyssal_domain.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import nightlib.providers.NightLibAutoLangProvider;
import nightlib.providers.NightLibAutoModelProvider;
import nightlib.providers.NightLibBlockTagProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbyssaldomainDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModLootTableProvider::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModRecipeProvider::new);

    }
}