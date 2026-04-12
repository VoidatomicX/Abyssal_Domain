package abyssal.abyssal_domain.mixins;

import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TridentEntity.class)
public interface TridentEntityAccessor {

    @Invoker("asItemStack")
    ItemStack abyssal$asItemStack();
}