package abyssal.abyssal_domain.enchants.custom;

import abyssal.abyssal_domain.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.Entity;

public class PoisonEnchantment extends Enchantment {

    public PoisonEnchantment() {
        super(
                Rarity.UNCOMMON,
                EnchantmentTarget.WEAPON,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        );
    }

    @Override
    public boolean isAcceptableItem(net.minecraft.item.ItemStack stack) {
        return stack.isOf(ModItems.Scythe);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinPower(int level) {
        return 5 + (level - 1) * 8;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 20;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity livingTarget) {

            int duration = 40 + (level * 40); // 2s + scaling
            int amplifier = level - 1;

            livingTarget.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.POISON, duration, amplifier)
            );
        }

        super.onTargetDamaged(user, target, level);
    }
}