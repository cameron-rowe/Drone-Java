package edu.unr.ecsl.aspects;

import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.UnitAspectType;

/**
 * Created by cam on 1/8/15.
 */
public class Physics extends UnitAspect {
    public float angleDiff, dHeading, timeScaldAcceleration;
    public float timeScaledRotationalSpeed, cosYaw, sinYaw;

    public Physics(Entity ent) {
        super(ent, UnitAspectType.PHYSICS);
    }
}
