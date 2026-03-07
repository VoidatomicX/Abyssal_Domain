package abyssal.abyssal_domain.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.registry.RegistryWrapper;
import nightlib.providers.NightLibAutoModelProvider;
import nightlib.providers.NightLibBlockTagProvider;

import java.util.concurrent.CompletableFuture;

public class CombinedDataProviders {

    public static class CombinedBlockTagProvider extends NightLibBlockTagProvider {
        private final ModBlockTagProvider modProvider;

        public CombinedBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
            this.modProvider = new ModBlockTagProvider(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            super.configure(wrapperLookup);
            modProvider.configure(wrapperLookup);
        }
    }

    public static class CombinedModelProvider extends NightLibAutoModelProvider {
        private final ModModelProvider modProvider;

        public CombinedModelProvider(FabricDataOutput output) {
            super(output);
            this.modProvider = new ModModelProvider(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
            super.generateBlockStateModels(blockStateModelGenerator);
            modProvider.generateBlockStateModels(blockStateModelGenerator);
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            super.generateItemModels(itemModelGenerator);
            modProvider.generateItemModels(itemModelGenerator);
        }
    }
}