package abyssal.abyssal_domain.entity;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.entity.custom.GoobichthysEntity;
import abyssal.abyssal_domain.entity.custom.OraxiaEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<GoobichthysEntity> GOOBICHTHYS =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(Abyssal_domain.MOD_ID,"goobichthys"),
                    FabricEntityTypeBuilder.create(
                            SpawnGroup.CREATURE,
                            GoobichthysEntity::new
                    ).dimensions(EntityDimensions.fixed(1f,1f)).build());

    public static final EntityType<OraxiaEntity> Oraxia_Projectile = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(Abyssal_domain.MOD_ID, "oraxia_projectile"),
            FabricEntityTypeBuilder.<OraxiaEntity>create(SpawnGroup.MISC, OraxiaEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());
}
