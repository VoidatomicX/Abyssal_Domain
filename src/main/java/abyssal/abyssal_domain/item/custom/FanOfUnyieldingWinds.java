package abyssal.abyssal_domain.item.custom;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FanOfUnyieldingWinds extends Item {

    private static final double RANGE = 5.0;
    private static final double KNOCKBACK = 1.2;

    public FanOfUnyieldingWinds(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        Vec3d look = player.getRotationVec(1.0F);
        Vec3d start = player.getEyePos();
        Vec3d end = start.add(look.multiply(RANGE));
        Box area = new Box(start, end).expand(1.0);

        if (!world.isClient) {
            for (Entity entity : world.getOtherEntities(player, area)) {
                if (entity instanceof LivingEntity) {
                    entity.addVelocity(look.x * KNOCKBACK, look.y * KNOCKBACK, look.z * KNOCKBACK);
                    entity.velocityModified = true;
                }
            }
        } else {
            for (int i = 0; i < 15; i++) {
                double offsetX = (world.random.nextDouble() - 0.5) * 1.5;
                double offsetY = (world.random.nextDouble() - 0.5) * 1.0;
                double offsetZ = (world.random.nextDouble() - 0.5) * 1.5;
                Vec3d particlePos = start.add(look.multiply(world.random.nextDouble() * RANGE))
                        .add(offsetX, offsetY, offsetZ);
                world.addParticle(ParticleTypes.CLOUD, particlePos.x, particlePos.y, particlePos.z, look.x * 0.1, look.y * 0.1, look.z * 0.1);
            }
            world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
        }

        player.swingHand(hand, true);
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}