package abyssal.abyssal_domain.item.custom;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class Scythe extends SwordItem {


    public Scythe(ToolMaterials toolMaterials, int i, float v, FabricItemSettings fabricItemSettings) {
        super(toolMaterials, i, v, fabricItemSettings);

    }
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {

        var enchants = EnchantmentHelper.get(stack);
//tool tip
        if (enchants.containsKey(Enchantments.FIRE_ASPECT)) {
            tooltip.add(Text.literal("Flame Soul").formatted(Formatting.GOLD));
        }
//item name
        else if
        (!enchants.isEmpty()) {
            tooltip.add(Text.literal("Flame Scythe").formatted(Formatting.GRAY));
        }
    }


    @Override
    public Text getName(ItemStack stack) {
        var enchants = EnchantmentHelper.get(stack);

        if (enchants.containsKey(Enchantments.FIRE_ASPECT)) {
            return Text.literal("Flame Scythe").formatted(Formatting.RED);
        }

        return super.getName(stack);
    }

}
