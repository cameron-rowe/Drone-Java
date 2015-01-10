package edu.unr.ecsl.commands;

import com.jme3.math.FastMath;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;

/**
 * Created by cam on 1/10/15.
 */
public class MoveDirect3D extends MoveDirect {
    public MoveDirect3D(Entity ent, Target targ) {
        super(ent, targ);
    }

    @Override
    public boolean done() {
        float distance = entity.pos.distanceSquared(target.location);
        return distance <= (entity.turningRadius * entity.turningRadius * 5f);
    }

    @Override
    public void init() {
        super.init();
        entity.desiredHeight = target.location.y;
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);

        if(!done()) {
            if(entity.pos.y < 2f)
                entity.pos.y = 2f;

            else if(entity.pos.y > 598f)
                entity.pos.y = 598f;

            if(FastMath.abs(relativePos.y) < 15f)
                entity.desiredVerticalSpeed = 0f;

            else if(relativePos.y > 0f)
                entity.desiredVerticalSpeed = entity.maxSpeed;

            else
                entity.desiredVerticalSpeed = -entity.maxSpeed;
        }

        else {
            entity.desiredSpeed = 0f;
            entity.desiredVerticalSpeed = 0f;
        }
    }
}
