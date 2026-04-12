package abyssal.abyssal_domain.item.custom.trident;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public abstract class AbyssalTridentsItem extends TridentItem {

    protected final int cooldown;
    protected final boolean chargeable;
    protected final boolean throwable;

    public AbyssalTridentsItem(Settings settings, int cooldown, boolean chargeable, boolean throwable) {
        super(settings);
        this.cooldown = cooldown;
        this.chargeable = chargeable;
        this.throwable = throwable;
    }

    // ======================
    // BASIC BEHAVIOR
    // ======================

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return chargeable ? 72000 : 0;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (chargeable) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        } else {
            // Instant use ability
            if (!world.isClient) {
                onInstantUse(world, user, stack);
                applyCooldown(user);
            }
            return TypedActionResult.success(stack, world.isClient());
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (world.isClient) return;
        if (!(user instanceof PlayerEntity player)) return;

        int useTime = this.getMaxUseTime(stack) - remainingUseTicks;

        // Charge-based ability
        if (chargeable) {
            onChargeRelease(world, player, stack, useTime);
        }

        // Throw logic
        if (throwable && useTime > 10) {
            spawnTridentEntity(world, player, stack, useTime);
        }

        applyCooldown(player);
    }

    // ======================
    // ABILITY HOOKS
    // ======================

    /**
     * Called when right-click is used instantly (non-charge weapons)
     */
    protected void onInstantUse(World world, PlayerEntity player, ItemStack stack) {}

    /**
     * Called when releasing a charged trident
     */
    protected void onChargeRelease(World world, PlayerEntity player, ItemStack stack, int chargeTime) {}

    /**
     * Override this to spawn a custom trident entity
     */
    protected void spawnTridentEntity(World world, PlayerEntity player, ItemStack stack, int chargeTime) {
        TridentEntity trident = new TridentEntity(world, player, stack);

        trident.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F,
                getThrowSpeed(chargeTime), 1.0F);

        world.spawnEntity(trident);

        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }
    }

    /**
     * Customize throw speed scaling
     */
    protected float getThrowSpeed(int chargeTime) {
        return Math.min(2.5F, 0.1F * chargeTime);
    }

    protected void applyCooldown(PlayerEntity player) {
        if (cooldown > 0) {
            player.getItemCooldownManager().set(this, cooldown);
        }
    }
}