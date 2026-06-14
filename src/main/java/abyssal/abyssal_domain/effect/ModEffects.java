package abyssal.abyssal_domain.effect;

import abyssal.abyssal_domain.Abyssal_domain;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects implements ModInitializer {

    public static StatusEffect STUN;

    private static StatusEffect registerStatusEffect(String name) {
        return Registry.register(
                Registries.STATUS_EFFECT,
                new Identifier(Abyssal_domain.MOD_ID, name),
                new StunEffect(StatusEffectCategory.HARMFUL, 3124687)
        );
    }

    public static void registerEffects() {
        STUN = registerStatusEffect("stun");
    }

    @Override
    public void onInitialize() {
        registerEffects();
    }
}