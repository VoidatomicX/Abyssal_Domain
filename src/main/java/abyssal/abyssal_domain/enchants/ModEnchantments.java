package abyssal.abyssal_domain.enchants;

import abyssal.abyssal_domain.Abyssal_domain;
import abyssal.abyssal_domain.enchants.custom.GustEnchantment;
import abyssal.abyssal_domain.enchants.custom.PoisonEnchantment;
import abyssal.abyssal_domain.enchants.custom.TargetingEnchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.enchantment.Enchantment;


public class ModEnchantments {

    public static Enchantment POISON;
    public static Enchantment GUST;
    public static Enchantment TARGETING;

    public static void register() {

                POISON = register("poison", new PoisonEnchantment());

                GUST = register("gust", new GustEnchantment());

                TARGETING = register("targeting", new TargetingEnchantment());
    }

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(
                Registries.ENCHANTMENT,
                new Identifier(Abyssal_domain.MOD_ID, name),
                enchantment
        );
    }
}