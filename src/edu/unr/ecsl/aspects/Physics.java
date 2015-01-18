package edu.unr.ecsl.aspects;

import com.jme3.math.Quaternion;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.UnitAspectType;

/**
 * Created by cam on 1/8/15.
 */
public abstract class Physics extends UnitAspect {
    protected Quaternion desiredRot;
    protected float[] angles;

    public Physics(Entity ent) {
        super(ent, UnitAspectType.PHYSICS);

        desiredRot = new Quaternion();
        angles = new float[3];
    }
}
