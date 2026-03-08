/* package abyssal.abyssal_domain.entity.custom.goals;

import abyssal.abyssal_domain.entity.custom.GoobichthysEntity;
import net.minecraft.entity.ai.goal.Goal;

public class GoobBurrowGoal extends Goal {

    private final GoobichthysEntity goob;

    public GoobBurrowGoal(GoobichthysEntity goob){
        this.goob = goob;
    }

    @Override
    public boolean canStart(){
        return !goob.isBurrowed() && goob.getRandom().nextInt(200)==0;
    }

    @Override
    public void start(){
        goob.setBurrowed(true);
        goob.setInvisible(true);
        goob.setNoGravity(true);
    }

    @Override
    public void stop(){
        goob.setBurrowed(false);
        goob.setInvisible(false);
        goob.setNoGravity(false);
    }
}*/