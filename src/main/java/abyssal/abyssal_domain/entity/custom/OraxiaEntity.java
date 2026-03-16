package abyssal.abyssal_domain.entity.custom;

import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class OraxiaEntity extends ThrownItemEntity {
    public OraxiaEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public  OraxiaEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.Oraxia_Projectile, livingEntity, world);

    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.Oraxia;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void onDamaged(DamageSource damageSource) {
        super.onDamaged(damageSource);
    }
}
