package abyssal.abyssal_domain.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ParticleSwitcher extends Item {

    private static final List<ParticleType<?>> PARTICLES = new ArrayList<>();
    private static int currentIndex = 0;

    static {
        for (ParticleType<?> particle : Registries.PARTICLE_TYPE) {
            if (particle instanceof ParticleEffect) {
                PARTICLES.add(particle);
            }
        }
    }

    public ParticleSwitcher(Settings settings) {
        super(settings);
    }

    private ParticleEffect getEffectSafe(ParticleType<?> type) {
        if (type instanceof ParticleEffect effect) return effect;
        return ParticleTypes.FLAME;
    }

    private void spawnParticle(World world, Vec3d pos, ParticleType<?> type) {
        ParticleEffect effect = getEffectSafe(type);
        world.addParticle(effect, pos.x, pos.y, pos.z, 0, 0, 0);
    }

    private void sendSelectedParticleMessage(World world, PlayerEntity player) {
        if (!world.isClient) {
            String particleName = Registries.PARTICLE_TYPE.getId(PARTICLES.get(currentIndex)).toString();
            player.sendMessage(Text.literal("Selected Particle: " + particleName), true);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.isSneaking()) {
            currentIndex = (currentIndex + 1) % PARTICLES.size();
            sendSelectedParticleMessage(world, user);
            return TypedActionResult.success(stack, world.isClient);
        } else {
            Vec3d look = user.getRotationVector().multiply(2);
            Vec3d spawnPos = user.getPos().add(look).add(0, 1, 0);
            spawnParticle(world, spawnPos, PARTICLES.get(currentIndex));
            return TypedActionResult.success(stack, world.isClient);
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        ItemStack stack = context.getStack();

        if (player == null) return ActionResult.PASS;

        if (player.isSneaking()) {
            currentIndex = (currentIndex + 1) % PARTICLES.size();
            sendSelectedParticleMessage(world, player);
            return ActionResult.SUCCESS;
        } else {
            Vec3d look = player.getRotationVector().multiply(2);
            Vec3d spawnPos = player.getPos().add(look).add(0, 1, 0);
            spawnParticle(world, spawnPos, PARTICLES.get(currentIndex));
            return ActionResult.SUCCESS;
        }
    }
}