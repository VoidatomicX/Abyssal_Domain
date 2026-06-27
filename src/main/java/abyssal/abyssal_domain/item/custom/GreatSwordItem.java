package abyssal.abyssal_domain.item.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

import java.util.UUID;

public class GreatSwordItem extends SwordItem {

    private static final UUID REACH_UUID =
            UUID.fromString("a1b2c3d4-1111-2222-3333-444455556666");

    private static final EntityAttributeModifier REACH_BONUS =
            new EntityAttributeModifier(
                    REACH_UUID,
                    "great_sword_reach",
                    1.5D, // +1 blocks reach
                    EntityAttributeModifier.Operation.ADDITION
            );

    public GreatSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
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

}
