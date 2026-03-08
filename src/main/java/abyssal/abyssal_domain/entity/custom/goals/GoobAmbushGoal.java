package abyssal.abyssal_domain.entity.custom.goals;

import abyssal.abyssal_domain.entity.custom.GoobichthysEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class GoobAmbushGoal extends Goal {

    private final GoobichthysEntity goob;

    public GoobAmbushGoal(GoobichthysEntity goob){
        this.goob = goob;
    }

    @Override
    public boolean canStart(){

        if(!goob.isBurrowed()) return false;

        LivingEntity target = goob.getWorld().getClosestEntity(
                LivingEntity.class,
                TargetPredicate.DEFAULT,
                goob,
                goob.getX(),
                goob.getY(),
                goob.getZ(),
                goob.getBoundingBox().expand(6)
        );

        return target != null;
    }

    @Override
    public void start(){

        goob.setBurrowed(false);

        LivingEntity target = goob.getWorld().getClosestPlayer(goob,6);

        if(target instanceof PlayerEntity player){

            if(player.getMainHandStack().isOf(Items.BEEF)){
                goob.getNavigation().startMovingTo(player,1.1);
            }

        } else if(target instanceof HuskEntity husk){
            goob.setTarget(husk);
        }
    }
}