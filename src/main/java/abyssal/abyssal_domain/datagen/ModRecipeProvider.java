package abyssal.abyssal_domain.datagen;

import abyssal.abyssal_domain.block.ModBlocks;
import abyssal.abyssal_domain.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    private static final List<ItemConvertible> GILDED_SMELTABLES = List.of(ModItems.Raw_Gilded_Ruby,
            ModBlocks.Gilded_Block_Ore, ModBlocks.Deepslate_Gilded_Block_Ore);

    private static final List<ItemConvertible> DAEDRIC_SMELTABLES = List.of(ModItems.Raw_Daedric,
            ModBlocks.Daedric_ore);

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerSmelting(exporter, GILDED_SMELTABLES, RecipeCategory.MISC, ModItems.Gilded_Ruby,
                0.7f, 200, "gildedruby");

        offerBlasting(exporter, GILDED_SMELTABLES, RecipeCategory.MISC, ModItems.Gilded_Ruby,
                0.7f, 200, "gildedruby");

        offerSmelting(exporter, DAEDRIC_SMELTABLES, RecipeCategory.MISC, ModItems.Daedric_Ingot,
                0.7f, 200, "daedric");

        offerBlasting(exporter, DAEDRIC_SMELTABLES, RecipeCategory.MISC, ModItems.Daedric_Ingot,
                0.7f, 200, "daedric");

        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.Gilded_Ruby, RecipeCategory.DECORATIONS,
                ModBlocks.BLOCK_GILDED_RUBY.base);

        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.Daedric_Ingot, RecipeCategory.DECORATIONS,
                ModBlocks.DAEDRIC_BLOCK.base);


        //Corundum
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Gilded_Ruby_Sword, 1)
                .pattern("  R")
                .pattern(" R ")
                .pattern("S  ")
                .input('S', Items.STICK)
                .input('R', ModItems.Gilded_Ruby)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Gilded_Ruby), conditionsFromItem(ModItems.Gilded_Ruby))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Gilded_Ruby_Sword)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Gilded_Ruby_Greatsword, 1)
                .pattern(" RR")
                .pattern("RRR")
                .pattern("SR ")
                .input('S', Items.STICK)
                .input('R', ModItems.Gilded_Ruby)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Gilded_Ruby), conditionsFromItem(ModItems.Gilded_Ruby))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Gilded_Ruby_Greatsword)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Gilded_Ruby_Battleaxe, 1)
                .pattern(" RR")
                .pattern("RSC")
                .pattern("S  ")
                .input('S', Items.STICK)
                .input('C', ModBlocks.BLOCK_GILDED_RUBY.base)
                .input('R', ModItems.Gilded_Ruby)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Gilded_Ruby), conditionsFromItem(ModItems.Gilded_Ruby))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Gilded_Ruby_Battleaxe)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Gilded_Ruby_Axe, 1)
                .pattern("RR ")
                .pattern("RS ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('R', ModItems.Gilded_Ruby)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Gilded_Ruby), conditionsFromItem(ModItems.Gilded_Ruby))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Gilded_Ruby_Axe)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Gilded_Ruby_Pickaxe, 1)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('R', ModItems.Gilded_Ruby)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Gilded_Ruby), conditionsFromItem(ModItems.Gilded_Ruby))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Gilded_Ruby_Pickaxe)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Gilded_Ruby_Shovel, 1)
                .pattern(" R ")
                .pattern(" S ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('R', ModItems.Gilded_Ruby)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Gilded_Ruby), conditionsFromItem(ModItems.Gilded_Ruby))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Gilded_Ruby_Shovel)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Gilded_Ruby_Hoe, 1)
                .pattern("RR ")
                .pattern(" S ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('R', ModItems.Gilded_Ruby)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Gilded_Ruby), conditionsFromItem(ModItems.Gilded_Ruby))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Gilded_Ruby_Hoe)));


        //DAEDRIC
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Daedric_Sword, 1)
                .pattern("  R")
                .pattern(" R ")
                .pattern("S  ")
                .input('S', Items.STICK)
                .input('R', ModItems.Daedric_Ingot)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Daedric_Ingot), conditionsFromItem(ModItems.Daedric_Ingot))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Daedric_Sword)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Daedric_Greatsword, 1)
                .pattern(" RR")
                .pattern("RRR")
                .pattern("SR ")
                .input('S', Items.STICK)
                .input('R', ModItems.Daedric_Ingot)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Daedric_Ingot), conditionsFromItem(ModItems.Daedric_Ingot))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Daedric_Greatsword)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Daedric_Battleaxe, 1)
                .pattern(" RR")
                .pattern("RSC")
                .pattern("S  ")
                .input('S', Items.STICK)
                .input('C', ModBlocks.DAEDRIC_BLOCK.base)
                .input('R', ModItems.Daedric_Ingot)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Daedric_Ingot), conditionsFromItem(ModItems.Daedric_Ingot))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Daedric_Battleaxe)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Daedric_Axe, 1)
                .pattern("RR ")
                .pattern("RS ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('R', ModItems.Daedric_Ingot)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Daedric_Ingot), conditionsFromItem(ModItems.Daedric_Ingot))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Daedric_Axe)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Daedric_Pickaxe, 1)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('R', ModItems.Daedric_Ingot)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Daedric_Ingot), conditionsFromItem(ModItems.Daedric_Ingot))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Daedric_Pickaxe)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Daedric_Shovel, 1)
                .pattern(" R ")
                .pattern(" S ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('R', ModItems.Daedric_Ingot)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Daedric_Ingot), conditionsFromItem(ModItems.Daedric_Ingot))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Daedric_Shovel)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.Daedric_Hoe, 1)
                .pattern("RR ")
                .pattern(" S ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('R', ModItems.Daedric_Ingot)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .criterion(hasItem(ModItems.Daedric_Ingot), conditionsFromItem(ModItems.Daedric_Ingot))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.Daedric_Hoe)));


        //Woods
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.Crepe_Myrtle_PLANKS.stairs, 4)
                .pattern("R  ")
                .pattern("RR ")
                .pattern("RRR")
                .input('R', ModBlocks.Crepe_Myrtle_PLANKS.planks)
                .criterion(hasItem(ModBlocks.Crepe_Myrtle_PLANKS.planks), conditionsFromItem(ModBlocks.Crepe_Myrtle_PLANKS.planks))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.Crepe_Myrtle_PLANKS.stairs)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.Crepe_Myrtle_PLANKS.slab, 6)
                .pattern("   ")
                .pattern("RRR")
                .pattern("   ")
                .input('R', ModBlocks.Crepe_Myrtle_PLANKS.planks)
                .criterion(hasItem(ModBlocks.Crepe_Myrtle_PLANKS.planks), conditionsFromItem(ModBlocks.Crepe_Myrtle_PLANKS.planks))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.Crepe_Myrtle_PLANKS.slab)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.Crepe_Myrtle_PLANKS.button, 6)
                .pattern("R  ")
                .pattern("   ")
                .pattern("   ")
                .input('R', ModBlocks.Crepe_Myrtle_PLANKS.planks)
                .criterion(hasItem(ModBlocks.Crepe_Myrtle_PLANKS.planks), conditionsFromItem(ModBlocks.Crepe_Myrtle_PLANKS.planks))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.Crepe_Myrtle_PLANKS.button)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.Crepe_Myrtle_PLANKS.door, 3)
                .pattern("RR ")
                .pattern("RR ")
                .pattern("RR ")
                .input('R', ModBlocks.Crepe_Myrtle_PLANKS.planks)
                .criterion(hasItem(ModBlocks.Crepe_Myrtle_PLANKS.planks), conditionsFromItem(ModBlocks.Crepe_Myrtle_PLANKS.planks))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.Crepe_Myrtle_PLANKS.door)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.Crepe_Myrtle_PLANKS.pressurePlate, 1)
                .pattern("   ")
                .pattern("RR ")
                .pattern("   ")
                .input('R', ModBlocks.Crepe_Myrtle_PLANKS.planks)
                .criterion(hasItem(ModBlocks.Crepe_Myrtle_PLANKS.planks), conditionsFromItem(ModBlocks.Crepe_Myrtle_PLANKS.planks))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.Crepe_Myrtle_PLANKS.pressurePlate)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.Crepe_Myrtle_PLANKS.trapdoor, 2)
                .pattern("RRR")
                .pattern("RRR")
                .pattern("   ")
                .input('R', ModBlocks.Crepe_Myrtle_PLANKS.planks)
                .criterion(hasItem(ModBlocks.Crepe_Myrtle_PLANKS.planks), conditionsFromItem(ModBlocks.Crepe_Myrtle_PLANKS.planks))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.Crepe_Myrtle_PLANKS.trapdoor)));

    }
}

