package abyssal.abyssal_domain.entity.custom;

import abyssal.abyssal_domain.entity.ModEntities;
import abyssal.abyssal_domain.item.ModItems;

import net.minecraft.block.BlockState;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;

import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.MobEntity;

import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;

import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;

import net.minecraft.server.world.ServerWorld;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import net.minecraft.world.EntityView;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

public class GoobichthysEntity extends TameableEntity {

    private static final TrackedData<Boolean> BURROWED =
            DataTracker.registerData(GoobichthysEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Integer> STATE =
            DataTracker.registerData(GoobichthysEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private boolean performedJump = false;

    public GoobichthysEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BURROWED,false);
        this.dataTracker.startTracking(STATE,0);
    }

    public boolean isBurrowed(){
        return this.dataTracker.get(BURROWED);
    }

    public void setBurrowed(boolean value){
        this.dataTracker.set(BURROWED,value);
    }

    @Override
    public void tick() {
        super.tick();

        PlayerEntity owner = this.getOwner() instanceof PlayerEntity p ? p : null;

        if(this.isSitting()){
            this.getNavigation().stop();
            return;
        }

        if(owner != null){
            double ownerDistance = this.distanceTo(owner);

            if(ownerDistance > 16){
                teleportToOwner(owner);
            }

            if(ownerDistance < 6){
                setBurrowed(false);
            }
        }

        if(isBurrowed()){
            this.getNavigation().stop();
        }

        PlayerEntity player = this.getWorld().getClosestPlayer(this,12);

        HuskEntity husk = this.getWorld().getClosestEntity(
                HuskEntity.class,
                TargetPredicate.DEFAULT,
                this,
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getBoundingBox().expand(10)
        );

        LivingEntity vibration = detectFootsteps();

        if(isBurrowed() && vibration != null){

            setBurrowed(false);

            Vec3d jump = vibration.getPos().subtract(this.getPos()).normalize();

            this.setVelocity(
                    jump.x * 0.45,
                    0.35,
                    jump.z * 0.45
            );

            this.setTarget(vibration);
            performedJump = true;
        }

        if(husk != null){

            this.dataTracker.set(STATE,2);
            setBurrowed(false);

            this.setTarget(husk);

            if(!performedJump){

                Vec3d jump = husk.getPos().subtract(this.getPos()).normalize();

                this.setVelocity(
                        jump.x * 0.45,
                        0.35,
                        jump.z * 0.45
                );

                performedJump = true;
            }
        }

        else if(player != null && player != owner){

            boolean hasBeef =
                    player.getMainHandStack().isOf(Items.BEEF) ||
                            player.getOffHandStack().isOf(Items.BEEF);

            double distance = this.distanceTo(player);

            if(hasBeef){

                this.dataTracker.set(STATE,1);
                setBurrowed(false);

                this.getNavigation().startMovingTo(player,1.0);

            }else{

                if(distance < 4){

                    this.dataTracker.set(STATE,3);
                    setBurrowed(false);

                    Vec3d flee =
                            this.getPos().subtract(player.getPos()).normalize();

                    this.getNavigation().startMovingTo(
                            this.getX()+flee.x*6,
                            this.getY(),
                            this.getZ()+flee.z*6,
                            1.3
                    );

                }else{

                    this.dataTracker.set(STATE,0);
                    setBurrowed(true);
                }
            }

        }else{

            this.dataTracker.set(STATE,0);
            setBurrowed(true);
            performedJump = false;
        }

        handleBurrowParticles();

        if(isBurrowed()){

            this.setInvisible(true);
            this.setNoGravity(true);
            this.setVelocity(0,0,0);

            this.setPose(EntityPose.SWIMMING);

        }else{

            this.setInvisible(false);
            this.setNoGravity(false);

            this.setPose(EntityPose.STANDING);
        }
    }

    private LivingEntity detectFootsteps(){

        for(Entity entity : this.getWorld().getOtherEntities(
                this,
                this.getBoundingBox().expand(4))){

            if(entity instanceof LivingEntity living){

                if(living instanceof PlayerEntity || living instanceof HuskEntity){

                    if(living.getVelocity().horizontalLength() > 0.05){
                        return living;
                    }
                }
            }
        }

        return null;
    }

    private void teleportToOwner(PlayerEntity owner){

        BlockPos pos = owner.getBlockPos();

        for(int i=0;i<10;i++){

            int x = pos.getX() + random.nextInt(6) - 3;
            int y = pos.getY();
            int z = pos.getZ() + random.nextInt(6) - 3;

            BlockPos tp = new BlockPos(x,y,z);

            if(this.getWorld().isAir(tp)){
                this.refreshPositionAndAngles(x+0.5,y,z+0.5,this.getYaw(),this.getPitch());
                this.getNavigation().stop();
                return;
            }
        }
    }

    private void handleBurrowParticles(){

        if(!this.isBurrowed()) return;

        if(this.random.nextInt(3)!=0) return;

        BlockState state =
                this.getWorld().getBlockState(this.getBlockPos().down());

        double x = this.getX() + (this.random.nextDouble()-0.5)*0.4;
        double z = this.getZ() + (this.random.nextDouble()-0.5)*0.4;

        this.getWorld().addParticle(
                new BlockStateParticleEffect(ParticleTypes.BLOCK,state),
                x,
                this.getY()+0.05,
                z,
                (this.random.nextDouble()-0.5)*0.15,
                0.05,
                (this.random.nextDouble()-0.5)*0.15
        );
    }


    @Override
    protected void initGoals() {

        this.goalSelector.add(0,new SwimGoal(this));
        this.goalSelector.add(1,new SitGoal(this));
        this.goalSelector.add(2,new FollowOwnerGoal(this,1.1f,5,2,false));
        this.goalSelector.add(3,new MeleeAttackGoal(this,1.2,true));
        this.goalSelector.add(4,new WanderAroundFarGoal(this,1));
        this.goalSelector.add(5,new LookAtEntityGoal(this, PlayerEntity.class,6));

        this.targetSelector.add(1,new ActiveTargetGoal<>(this,HuskEntity.class,true));
    }

    public static DefaultAttributeContainer.Builder createGoobichthyAttributes(){

        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,12)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.25)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,4);
    }

    public boolean isOnFire() {
        return false;
    }

            return ActionResult.SUCCESS;
        }

        if(stack.isOf(Items.LAVA_BUCKET)){

            if(!player.getAbilities().creativeMode){
                stack.decrement(1);
            }

            ItemStack goobBucket = new ItemStack(ModItems.GOOB_BUCKET);

            player.giveItemStack(goobBucket);

            this.discard();

            return ActionResult.SUCCESS;
        }

        return super.interactMob(player,hand);
    }

    @Nullable
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {

        ItemStack stack = player.getStackInHand(hand);

        if(this.isTamed() && player == this.getOwner()){
            this.setSitting(!this.isSitting());
            this.getNavigation().stop();
            this.setTarget(null);
            return ActionResult.SUCCESS;
        }

        if(stack.isOf(Items.BEEF) && !this.isTamed()){

            if(!player.getAbilities().creativeMode){
                stack.decrement(1);
            }

            if(this.random.nextInt(3)==0){
                this.setOwner(player);
                this.getWorld().sendEntityStatus(this,(byte)7);
            }else{
                this.getWorld().sendEntityStatus(this,(byte)6);
            }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.GOOBICHTHYS.create(world);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_FOX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_FOX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_FOX_DEATH;
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }
}