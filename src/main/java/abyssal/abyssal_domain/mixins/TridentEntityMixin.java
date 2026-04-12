package abyssal.abyssal_domain.mixins;

import abyssal.abyssal_domain.enchants.ModEnchantments;


import abyssal.abyssal_domain.entity.custom.HomingProjectile;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin implements HomingProjectile {

    @Unique private Entity abyssal$target;
    @Unique private boolean abyssal$homing;

    @Override
    public void setTarget(Entity target) {
        this.abyssal$target = target;
    }

    @Override
    public void setHomingEnabled(boolean enabled) {
        this.abyssal$homing = enabled;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void abyssal$homingTick(CallbackInfo ci) {
        TridentEntity self = (TridentEntity)(Object)this;

        ItemStack stack = ((TridentEntityAccessor) self).abyssal$asItemStack();

        // 🔒 Enchant check
        if (EnchantmentHelper.getLevel(ModEnchantments.TARGETING, stack) <= 0) return;

        if (!abyssal$homing) return;
        if (abyssal$target == null || !abyssal$target.isAlive()) return;

        Vec3d vel = self.getVelocity();

        Vec3d targetPos = abyssal$target.getPos()
                .add(0, abyssal$target.getHeight() * 0.5, 0);

        Vec3d dir = targetPos.subtract(self.getPos()).normalize();

        double strength = 0.2;

        Vec3d newVel = vel.multiply(1.0 - strength)
                .add(dir.multiply(strength * vel.length()));

        self.setVelocity(newVel);
    }
}