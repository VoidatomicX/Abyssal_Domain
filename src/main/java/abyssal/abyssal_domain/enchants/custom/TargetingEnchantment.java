package abyssal.abyssal_domain.enchants.custom;

import abyssal.abyssal_domain.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class TargetingEnchantment extends Enchantment {

    public TargetingEnchantment() {
        super(
                Rarity.UNCOMMON,
                EnchantmentTarget.TRIDENT,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        );
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isOf(ModItems.Oraxia)
                || stack.isOf(ModItems.Voruna)
                || stack.isOf(Items.TRIDENT);
    }

    @Override
    public int getMaxLevel() {
        return 1;
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