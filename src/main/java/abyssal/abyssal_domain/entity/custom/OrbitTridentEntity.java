package abyssal.abyssal_domain.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class OrbitTridentEntity extends Entity {

    private int index;
    private int total;
    private double radius;

    private float angle;
    private int life;

    private static final int MAX_LIFE = 20 * 15;
    private static final float SPEED = 0.18f;

    private final HashMap<UUID, Integer> hitCooldown = new HashMap<>();
    private static final int HIT_DELAY = 10;

    private int spawnGracePeriod = 5;

    public OrbitTridentEntity(EntityType<?> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    public void init(LivingEntity owner, int index, int total, double radius) {

        this.index = index;
        this.total = total;
        this.radius = radius;

    }
    private UUID ownerUuid;
    private LivingEntity cachedOwner;

    public void setOwner(LivingEntity owner) {
        this.ownerUuid = owner.getUuid();
        this.cachedOwner = owner;
    }

    public LivingEntity getOwnerEntity() {
        if (cachedOwner != null && cachedOwner.isAlive()) {
            return cachedOwner;
        }

        if (ownerUuid != null && this.getWorld() instanceof ServerWorld world) {
            var entity = world.getPlayerByUuid(ownerUuid);
            if (entity != null) {
                cachedOwner = entity;
            }
        }

        return cachedOwner;
    }
    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    public void tick() {
        if (spawnGracePeriod > 0) {
            spawnGracePeriod--;
            super.tick();
            return;
        }

        LivingEntity owner = getOwnerEntity();

        if (owner == null || !owner.isAlive()) {
            this.discard();
            return;
        }

        life++;
        if (life >= MAX_LIFE) {
            this.discard();
            return;
        }

        super.tick();

        angle += SPEED;

        double base = (Math.PI * 2 / total) * index;
        double offset = base + angle;

        Vec3d center = owner.getPos().add(0, 1.2, 0);

        double x = center.x + Math.cos(offset) * radius;
        double z = center.z + Math.sin(offset) * radius;
        double y = center.y + Math.sin(angle * 2) * 0.25;

        this.setPosition(x, y, z);

        if (this.getWorld().isClient) return;

        for (LivingEntity target : this.getWorld().getEntitiesByClass(
                LivingEntity.class,
                this.getBoundingBox().expand(1.2),
                e -> e != owner && e.isAlive()
        )) {
            UUID id = target.getUuid();
            int last = hitCooldown.getOrDefault(id, -999);

            if (life - last >= HIT_DELAY) {
                target.damage(this.getWorld().getDamageSources().magic(), 4.0f);
                hitCooldown.put(id, life);
            }
        }
    }


    @Override
    public boolean shouldSave() {
        return false;
    }
}