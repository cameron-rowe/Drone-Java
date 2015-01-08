package edu.unr.ecsl.aspects;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import edu.unr.ecsl.ents.Entity;

/**
 * Created by cam on 1/8/15.
 */
public class Physics2D extends Physics {

    public Physics2D(Entity ent) {
        super(ent);
    }

    @Override
    public void init() {
        entity.desiredSpeed = entity.speed;
        entity.desiredHeading = entity.heading;
    }

    @Override
    public void tick(float dt) {
        doHelmsman(dt);
    }

    private void doHelmsman(float dt) {
        if (entity.speed < entity.desiredSpeed) {
            entity.speed += (entity.maxAcceleration * dt);
        } else if (entity.speed > entity.desiredSpeed) {
            entity.speed -= (entity.maxAcceleration * dt);
        }

        Quaternion desiredRot = new Quaternion().fromAngleAxis(entity.desiredHeading, Vector3f.UNIT_Y);

        //update it all
        entity.rot.slerp(desiredRot, entity.rot, entity.maxRotationalSpeed * dt);

        //entity.heading = entity.rot.getYaw().valueRadians();
        entity.heading = entity.rot.getY();
        entity.yaw     = entity.heading;

        entity.speed = FastMath.clamp(entity.speed, entity.minSpeed, entity.maxSpeed);

        entity.vel.set(FastMath.cos(-entity.heading) * entity.speed, 0.0f, FastMath.sin(-entity.heading) * entity.speed);
        //finally update pos
        entity.pos.addLocal(entity.vel.mult(dt));
    }
}
