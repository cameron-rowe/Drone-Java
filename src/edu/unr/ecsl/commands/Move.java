package edu.unr.ecsl.commands;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;
import edu.unr.ecsl.enums.CommandType;

/**
 * Created by cam on 1/8/15.
 */
public class Move extends UnitCommand {

    public Move(Entity ent, Target targ) {
        super(ent, CommandType.MOVE, targ);
    }

    @Override
    public boolean done() {
        return entity.pos.distanceSquared(target.location) <= entity.turningRadius;
    }

    @Override
    public void init() {
        Vector3f diff = target.location.subtract(entity.pos);
        entity.desiredHeading = -FastMath.atan2(diff.z, diff.x);
        entity.desiredSpeed = entity.maxSpeed;

        relativePos = new Vector3f();
    }

    @Override
    public void tick(float dt) {
        if(!done()) {
            target.location.subtract(entity.pos, relativePos);
            entity.desiredHeading = -FastMath.atan2(relativePos.z, relativePos.x);
            entity.desiredSpeed = entity.maxSpeed;
        }

        else {
            entity.desiredSpeed = 0.0f;
        }
    }
}
