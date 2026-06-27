package abyssal.abyssal_domain.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class TerminusShieldEntity extends Entity {

    private UUID ownerUuid;
    private int shieldHp = 50;
    private int maxShieldHp = 50;
    private int lifetime = 0;
    private boolean isActive = false;
    private boolean holding = false;

    private static final double RADIUS = 3.0;
    private static final double HEIGHT = 4.0;

    public TerminusShieldEntity(EntityType<?> type, World world) {
        super(type, world);
        setNoGravity(true);
        setInvulnerable(false);
    }

    public void setOwner(PlayerEntity owner) {
        this.ownerUuid = owner.getUuid();
    }

    public void setShieldHp(int hp) {
        this.shieldHp = hp;
        this.maxShieldHp = hp;
    }

    public void addHp(int amount) {
        this.shieldHp = Math.min(this.shieldHp + amount, this.maxShieldHp);
    }

    public void setActive(int lifetimeTicks) {
        this.isActive = true;
        this.lifetime = lifetimeTicks;
        this.shieldHp = 50;
        this.maxShieldHp = 50;
    }

    public int getShieldHp() {
        return shieldHp;
    }

    public int getMaxShieldHp() {
        return maxShieldHp;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public boolean isActive() {
        return isActive;
    }

    public void startHolding() {
        this.holding = true;
        this.shieldHp = 50;
        this.maxShieldHp = 50;
    }

    public boolean isHolding() {
        return holding;
    }

    @Override
    public void tick() {
        super.tick();

        if (getWorld().isClient) {
            spawnClientParticles();
            return;
        }

        if (ownerUuid == null) {
            discard();
            return;
        }

        PlayerEntity owner = getWorld().getPlayerByUuid(ownerUuid);
        if (owner == null || !owner.isAlive()) {
            discard();
            return;
        }

        Vec3d pos = owner.getPos().add(0, 1.0, 0);
        setPosition(pos);

        if (isActive && !holding) {
            lifetime--;
            if (lifetime <= 0) {
                discard();
                return;
            }
        }

        if (shieldHp <= 0) {
            onShatter(owner);
            return;
        }

        Box shieldBox = getShieldBox();

        for (Entity entity : getWorld().getOtherEntities(owner, shieldBox)) {
            if (entity instanceof ProjectileEntity projectile && !projectile.getUuid().equals(ownerUuid)) {
                projectile.setVelocity(0, 0, 0);
                projectile.velocityModified = true;
                projectile.setNoGravity(true);
                shieldHp -= 2;
                getWorld().playSound(null, getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, net.minecraft.sound.SoundCategory.PLAYERS, 0.5f, 1.0f);
                spawnBreakParticles(projectile.getPos());
            }
        }

        for (Entity entity : getWorld().getOtherEntities(owner, shieldBox)) {
            if (entity instanceof LivingEntity living && !entity.getUuid().equals(ownerUuid)) {
                Vec3d knockback = entity.getPos().subtract(pos).normalize().multiply(1.5).add(0, 0.3, 0);
                entity.setVelocity(knockback);
                entity.velocityModified = true;
                shieldHp -= 1;
            }
        }

        if (getWorld().getTime() % 10 == 0) {
            getWorld().playSound(null, getBlockPos(), SoundEvents.BLOCK_BEACON_AMBIENT, net.minecraft.sound.SoundCategory.PLAYERS, 0.3f, 1.5f);
        }
    }

    public boolean tryBlockDamage(float amount) {
        if (shieldHp <= 0) return false;
        shieldHp -= (int) amount;
        if (shieldHp <= 0) {
            PlayerEntity owner = getWorld().getPlayerByUuid(ownerUuid);
            if (owner != null) {
                onShatter(owner);
            }
        }
        return true;
    }

    private void onShatter(PlayerEntity owner) {
        getWorld().playSound(null, getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, net.minecraft.sound.SoundCategory.PLAYERS, 2.0f, 0.5f);
        getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_ITEM_BREAK, net.minecraft.sound.SoundCategory.PLAYERS, 2.0f, 0.8f);

        if (getWorld() instanceof ServerWorld serverWorld) {
            Vec3d p = getPos();
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, p.x, p.y, p.z, 1, 0, 0, 0, 0);
        }
        discard();
    }

    public Box getShieldBox() {
        Vec3d p = getPos();
        return new Box(p.x - RADIUS, p.y - 1, p.z - RADIUS, p.x + RADIUS, p.y + HEIGHT, p.z + RADIUS);
    }

    private void spawnClientParticles() {
        Vec3d p = getPos();
        double time = getWorld().getTime() * 0.05;

        int rings = (isActive || holding) ? 4 : 2;
        for (int ring = 0; ring < rings; ring++) {
            int count = (isActive || holding) ? 16 : 8;
            double yOffset = ring * (HEIGHT / rings) + 0.5;
            for (int i = 0; i < count; i++) {
                double angle = (Math.PI * 2 * i / count) + time + ring;
                double r = RADIUS * 0.95 + Math.sin(time * 2 + i + ring) * 0.1;
                double x = p.x + Math.cos(angle) * r;
                double z = p.z + Math.sin(angle) * r;
                double y = p.y + yOffset + Math.sin(angle * 2 + time) * 0.2;

                if (isActive) {
                    getWorld().addParticle(ParticleTypes.ENCHANT, x, y, z, 0, 0.06, 0);
                    getWorld().addParticle(ParticleTypes.END_ROD, x, y, z, (random.nextDouble() - 0.5) * 0.02, 0.04, (random.nextDouble() - 0.5) * 0.02);
                } else {
                    getWorld().addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, 0, 0.03, 0);
                    getWorld().addParticle(ParticleTypes.WITCH, x, y, z, 0, 0.04, 0);
                }
            }
        }

        if (isActive || holding) {
            for (int i = 0; i < 6; i++) {
                double angle = random.nextDouble() * Math.PI * 2;
                double r = RADIUS * random.nextDouble();
                double x = p.x + Math.cos(angle) * r;
                double z = p.z + Math.sin(angle) * r;
                double y = p.y + random.nextDouble() * HEIGHT;
                getWorld().addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, 0.1, 0);
            }
        }
    }

    private void spawnBreakParticles(Vec3d pos) {
        if (getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 10, 0.3, 0.3, 0.3, 0.1);
        }
    }

    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) ownerUuid = nbt.getUuid("Owner");
        shieldHp = nbt.getInt("ShieldHp");
        maxShieldHp = nbt.getInt("MaxShieldHp");
        lifetime = nbt.getInt("Lifetime");
        isActive = nbt.getBoolean("IsActive");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (ownerUuid != null) nbt.putUuid("Owner", ownerUuid);
        nbt.putInt("ShieldHp", shieldHp);
        nbt.putInt("MaxShieldHp", maxShieldHp);
        nbt.putInt("Lifetime", lifetime);
        nbt.putBoolean("IsActive", isActive);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}
