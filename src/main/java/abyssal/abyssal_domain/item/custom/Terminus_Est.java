package abyssal.abyssal_domain.item.custom;

import abyssal.abyssal_domain.network.ModPackets;
import abyssal.abyssal_domain.util.ShieldZone;
import abyssal.abyssal_domain.util.ShieldZoneManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class Terminus_Est extends SwordItem {

    private static final int BORDER_RADIUS = 10;
    private static final int BORDER_HEIGHT = 10;
    private static final int COOLDOWN_TICKS = 20;
    private static final int RECHARGE_TICKS = 3200;
    private static final int DISCHARGE_DURATION = 300;
    private static final int MAX_DURABILITY = 100;
    private static final int MAX_CHARGES = 5;
    private static final int MANUAL_CHARGE_TIME = 40;

    public Terminus_Est(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return MANUAL_CHARGE_TIME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        
        if (!(user instanceof ServerPlayerEntity player)) return TypedActionResult.fail(stack);
        
        NbtCompound nbt = getOrCreateNbt(stack);
        
        boolean hasCharge = nbt.getBoolean("Charge");
        boolean isDischarged = nbt.getBoolean("Discharged");
        
        if (!hasCharge || isDischarged || player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }
        
        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof ServerPlayerEntity player)) return;
        if (world.isClient) return;
        
        int useTime = getMaxUseTime(stack) - remainingUseTicks;
        
        if (useTime >= MANUAL_CHARGE_TIME - 5) {
            createManualBarrier(player, stack);
        }
    }

    private void createManualBarrier(ServerPlayerEntity player, ItemStack stack) {
        NbtCompound nbt = getOrCreateNbt(stack);
        
        boolean hasCharge = nbt.getBoolean("Charge");
        if (!hasCharge) return;
        
        BlockPos pos = player.getBlockPos().add(0, 1, 0);
        ShieldZone zone = new ShieldZone(player, player, pos, BORDER_RADIUS);
        zone.setDurability(drainDurability(stack, 15));
        ShieldZoneManager.addZone(zone);
        
        sendShieldPacket(player, pos, BORDER_RADIUS, BORDER_HEIGHT);
        
        player.getWorld().playSound(null, player.getBlockPos(), 
            net.minecraft.sound.SoundEvents.ITEM_SHIELD_BLOCK, 
            net.minecraft.sound.SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof ServerPlayerEntity player)) return super.postHit(stack, target, attacker);
        if (!(target instanceof ServerPlayerEntity targetPlayer)) return super.postHit(stack, target, attacker);

        NbtCompound nbt = getOrCreateNbt(stack);
        
        boolean hasCharge = nbt.getBoolean("Charge");
        boolean isDischarged = nbt.getBoolean("Discharged");

        if (!hasCharge || player.getItemCooldownManager().isCoolingDown(this))
            return super.postHit(stack, target, attacker);

        if (isDischarged) {
            nbt.putBoolean("Discharged", false);
        }

        nbt.putBoolean("Charge", false);

        ShieldZone zone = new ShieldZone(player, targetPlayer, target.getBlockPos(), BORDER_RADIUS);
        zone.setDurability(drainDurability(stack, 10));
        ShieldZoneManager.addZone(zone);

        sendShieldPacket(player, target.getBlockPos(), BORDER_RADIUS, BORDER_HEIGHT);
        sendShieldPacket(targetPlayer, target.getBlockPos(), BORDER_RADIUS, BORDER_HEIGHT);

        return super.postHit(stack, target, attacker);
    }
    
    private void addKillCharge(ItemStack stack) {
        NbtCompound nbt = getOrCreateNbt(stack);
        int kills = nbt.getInt("KillCount");
        int charges = nbt.getInt("StackedCharges");
        
        kills++;
        
        if (kills >= 3 && charges < MAX_CHARGES) {
            charges++;
            kills = 0;
        }
        
        nbt.putInt("KillCount", kills);
        nbt.putInt("StackedCharges", charges);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        if (!(entity instanceof ServerPlayerEntity player)) return;

        NbtCompound nbt = getOrCreateNbt(stack);

        boolean hasCharge = nbt.getBoolean("Charge");
        int rechargeTimer = nbt.getInt("RechargeTimer");
        int dischargeTimer = nbt.getInt("DischargeTimer");
        int durability = nbt.getInt("Durability");
        
        if (durability <= 0 && hasCharge) {
            nbt.putBoolean("Charge", false);
            nbt.putInt("RechargeTimer", 0);
            hasCharge = false;
        }

        if (!hasCharge) {
            rechargeTimer++;
            if (rechargeTimer >= RECHARGE_TICKS) {
                int charges = nbt.getInt("StackedCharges");
                int kills = nbt.getInt("KillCount");
                
                kills++;
                nbt.putInt("KillCount", kills);
                
                if (kills >= 3 && charges < MAX_CHARGES) {
                    charges++;
                    kills = 0;
                }
                
                nbt.putInt("KillCount", kills);
                nbt.putInt("StackedCharges", charges);
                
                if (durability > 0) {
                    nbt.putBoolean("Charge", true);
                }
                rechargeTimer = 0;
            }
            nbt.putInt("RechargeTimer", rechargeTimer);
        }

        if (dischargeTimer > 0) {
            dischargeTimer--;
            nbt.putInt("DischargeTimer", dischargeTimer);
            
            if (dischargeTimer == 0) {
                nbt.putBoolean("Discharged", false);
            }
        }
    }
    
    private int drainDurability(ItemStack stack, int amount) {
        NbtCompound nbt = getOrCreateNbt(stack);
        int durability = nbt.getInt("Durability");
        
        if (durability == 0) {
            durability = MAX_DURABILITY;
        }
        
        durability -= amount;
        nbt.putInt("Durability", Math.max(0, durability));
        
        if (durability <= 0) {
            nbt.putBoolean("Charge", false);
            nbt.putInt("RechargeTimer", 0);
        }
        
        return durability;
    }
    
    public static int getDurability(ItemStack stack) {
        if (!stack.hasNbt()) return MAX_DURABILITY;
        return stack.getNbt().getInt("Durability");
    }
    
    public static int getStackedCharges(ItemStack stack) {
        if (!stack.hasNbt()) return 0;
        return stack.getNbt().getInt("StackedCharges");
    }

    private NbtCompound getOrCreateNbt(ItemStack stack) {
        if (!stack.hasNbt()) stack.setNbt(new NbtCompound());
        var nbt = stack.getNbt();
        if (!nbt.contains("Charge")) nbt.putBoolean("Charge", true);
        if (!nbt.contains("RechargeTimer")) nbt.putInt("RechargeTimer", 0);
        if (!nbt.contains("DischargeTimer")) nbt.putInt("DischargeTimer", 0);
        if (!nbt.contains("Durability")) nbt.putInt("Durability", MAX_DURABILITY);
        if (!nbt.contains("KillCount")) nbt.putInt("KillCount", 0);
        if (!nbt.contains("StackedCharges")) nbt.putInt("StackedCharges", 0);
        return nbt;
    }

    private void sendShieldPacket(ServerPlayerEntity player, BlockPos center, int radius, int height) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(center.getX());
        buf.writeInt(center.getY());
        buf.writeInt(center.getZ());
        buf.writeInt(radius);
        buf.writeInt(height);
        ServerPlayNetworking.send(player, ModPackets.SHIELD_PACKET, buf);
    }

    public static void triggerDischarge(ServerPlayerEntity player, ItemStack stack) {
        if (!stack.hasNbt()) return;
        var nbt = stack.getNbt();
        
        nbt.putBoolean("Discharged", true);
        nbt.putInt("DischargeTimer", DISCHARGE_DURATION);
        
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, DISCHARGE_DURATION, 2));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, DISCHARGE_DURATION, 1));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, DISCHARGE_DURATION, 1));
        
        player.getWorld().playSound(null, player.getBlockPos(), 
            net.minecraft.sound.SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 
            net.minecraft.sound.SoundCategory.PLAYERS, 1.5f, 0.5f);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        boolean hasCharge = stack.hasNbt() && stack.getNbt().getBoolean("Charge");
        boolean isDischarged = stack.hasNbt() && stack.getNbt().getBoolean("Discharged");
        
        if (isDischarged) {
            int timer = stack.getNbt().getInt("DischargeTimer");
            return 13 - (13 * timer / DISCHARGE_DURATION);
        }
        
        int durability = getDurability(stack);
        return 13 - (13 * durability / MAX_DURABILITY);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        boolean isDischarged = stack.hasNbt() && stack.getNbt().getBoolean("Discharged");
        if (isDischarged) return 0x00ffff;
        
        int durability = getDurability(stack);
        if (durability < 30) return 0xff0000;
        if (durability < 60) return 0xff9300;
        return 0x00ff00;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, World world, java.util.List<net.minecraft.text.Text> tooltip, net.minecraft.client.item.TooltipContext context) {
        int kills = getKillCount(stack);
        int charges = getStackedCharges(stack);
        
        tooltip.add(net.minecraft.text.Text.literal("Kills: " + kills + "/3").formatted(net.minecraft.util.Formatting.GRAY));
        if (charges > 0) {
            tooltip.add(net.minecraft.text.Text.literal("Charges: " + charges).formatted(net.minecraft.util.Formatting.GOLD));
        }
    }
    
    public static int getKillCount(ItemStack stack) {
        if (!stack.hasNbt()) return 0;
        return stack.getNbt().getInt("KillCount");
    }
}
