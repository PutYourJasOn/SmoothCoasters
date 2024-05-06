package me.m56738.smoothcoasters.mixin;

import me.m56738.smoothcoasters.AnimatedPose;
import me.m56738.smoothcoasters.DisplayEntityMixinInterface;
import me.m56738.smoothcoasters.SmoothCoasters;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisplayEntity.class)
public abstract class DisplayEntityMixin extends Entity implements DisplayEntityMixinInterface {

    private final AnimatedPose animatedPose = new AnimatedPose();
    private Quaternionf rotation = new Quaternionf();

    public DisplayEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructorHead(CallbackInfo info) {
        rotation = getTargetLeftRotation();
        animatedPose.set(rotation, 0);
    }

    @Inject(method = "tick", at=@At("HEAD"))
    public void tickHead(CallbackInfo info) {

        //Update (Watcher)
        if(!getTargetLeftRotation().equals(animatedPose.getTarget())) {
            animatedPose.set(getTargetLeftRotation(), getInvokedInterpolationDuration());
        }

        //For varying 'shouldBeAnimated()' values, we should keep track of the pose no matter the value
        //In 'normal' situations the shouldBeAnimated() won't vary, so we can skip the pose tick

        //if(shouldBeAnimated()) {
        if(true) {    //Currently testing with toggling the animation for before/after results, so we should tick it
            animatedPose.tick();
            rotation = animatedPose.getTarget();
        }

    }

    @Override
    public void animate(float delta) {
        rotation = animatedPose.calculateQuaternion(delta);
    }

    @Override
    public boolean shouldBeAnimated() {
        return SmoothCoasters.getDisplayAnimationToggle() && !this.isSilent();
    }

    @Override
    public Quaternionf getRotation() {
        return rotation;
    }

    //invoking
    @Invoker("getInterpolationDuration")
    public abstract int getInvokedInterpolationDuration();

    @Invoker("getTransformation")
    public static AffineTransformation getInvokedTransformation(DataTracker dataTracker) {
        throw new AssertionError();
    }

    private Quaternionf getTargetLeftRotation() {
        return getInvokedTransformation(this.dataTracker).getLeftRotation();
    }

}