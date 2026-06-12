package abyssal.abyssal_domain.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum ModToolMaterial implements ToolMaterial {
    GILDEDRUBY(5, 2025, 4.5f, 3.5f, 26, () -> Ingredient.ofItems(ModItems.Gilded_Ruby));

    private final int mininiglevel;
    private final int itemDurability;
    private final float mininigSpeed;
    private final float attackDamage;
    private final int enchatability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterial(int mininiglevel, int itemDurability, float mininigSpeed, float attackDamage, int enchatability, Supplier<Ingredient> repairIngredient) {
        this.mininiglevel = mininiglevel;
        this.itemDurability = itemDurability;
        this.mininigSpeed = mininigSpeed;
        this.attackDamage = attackDamage;
        this.enchatability = enchatability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.mininigSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.mininiglevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchatability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
