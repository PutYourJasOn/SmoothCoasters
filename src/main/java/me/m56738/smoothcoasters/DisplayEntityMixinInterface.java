package me.m56738.smoothcoasters;

import org.joml.Quaternionf;

public interface DisplayEntityMixinInterface {

    boolean shouldBeAnimated();
    void animate(float delta);
    Quaternionf getRotation();

}
