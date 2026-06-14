package abyssal.abyssal_domain.item.custom;

import abyssal.abyssal_domain.effect.ModEffects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

import java.util.UUID;

public class BattleAxeItem extends AxeItem {

    private static final UUID REACH_UUID =
            UUID.fromString("a1b2c3d4-1111-2222-3333-444455556666");

    private static final EntityAttributeModifier REACH_BONUS =
            new EntityAttributeModifier(
                    REACH_UUID,
                    "battle_axe_reach",
                    1.0D, // +1 blocks reach
                    EntityAttributeModifier.Operation.ADDITION
            );

    public BattleAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers =
                super.getAttributeModifiers(slot);

        if (slot == EquipmentSlot.MAINHAND) {
            modifiers = HashMultimap.create(modifiers);

            modifiers.put(
                    ReachEntityAttributes.REACH,
                    REACH_BONUS
            );
        }

        return modifiers;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        target.addStatusEffect(new StatusEffectInstance(
                ModEffects.STUN,
                15, // 0.5 seconds
                0
        ));

        return super.postHit(stack, target, attacker);
    }
}
