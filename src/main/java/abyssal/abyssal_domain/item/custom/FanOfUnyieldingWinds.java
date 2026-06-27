package abyssal.abyssal_domain.item.custom;

import abyssal.abyssal_domain.enchants.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FanOfUnyieldingWinds extends Item {

    private static final double RANGE = 5.0;
    private static final double KNOCKBACK = 1.2;
    private static final double SELFKNOCKBACK = 0.2;
    private static final int MAX_CHARGES = 6;
    private static final int RECHARGE_TICKS = 100; // 5 seconds
    private static final int COOLDOWN_TICKS = 0;  // 1 second

    public FanOfUnyieldingWinds(Settings settings) {
        super(settings);
    }



    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity) {
            NbtCompound nbt = stack.getOrCreateNbt();

            if (!nbt.contains("Charges")) nbt.putInt("Charges", MAX_CHARGES);
            if (!nbt.contains("RechargeTimer")) nbt.putInt("RechargeTimer", 0);

            int charges = nbt.getInt("Charges");
            int rechargeTimer = nbt.getInt("RechargeTimer");

            // Recharge logic
            if (charges < MAX_CHARGES) {
                rechargeTimer++;
                if (rechargeTimer >= RECHARGE_TICKS) {
                    charges++;
                    rechargeTimer = 0;
                }
                nbt.putInt("Charges", charges);
                nbt.putInt("RechargeTimer", rechargeTimer);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        NbtCompound nbt = stack.getOrCreateNbt();
        int charges = nbt.getInt("Charges");

        // Get enchant level
        int level = EnchantmentHelper.getLevel(ModEnchantments.GUST, stack);

        // Scaling values
        float knockbackMultiplier = 1.0f + (level * 0.35f);
        float selfKnockbackMultiplier = 1.0f + (level * 0.5f);
        int particleCount = 15 + (level * 12);

        // Block usage if no charges or cooldown
        if (charges <= 0) {
            if (!world.isClient) player.sendMessage(Text.of("OverHeated!"), true);
            return TypedActionResult.fail(stack);
        }

        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        // Reduce charges + cooldown
        charges--;
        nbt.putInt("Charges", charges);
        player.getItemCooldownManager().set(this, COOLDOWN_TICKS);

        if (!world.isClient) {
            // =========================
            // SERVER SIDE LOGIC
            // =========================

            Vec3d look = player.getRotationVec(1.0F);
            Vec3d start = player.getEyePos();
            Vec3d end = start.add(look.multiply(RANGE));
            Vec3d unlook = player.getRotationVector().multiply(-1);

            // Self knockback scaling
            player.addVelocity(
                    unlook.x * SELFKNOCKBACK * selfKnockbackMultiplier,
                    unlook.y * SELFKNOCKBACK * selfKnockbackMultiplier,
                    unlook.z * SELFKNOCKBACK * selfKnockbackMultiplier
            );
            player.velocityModified = true;

            Box area = new Box(start, end).expand(1.0 + (level * 0.2));

            for (Entity entity : world.getOtherEntities(player, area)) {
                if (entity instanceof LivingEntity living) {
                    living.addVelocity(
                            look.x * KNOCKBACK * knockbackMultiplier,
                            look.y * KNOCKBACK * knockbackMultiplier,
                            look.z * KNOCKBACK * knockbackMultiplier
                    );
                    living.velocityModified = true;
                }
            }

        } else {
            // =========================
            // CLIENT SIDE VISUALS
            // =========================

            Vec3d look = player.getRotationVec(1.0F);
            Vec3d start = player.getEyePos();

            // Main particle burst
            for (int i = 0; i < particleCount; i++) {
                double offsetX = (world.random.nextDouble() - 0.5) * (1.5 + level * 0.3);
                double offsetY = (world.random.nextDouble() - 0.5) * (1.0 + level * 0.2);
                double offsetZ = (world.random.nextDouble() - 0.5) * (1.5 + level * 0.3);

                Vec3d particlePos = start
                        .add(look.multiply(world.random.nextDouble() * RANGE))
                        .add(offsetX, offsetY, offsetZ);

                world.addParticle(
                        ParticleTypes.CLOUD,
                        particlePos.x, particlePos.y, particlePos.z,
                        look.x * 0.15 * level,
                        look.y * 0.15 * level,
                        look.z * 0.15 * level
                );
            }

            // Base sound
            world.playSound(
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BLOCK_SAND_BREAK,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F + (level * 0.1f),
                    false
            );

            // =========================
            // SHOCKWAVE TIERS
            // =========================

            // Tier 1: small burst
            if (level >= 1) {
                world.addParticle(ParticleTypes.POOF, start.x, start.y, start.z, 0, 0.1, 0);
            }

            // Tier 2: explosion flash
            if (level >= 2) {
                world.addParticle(ParticleTypes.EXPLOSION, start.x, start.y, start.z, 0, 0, 0);
            }

            // Tier 3: sonic-style burst
            if (level >= 3) {
                world.addParticle(ParticleTypes.SONIC_BOOM, start.x, start.y, start.z, 0, 0, 0);
            }

            // Tier 4: expanding ring shockwave
            if (level >= 4) {
                int ringPoints = 24;
                double radius = 1.5 + level * 0.4;

                for (int i = 0; i < ringPoints; i++) {
                    double angle = (Math.PI * 2) * (i / (double) ringPoints);

                    Vec3d ring = new Vec3d(
                            Math.cos(angle),
                            0.1,
                            Math.sin(angle)
                    ).multiply(radius);

                    world.addParticle(
                            ParticleTypes.CLOUD,
                            start.x + ring.x,
                            start.y,
                            start.z + ring.z,
                            ring.x * 0.2,
                            0.05,
                            ring.z * 0.2
                    );
                }
            }

            // Tier 5: heavy shockwave burst
            if (level >= 5) {
                for (int i = 0; i < 30; i++) {
                    double angle = world.random.nextDouble() * Math.PI * 2;
                    double radius = 1.0 + world.random.nextDouble() * 2.5;

                    Vec3d dir = new Vec3d(
                            Math.cos(angle),
                            world.random.nextDouble() * 0.5,
                            Math.sin(angle)
                    ).multiply(radius);

                    world.addParticle(
                            ParticleTypes.CLOUD,
                            start.x,
                            start.y,
                            start.z,
                            dir.x * 0.2,
                            dir.y * 0.1,
                            dir.z * 0.2
                    );
                }
            }
        }

        player.swingHand(hand, true);
        return TypedActionResult.success(stack);
    }

    // ---- ITEM BAR METHODS ----

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int charges = stack.getOrCreateNbt().getInt("Charges");
        // Fill starts full and decreases with usage
        return 13 * (MAX_CHARGES - charges) / MAX_CHARGES;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xff9300;
    }
}