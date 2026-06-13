package abyssal.abyssal_domain.item.custom;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class FanOfUnyieldingWinds extends MiningToolItem {

    private static final double RANGE = 5;
    private static final double KNOCKBACK = 1.4;
    private static final int MAX_CHARGES = 6;
    private static final int RECHARGE_TICKS = 100; // 5 seconds
    private static final int COOLDOWN_TICKS = 0;  // 1 second

        public FanOfUnyieldingWinds(ToolMaterial material, float damage, float speed, Settings settings) {
            super(damage, speed, material, BlockTags.HOE_MINEABLE, settings);
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

        // Block usage if no charges or item on cooldown
        if (charges <= 0) {
            if (!world.isClient) player.sendMessage(Text.of("OverHeated!"), true);
            return TypedActionResult.fail(stack);
        }

        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        // Reduce charges and start cooldown
        charges--;
        nbt.putInt("Charges", charges);
        player.getItemCooldownManager().set(this, COOLDOWN_TICKS);

        // Knockback logic (server-side)
        if (!world.isClient) {
            Vec3d look = player.getRotationVec(1.0F);
            Vec3d start = player.getEyePos();
            Vec3d end = start.add(look.multiply(RANGE));
            Box area = new Box(start, end).expand(1.0);

            for (Entity entity : world.getOtherEntities(player, area)) {
                if (entity instanceof LivingEntity) {
                    entity.addVelocity(look.x * KNOCKBACK, look.y * KNOCKBACK, look.z * KNOCKBACK);
                    entity.velocityModified = true;
                }
            }
        } else {
            // Client-side particle and sound
            Vec3d look = player.getRotationVec(1.0F);
            Vec3d start = player.getEyePos();

            for (int i = 0; i < 15; i++) {
                double offsetX = (world.random.nextDouble() - 0.5) * 1.5;
                double offsetY = (world.random.nextDouble() - 0.5) * 1.0;
                double offsetZ = (world.random.nextDouble() - 0.5) * 1.5;
                Vec3d particlePos = start.add(look.multiply(world.random.nextDouble() * RANGE))
                        .add(offsetX, offsetY, offsetZ);
                world.addParticle(ParticleTypes.CLOUD, particlePos.x, particlePos.y, particlePos.z,
                        look.x * 0.1, look.y * 0.1, look.z * 0.1);
            }

            world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_SAND_BREAK,
                    SoundCategory.PLAYERS, 1.0F, 1.0F, false);
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