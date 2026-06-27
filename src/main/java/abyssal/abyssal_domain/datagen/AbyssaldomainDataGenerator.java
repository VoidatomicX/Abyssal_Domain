package abyssal.abyssal_domain.datagen;

import abyssal.abyssal_domain.datagen.CombinedDataProviders.CombinedBlockTagProvider;
import abyssal.abyssal_domain.datagen.CombinedDataProviders.CombinedModelProvider;
import abyssal.abyssal_domain.world.ModConfiguredFeatures;
import abyssal.abyssal_domain.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import nightlib.providers.NightLibAutoLangProvider;
import nightlib.providers.NightLibAutoModelProvider;
import nightlib.providers.NightLibBlockTagProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbyssaldomainDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(CombinedModelProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModLootTableProvider::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModWorldGenerator::new);
        pack.addProvider(ModBlockTagProvider::new);

    }
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::boostrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::boostrap);
    }
}