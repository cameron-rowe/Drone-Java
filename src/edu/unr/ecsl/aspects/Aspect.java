package edu.unr.ecsl.aspects;

/**
 * Created by cam on 1/8/15.
 */
public interface Aspect {
    void init();
    void tick(float dt);
}
