package abyssal.abyssal_domain.util;

import abyssal.abyssal_domain.entity.ModEntities;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.BiomeKeys;

public class ModSpawns {

    public static void register(){

        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.DESERT),
                SpawnGroup.CREATURE,
                ModEntities.GOOBICHTHYS,
                8,
                1,
                3
        );
    }
}