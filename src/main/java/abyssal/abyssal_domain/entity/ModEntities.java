package abyssal.abyssal_domain.entity;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.entity.custom.GoobichthysEntity;
import abyssal.abyssal_domain.entity.custom.MirrorTridentEntity;
import abyssal.abyssal_domain.entity.custom.OrbitTridentEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
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

    public static final EntityType<MirrorTridentEntity> MIRROR_TRIDENT =
            Registry.register(Registries.ENTITY_TYPE,
                    Identifier.of("abyssal", "mirror_trident"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, MirrorTridentEntity::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .build());

    public static final EntityType<OrbitTridentEntity> ORBIT_TRIDENT =
            Registry.register(Registries.ENTITY_TYPE,
                    Identifier.of("abyssal", "orbit_trident"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, OrbitTridentEntity::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .build());


}
