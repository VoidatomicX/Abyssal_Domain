package abyssal.abyssal_domain.item.custom;

import abyssal.abyssal_domain.network.ModPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class ChainOfJudgment extends SwordItem {

    private static final int COOLDOWN_TICKS = 0;
    private static final int CHANNEL_DURATION = 180;
    private static final float INSTAKILL_HEIGHT = 20f;
    private static final double ZONE_RADIUS = 15.0;

    public ChainOfJudgment(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        
        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        NbtCompound nbt = stack.getOrCreateNbt();
        
        if (nbt.contains("TargetUUID") && nbt.contains("IsChanneling")) {
            nbt.remove("TargetUUID");
            nbt.putBoolean("IsChanneling", false);
            broadcastClearChainEffects(world, player);
            return TypedActionResult.success(stack);
        }

        if (!world.isClient) {
            Vec3d look = player.getRotationVec(1.0f);
            Vec3d eyePos = player.getEyePos();
            Vec3d currentPos = eyePos;
            Vec3d currentDir = look;

            for (int i = 0; i < 50; i++) {
                currentPos = currentPos.add(currentDir.multiply(0.5));

                for (Entity entity : world.getOtherEntities(player, new Box(currentPos, currentPos).expand(0.5))) {
                    if (entity instanceof LivingEntity living && entity != player && entity instanceof PlayerEntity targetPlayer) {
                        nbt.putString("TargetUUID", living.getUuid().toString());
                        nbt.putBoolean("IsChanneling", true);
                        nbt.putInt("ChannelTimer", CHANNEL_DURATION);
                        nbt.putInt("TargetEntityId", entity.getId());
                        
                        player.getItemCooldownManager().set(this, COOLDOWN_TICKS);
                        
                        broadcastChainStartEffects(world, player, targetPlayer);
                        
                        return TypedActionResult.success(stack);
                    }
                }
            }
        }
        
        return TypedActionResult.fail(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        if (world.isClient || !(entity instanceof PlayerEntity player)) return;
        
        NbtCompound nbt = stack.getOrCreateNbt();
        
        if (nbt.getBoolean("IsChanneling") && nbt.contains("TargetUUID") && nbt.contains("ChannelTimer")) {
            int timer = nbt.getInt("ChannelTimer");
            timer--;
            
            int entityId = nbt.getInt("TargetEntityId");
            Entity target = world.getEntityById(entityId);
            
            if (target == null || !(target instanceof PlayerEntity) || !target.isAlive()) {
                nbt.putBoolean("IsChanneling", false);
                nbt.remove("TargetUUID");
                broadcastClearChainEffects(world, player);
                return;
            }
            
            enforceZone(world, player, (PlayerEntity) target);
            
            if (timer <= 0) {
                executeJudgment(world, player, (PlayerEntity) target, stack);
                nbt.putBoolean("IsChanneling", false);
                nbt.remove("TargetUUID");
                broadcastClearChainEffects(world, player);
                return;
            }
            
            nbt.putInt("ChannelTimer", timer);
            
            broadcastChainTickEffects(world, player, target, timer);
            
            if (timer % 20 == 0) {
                world.playSound(null, target.getBlockPos(), SoundEvents.BLOCK_CONDUIT_ACTIVATE, target.getSoundCategory(), 0.5f, 2.0f);
            }
        }
    }
    
    private void enforceZone(World world, PlayerEntity caster, PlayerEntity target) {
        Vec3d casterPos = caster.getPos();
        Vec3d targetPos = target.getPos();
        
        double dx = targetPos.x - casterPos.x;
        double dz = targetPos.z - casterPos.z;
        double dist = Math.sqrt(dx * dx + dz * dz);
        
        if (dist > ZONE_RADIUS) {
            double scale = ZONE_RADIUS / dist;
            target.setPosition(
                    casterPos.x + dx * scale,
                    targetPos.y,
                    casterPos.z + dz * scale
            );
            target.velocityModified = true;
        }
    }
    
    private void executeJudgment(World world, PlayerEntity caster, PlayerEntity target, ItemStack stack) {
        if (!world.isClient) {
            target.setPosition(target.getX(), INSTAKILL_HEIGHT, target.getZ());
            target.damage(world.getDamageSources().playerAttack(caster), 1000f);
            
            world.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_WITHER_DEATH, target.getSoundCategory(), 1.0f, 0.5f);
            
            String deathMessage = "§c" + target.getName().getString() + " §7was judged by §c" + caster.getName().getString() + "§7!";
            
            broadcastJudgmentEffects(world, caster, target, deathMessage);
        }
    }
    
    private void broadcastChainStartEffects(World world, PlayerEntity caster, PlayerEntity target) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        
        ServerPlayerEntity serverCaster = (ServerPlayerEntity) caster;
        ServerPlayerEntity serverTarget = (ServerPlayerEntity) target;
        
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(target.getId());
        buf.writeBoolean(true);
        ServerPlayNetworking.send(serverCaster, ModPackets.CHAIN_EFFECTS, buf);
        
        buf = PacketByteBufs.create();
        buf.writeInt(caster.getId());
        buf.writeBoolean(false);
        buf.writeInt(CHANNEL_DURATION);
        ServerPlayNetworking.send(serverTarget, ModPackets.JUDGMENT_SYNC, buf);
        
        for (PlayerEntity nearby : serverWorld.getServer().getPlayerManager().getPlayerList()) {
            if (nearby.distanceTo(caster) < 50 || nearby.distanceTo(target) < 50) {
                if (nearby != serverCaster && nearby != serverTarget) {
                    buf = PacketByteBufs.create();
                    buf.writeInt(target.getId());
                    buf.writeBoolean(false);
                    buf.writeInt(CHANNEL_DURATION);
                    ServerPlayNetworking.send((ServerPlayerEntity) nearby, ModPackets.JUDGMENT_SYNC, buf);
                }
            }
        }
    }
    
    private void broadcastChainTickEffects(World world, PlayerEntity caster, Entity target, int timer) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        
        ServerPlayerEntity serverCaster = (ServerPlayerEntity) caster;
        
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(target.getId());
        buf.writeBoolean(false);
        buf.writeInt(timer);
        ServerPlayNetworking.send(serverCaster, ModPackets.CHAIN_TICK, buf);
        
        for (PlayerEntity nearby : serverWorld.getServer().getPlayerManager().getPlayerList()) {
            if (nearby.distanceTo(caster) < 50 || nearby.distanceTo(target) < 50) {
                buf = PacketByteBufs.create();
                buf.writeInt(target.getId());
                buf.writeBoolean(false);
                buf.writeInt(timer);
                ServerPlayNetworking.send((ServerPlayerEntity) nearby, ModPackets.JUDGMENT_SYNC, buf);
            }
        }
    }
    
    private void broadcastClearChainEffects(World world, PlayerEntity caster) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        
        ServerPlayerEntity serverCaster = (ServerPlayerEntity) caster;
        
        PacketByteBuf buf = PacketByteBufs.create();
        ServerPlayNetworking.send(serverCaster, ModPackets.CLEAR_CHAIN_EFFECTS, buf);
        
        for (PlayerEntity nearby : serverWorld.getServer().getPlayerManager().getPlayerList()) {
            if (nearby.distanceTo(caster) < 50) {
                ServerPlayNetworking.send((ServerPlayerEntity) nearby, ModPackets.CLEAR_CHAIN_EFFECTS, buf);
            }
        }
    }
    
    private void broadcastJudgmentEffects(World world, PlayerEntity caster, PlayerEntity target, String deathMessage) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        
        for (PlayerEntity nearby : serverWorld.getServer().getPlayerManager().getPlayerList()) {
            if (nearby.distanceTo(caster) < 50 || nearby.distanceTo(target) < 50) {
                if (nearby == target) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeString(deathMessage);
                    ServerPlayNetworking.send((ServerPlayerEntity) nearby, ModPackets.CUSTOM_DEATH, buf);
                }
                
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeDouble(target.getX());
                buf.writeDouble(INSTAKILL_HEIGHT);
                buf.writeDouble(target.getZ());
                ServerPlayNetworking.send((ServerPlayerEntity) nearby, ModPackets.JUDGMENT_EFFECT, buf);
            }
        }
    }
    
    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.getOrCreateNbt().getBoolean("IsChanneling");
    }
    
    @Override
    public int getItemBarStep(ItemStack stack) {
        if (!stack.getOrCreateNbt().getBoolean("IsChanneling")) return 0;
        int timer = stack.getOrCreateNbt().getInt("ChannelTimer");
        return 13 - (13 * timer / CHANNEL_DURATION);
    }
    
    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x8800ff;
    }
}
