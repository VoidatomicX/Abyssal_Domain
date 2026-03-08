package abyssal.abyssal_domain.item.custom;

import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.entity.custom.GoobichthysEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class GoobBucketItem extends Item {

    public GoobBucketItem(Settings settings){
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand){

        ItemStack stack = player.getStackInHand(hand);

        if(!world.isClient){

            GoobichthysEntity goob = ModEntities.GOOBICHTHYS.create(world);

            goob.refreshPositionAndAngles(
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    0,
                    0
            );

            world.spawnEntity(goob);
        }

        return TypedActionResult.success(new ItemStack(Items.LAVA_BUCKET));
    }
}