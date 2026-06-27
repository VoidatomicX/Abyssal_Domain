package abyssal.abyssal_domain.item.custom;

import abyssal.abyssal_domain.enchants.ModEnchantments;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class Scythe extends SwordItem {

    private static final int COOLDOWN_TICKS = 30;

    public Scythe(ToolMaterials toolMaterials, int i, float v, FabricItemSettings fabricItemSettings) {
        super(toolMaterials, i, v, fabricItemSettings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        var enchants = EnchantmentHelper.get(stack);

        if (enchants.containsKey(Enchantments.FIRE_ASPECT)) {
            if (!world.isClient && world instanceof ServerWorld serverWorld) {
                ScytheSlash(serverWorld, player, 1, 50, 3, 5);
                player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
            }
        } else if (enchants.containsKey(ModEnchantments.POISON)) {
            if (!world.isClient && world instanceof ServerWorld serverWorld) {
                int level = EnchantmentHelper.getLevel(ModEnchantments.POISON, stack);
                ScytheSlash(serverWorld, player, 2, 50, level, 5);
                player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
            }
        } else if (enchants.containsKey(Enchantments.SWEEPING)) {
            if (!world.isClient && world instanceof ServerWorld serverWorld) {
                ScytheSlash(serverWorld, player, 1, 50, 3, 5);
                player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
            }
        } else if (enchants.containsKey(Enchantments.SHARPNESS) || enchants.containsKey(Enchantments.SMITE) || enchants.containsKey(Enchantments.BANE_OF_ARTHROPODS)) {
            if (!world.isClient && world instanceof ServerWorld serverWorld) {
                ScytheSlash(serverWorld, player, 3, 50, 3, 5);
                player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
            }
        }

        return TypedActionResult.success(stack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            target.timeUntilRegen = 0;
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        var enchants = EnchantmentHelper.get(stack);

        if (enchants.containsKey(ModEnchantments.POISON)) {
            tooltip.add(Text.literal("Poison Scythe").formatted(Formatting.GREEN));
        } else if (enchants.containsKey(Enchantments.FIRE_ASPECT)) {
            tooltip.add(Text.literal("Flame Scythe").formatted(Formatting.GOLD));
        } else if (!enchants.isEmpty()) {
            tooltip.add(Text.literal("Scythe").formatted(Formatting.GRAY));
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        var enchants = EnchantmentHelper.get(stack);

        if (enchants.containsKey(ModEnchantments.POISON)) {
            return Text.literal("Poison Scythe").formatted(Formatting.GREEN);
        } else if (enchants.containsKey(Enchantments.FIRE_ASPECT)) {
            return Text.literal("Flame Scythe").formatted(Formatting.RED);
        }

        return super.getName(stack);
    }

    public static void ScytheSlash(ServerWorld world, PlayerEntity player, int mode, float range, float width, float damage) {
        Vec3d origin = player.getPos().add(0, 1.2, 0);
        Vec3d forward = player.getRotationVec(1.0f).normalize();
        Vec3d right = forward.crossProduct(new Vec3d(0, 1, 0)).normalize();

        // MODE 1 — FLAME CRESCENT
        if (mode == 1) {
            Box box = new Box(origin, origin.add(forward.multiply(range))).expand(width);
            for (Entity entity : world.getOtherEntities(player, box)) {
                Vec3d to = entity.getPos().subtract(player.getPos()).normalize();
                if (to.dotProduct(forward) < 0.2) continue;
                entity.damage(world.getDamageSources().playerAttack(player), damage);
                entity.setOnFireFor(5);
            }

            for (int i = 1; i <= (int) range; i++) {
                Vec3d center = origin.add(forward.multiply(i));
                for (int w = -((int) width); w <= width; w++) {
                    Vec3d offset = right.multiply(w);
                    BlockPos pos = BlockPos.ofFloored(center.add(offset));
                    BlockPos above = pos.up();
                    if (world.isAir(above) && world.getBlockState(pos).isSolidBlock(world, pos)) {
                        world.setBlockState(above, Blocks.FIRE.getDefaultState());
                    }
                }
            }

            int steps = 60;
            for (int step = 0; step < steps; step++) {
                double progress = step / (double) steps;
                Vec3d base = origin.add(forward.multiply(progress * range));
                for (int i = 0; i < 28; i++) {
                    double t = i / 28.0;
                    double spread = (t - 0.5) * 2.0;
                    double curve = Math.sin(t * Math.PI);
                    Vec3d point = base
                            .add(right.multiply(spread * width))
                            .add(forward.multiply(curve * 1.2));
                    Vec3d vel = forward.multiply(0.5).add(
                            right.multiply((Math.random() - 0.5) * 0.1));
                    world.spawnParticles(ParticleTypes.FLAME,
                            point.x, point.y, point.z, 0, vel.x, vel.y, vel.z, 0.01);
                    world.spawnParticles(ParticleTypes.SMOKE,
                            point.x, point.y, point.z, 0, vel.x * 0.6, vel.y * 0.6, vel.z * 0.6, 0.01);
                }
            }

            world.playSound(null, player.getBlockPos(),
                    SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                    SoundCategory.PLAYERS, 1.0f, 0.75f);
        }

        // MODE 2 — POISON ORB
        if (mode == 2) {
            Vec3d pos = origin;
            Vec3d vel = forward.multiply(0.4);

            for (int tick = 0; tick < 25; tick++) {
                pos = pos.add(vel);

                world.spawnParticles(ParticleTypes.ENTITY_EFFECT,
                        pos.x, pos.y, pos.z, 2, 0, 0, 0, 0);

                for (Entity e : world.getOtherEntities(player, new Box(pos, pos).expand(0.5))) {
                    if (e instanceof LivingEntity living) {
                        living.addStatusEffect(new StatusEffectInstance(
                                StatusEffects.POISON, (int)(damage * 20), (int)width - 1
                        ));
                    }

                    for (int i = 0; i < 4; i++) {
                        Vec3d fVel = new Vec3d(
                                (Math.random() - 0.5) * 0.6, 0.25, (Math.random() - 0.5) * 0.6);
                        Vec3d p = pos;
                        for (int t = 0; t < 20; t++) {
                            fVel = fVel.add(0, -0.03, 0);
                            p = p.add(fVel);
                            world.spawnParticles(ParticleTypes.ENTITY_EFFECT,
                                    p.x, p.y, p.z, 1, 0, 0, 0, 0);
                            for (Entity e2 : world.getOtherEntities(player, new Box(p, p).expand(0.3))) {
                                if (e2 instanceof LivingEntity living2) {
                                    living2.addStatusEffect(new StatusEffectInstance(
                                            StatusEffects.POISON, 60, 0
                                    ));
                                }
                                return;
                            }
                            if (!world.isAir(BlockPos.ofFloored(p))) return;
                        }
                    }
                    return;
                }

                if (!world.isAir(BlockPos.ofFloored(pos))) {
                    for (int i = 0; i < 4; i++) {
                        Vec3d fVel = new Vec3d(
                                (Math.random() - 0.5) * 0.6, 0.25, (Math.random() - 0.5) * 0.6);
                        Vec3d p = pos;
                        for (int t = 0; t < 20; t++) {
                            fVel = fVel.add(0, -0.03, 0);
                            p = p.add(fVel);
                            world.spawnParticles(ParticleTypes.ENTITY_EFFECT,
                                    p.x, p.y, p.z, 1, 0, 0, 0, 0);
                            if (!world.isAir(BlockPos.ofFloored(p))) return;
                        }
                    }
                    return;
                }
            }
        }

        // MODE 3 — LIFE RAY (fixed: advances properly per tick)
        if (mode == 3) {
            Vec3d dir = forward;
            Vec3d currentPos = origin;

            for (int tick = 0; tick < 100; tick++) {
                for (int i = 0; i < 4; i++) {
                    currentPos = currentPos.add(dir);

                    if (!world.isAir(BlockPos.ofFloored(currentPos))) {
                        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                                currentPos.x, currentPos.y, currentPos.z, 3, 0.1, 0.1, 0.1, 0.02);
                        return;
                    }

                    world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                            currentPos.x, currentPos.y, currentPos.z, 0, 0, 0, 0, 0);

                    for (Entity e : world.getOtherEntities(player, new Box(currentPos, currentPos).expand(0.5))) {
                        if (e instanceof LivingEntity living) {
                            living.damage(world.getDamageSources().playerAttack(player), damage);
                            if (tick % 20 == 0) {
                                player.heal(2.0f);
                            }
                            player.addStatusEffect(new StatusEffectInstance(
                                    StatusEffects.ABSORPTION, 60, 0
                            ));
                            return;
                        }
                    }
                }
            }
        }
    }
}
