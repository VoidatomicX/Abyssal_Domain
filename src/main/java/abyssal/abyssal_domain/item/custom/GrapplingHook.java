package abyssal.abyssal_domain.item.custom;

import abyssal.abyssal_domain.network.ModPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GrapplingHook extends Item {

    private static final int COOLDOWN_TICKS = 10;
    private static final int MAX_HOOK_DISTANCE = 40;
    private static final double PULL_SPEED = 2.0;
    private static final double SWING_FORCE = 1.2;
    private static final int HOOK_DURATION = 100;
    private static final int ROPE_LENGTH = 8;

    public GrapplingHook() {
        super(new net.fabricmc.fabric.api.item.v1.FabricItemSettings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        
        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        NbtCompound nbt = stack.getOrCreateNbt();
        boolean isHooked = nbt.getBoolean("IsHooked");

        if (isHooked) {
            nbt.putBoolean("IsReleasing", true);
            broadcastGrappleRelease(world, player);
            return TypedActionResult.success(stack);
        }

        if (!world.isClient) {
            Vec3d look = player.getRotationVec(1.0f);
            Vec3d eyePos = player.getEyePos();
            
            Vec3d currentPos = eyePos;
            Vec3d currentDir = look;
            
            for (int i = 0; i < MAX_HOOK_DISTANCE; i++) {
                currentPos = currentPos.add(currentDir);
                
                BlockPos blockPos = BlockPos.ofFloored(currentPos);
                
                if (!world.isAir(blockPos) && world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty() == false) {
                    nbt.putBoolean("IsHooked", true);
                    nbt.putBoolean("IsReleasing", false);
                    nbt.putDouble("HookX", blockPos.getX() + 0.5);
                    nbt.putDouble("HookY", blockPos.getY() + 0.5);
                    nbt.putDouble("HookZ", blockPos.getZ() + 0.5);
                    nbt.putInt("HookTimer", HOOK_DURATION);
                    nbt.putDouble("StartX", player.getX());
                    nbt.putDouble("StartY", player.getY());
                    nbt.putDouble("StartZ", player.getZ());
                    
                    player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
                    
                    world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, player.getSoundCategory(), 0.5f, 1.5f);
                    
                    broadcastGrappleStart(world, player, blockPos);
                    
                    return TypedActionResult.success(stack);
                }
            }
        }
        
        return TypedActionResult.fail(stack);
    }

    public static void tickHook(World world, PlayerEntity player, ItemStack stack) {
        if (world.isClient) return;
        
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.getBoolean("IsHooked")) return;
        
        int timer = nbt.getInt("HookTimer");
        timer--;
        
        if (timer <= 0 || !player.isAlive()) {
            unhook(world, player, stack);
            return;
        }
        
        nbt.putInt("HookTimer", timer);
        
        boolean isReleasing = nbt.getBoolean("IsReleasing");
        
        Vec3d targetPos;
        boolean hookedEntity = nbt.getBoolean("HookedEntity");
        
        if (hookedEntity) {
            int entityId = nbt.getInt("HookEntityId");
            Entity entity = world.getEntityById(entityId);
            if (entity == null || !entity.isAlive()) {
                unhook(world, player, stack);
                return;
            }
            targetPos = entity.getPos().add(0, 0.5, 0);
        } else {
            double x = nbt.getDouble("HookX");
            double y = nbt.getDouble("HookY");
            double z = nbt.getDouble("HookZ");
            targetPos = new Vec3d(x, y, z);
        }
        
        Vec3d playerPos = player.getPos();
        double distance = playerPos.distanceTo(targetPos);
        
        if (isReleasing) {
            if (distance > 1.5) {
                Vec3d direction = targetPos.subtract(playerPos).normalize();
                player.setVelocity(direction.multiply(PULL_SPEED));
                player.velocityModified = true;
            } else {
                unhook(world, player, stack);
            }
        } else {
            boolean tooFar = distance > ROPE_LENGTH;
            boolean tooClose = distance < 2.0;
            
            if (tooClose) {
                Vec3d away = playerPos.subtract(targetPos).normalize();
                player.setVelocity(away.multiply(0.5));
                player.velocityModified = true;
            } else if (tooFar) {
                Vec3d toward = targetPos.subtract(playerPos).normalize();
                double excess = distance - ROPE_LENGTH;
                player.setVelocity(toward.multiply(excess * 0.3));
                player.velocityModified = true;
            }
            
            spawnHookParticles(world, playerPos, targetPos);
        }
    }
    
    private static void unhook(World world, PlayerEntity player, ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putBoolean("IsHooked", false);
        nbt.putBoolean("IsReleasing", false);
        nbt.remove("HookPos");
        nbt.remove("HookedEntity");
        broadcastGrappleEnd(world, player);
    }
    
    private static void broadcastGrappleStart(World world, PlayerEntity player, BlockPos hookPos) {
        if (!(world instanceof net.minecraft.server.world.ServerWorld serverWorld)) return;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        
        for (PlayerEntity nearby : serverWorld.getServer().getPlayerManager().getPlayerList()) {
            if (nearby.distanceTo(player) < 50) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(true);
                buf.writeInt(hookPos.getX());
                buf.writeInt(hookPos.getY());
                buf.writeInt(hookPos.getZ());
                ServerPlayNetworking.send((ServerPlayerEntity) nearby, ModPackets.GRAPPLE_SWING, buf);
            }
        }
    }
    
    private static void broadcastGrappleRelease(World world, PlayerEntity player) {
        if (!(world instanceof net.minecraft.server.world.ServerWorld serverWorld)) return;
        
        ItemStack held = player.getMainHandStack();
        if (!(held.getItem() instanceof GrapplingHook)) {
            held = player.getOffHandStack();
        }
        NbtCompound nbt = held.getOrCreateNbt();
        int hx = nbt.getInt("HookX");
        int hy = nbt.getInt("HookY");
        int hz = nbt.getInt("HookZ");
        
        for (PlayerEntity nearby : serverWorld.getServer().getPlayerManager().getPlayerList()) {
            if (nearby.distanceTo(player) < 50) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(true);
                buf.writeInt(hx);
                buf.writeInt(hy);
                buf.writeInt(hz);
                ServerPlayNetworking.send((ServerPlayerEntity) nearby, ModPackets.GRAPPLE_SWING, buf);
            }
        }
    }
    
    private static void broadcastGrappleEnd(World world, PlayerEntity player) {
        if (!(world instanceof net.minecraft.server.world.ServerWorld serverWorld)) return;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        
        for (PlayerEntity nearby : serverWorld.getServer().getPlayerManager().getPlayerList()) {
            if (nearby.distanceTo(player) < 50) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(false);
                ServerPlayNetworking.send((ServerPlayerEntity) nearby, ModPackets.GRAPPLE_SWING, buf);
            }
        }
    }
    
    private static void spawnHookParticles(World world, Vec3d start, Vec3d end) {
        Vec3d dir = end.subtract(start).normalize();
        double dist = start.distanceTo(end);
        int segments = (int)(dist / 0.5);
        
        for (int i = 0; i < segments; i++) {
            Vec3d pos = start.add(dir.multiply(i * 0.5));
            if (world.random.nextBoolean()) {
                world.addParticle(net.minecraft.particle.ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, 0, 0, 0);
            }
        }
    }
}