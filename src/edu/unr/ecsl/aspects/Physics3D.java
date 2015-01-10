package edu.unr.ecsl.aspects;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import edu.unr.ecsl.ents.Entity;

/**
 * Created by cam on 1/8/15.
 */
public class Physics3D extends Physics {
    public Physics3D(Entity ent) {
        super(ent);
    }

    @Override
    public void init() {
        entity.desiredSpeed = entity.speed;
        entity.desiredRot = entity.rot;
        entity.desiredHeight = 0.0f;
        entity.verticalSpeed = 0.0f;
    }

    @Override
    public void tick(float dt) {
        if (entity.speed < entity.desiredSpeed) {
            entity.speed += (entity.maxAcceleration * dt);
        } else if (entity.speed > entity.desiredSpeed) {
            entity.speed -= (entity.maxAcceleration * dt);
        }

        if (entity.verticalSpeed < entity.desiredVerticalSpeed) {
            entity.verticalSpeed += (entity.maxAcceleration * dt);
        } else if (entity.verticalSpeed > entity.desiredVerticalSpeed) {
            entity.verticalSpeed -= (entity.maxAcceleration * dt);
        }

        Quaternion desiredRot = new Quaternion().fromAngleAxis(entity.desiredHeading, Vector3f.UNIT_Y);

        //update it all
        entity.rot.slerp(desiredRot, entity.maxRotationalSpeed * dt);

        //entity.heading = entity.rot.getYaw().valueRadians();
        entity.heading = entity.rot.toAngles(null)[1];
        entity.yaw     = entity.heading;

        entity.verticalSpeed = FastMath.clamp(entity.verticalSpeed, -entity.maxSpeed, entity.maxSpeed);
        entity.speed = FastMath.clamp(entity.speed, entity.minSpeed, entity.maxSpeed);

        entity.vel.set(FastMath.cos(-entity.heading) * entity.speed, 0.0f, FastMath.sin(-entity.heading) * entity.speed);
        //finally update pos
        entity.pos.addLocal(entity.vel.mult(dt));

        if(entity.pos.y < 2.0f)
            entity.pos.y = 2.0f;

        else if(entity.pos.y > 598.0f)
            entity.pos.y = 598.0f;
    }
}
