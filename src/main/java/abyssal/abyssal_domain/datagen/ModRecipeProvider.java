package abyssal.abyssal_domain.datagen;

import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    private static final List<ItemConvertible> GILDED_SMELTABLES = List.of(ModItems.Raw_Gilded_Ruby,
            ModBlocks.Gilded_Block_Ore, ModBlocks.Deepslate_Gilded_Block_Ore);

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerSmelting(exporter, GILDED_SMELTABLES, RecipeCategory.MISC, ModItems.Gilded_Ruby,
                0.7f, 200, "gildedruby");

        offerBlasting(exporter, GILDED_SMELTABLES, RecipeCategory.MISC, ModItems.Gilded_Ruby,
                0.7f, 200, "gildedruby");

        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.Gilded_Ruby, RecipeCategory.DECORATIONS,
                ModBlocks.GILDED_RUBY_BRICKS.base);




    }
}
