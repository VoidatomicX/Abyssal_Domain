package abyssal.abyssal_domain.item.custom;

import abyssal.abyssal_domain.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

public class BattleAxeItem extends AxeItem {
    public BattleAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        target.addStatusEffect(new StatusEffectInstance(
                ModEffects.STUN,
                10, // 0.5 seconds
                0
        ));

        return super.postHit(stack, target, attacker);
    }
}
