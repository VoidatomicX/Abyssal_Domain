package abyssal.abyssal_domain.entity.custom;

import net.minecraft.entity.Entity;

public interface HomingProjectile {

    void setTarget(Entity target);
    void setHomingEnabled(boolean enabled);

    void setHomingStrength(double strength);
    void setHomingDelay(int ticks);
    void setHomingNoise(double noise);

    void setMaxAttackCycles(int cycles);

    // 🧠 NEW: swarm phase control
    void setOrbitRadius(double radius);
}