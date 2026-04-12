package abyssal.abyssal_domain.enchants.custom;

import abyssal.abyssal_domain.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class GustEnchantment extends Enchantment {

    public GustEnchantment() {
        super(
                Rarity.UNCOMMON,
                EnchantmentTarget.BREAKABLE,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        );
    }

    @Override
    public boolean isAcceptableItem(net.minecraft.item.ItemStack stack) {
        return stack.isOf(ModItems.FAN_OF_UNYIELDING_WINDS);
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }
}