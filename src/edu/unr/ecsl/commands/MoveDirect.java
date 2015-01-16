package edu.unr.ecsl.commands;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;
import edu.unr.ecsl.enums.CommandType;

/**
 * Created by cam on 1/10/15.
 */
public class MoveDirect extends UnitCommand {

    protected Vector3f repulsor;

    public MoveDirect(Entity ent, Target targ) {
        super(ent, CommandType.MOVE, targ);
    }

    @Override
    public boolean done() {
        float distance = entity.pos.distanceSquared(target.location);
        return distance <= (entity.turningRadius * entity.turningRadius);
    }

    @Override
    public void init() {
        Vector3f diff = target.location.subtract(entity.pos);
        entity.desiredHeading = FastMath.atan2(diff.z, diff.x);
        entity.desiredSpeed = entity.maxSpeed;
        repulsor = new Vector3f();
    }

    @Override
    public void tick(float dt) {
        if(!done()) {
            computeRepulsor();

            target.location.subtract(entity.pos, relativePos);
            entity.desiredHeading = -FastMath.atan2(relativePos.z, relativePos.x);
            entity.desiredSpeed = entity.maxSpeed;

            if(repulsor.length() > 0.0f) {
                Vector3f force = repulsor.add(relativePos);
                float anglef = force.angleBetween(relativePos);
                Quaternion angle = new Quaternion().fromAngleAxis(anglef, Vector3f.UNIT_Y);

                float angleT = -FastMath.atan2(relativePos.z, relativePos.x);
                float angleF = -FastMath.atan2(force.z, force.x);
                float angleX = -FastMath.atan2(angle.getY(), angle.getW());
                entity.desiredHeading = (angleX < 1.3f && angleX > -1.3f) ? angleF : angleT;

                float maxForce = 1000f;
                float forceScale = force.length();
                if(forceScale < 700f) forceScale = 700f;
                else if(forceScale > 1000f) forceScale = 1000f;
                float forceRange = forceScale / maxForce;
                entity.desiredSpeed = entity.maxSpeed * forceRange;
            }
        }
        else {
            entity.desiredSpeed = 0f;
            repulsor.zero();
        }
    }

    protected void computeRepulsor() {
        repulsor.zero();

//        for(Entity ent : entity.engine.entityManager.ents) {
//            if(ent.id == entity.id) continue;
//
//            float collide = isCollide(ent);
//            if(collide > 0.0f) {
//                Vector3f dr = entity.pos.subtract(ent.pos);
//                dr.normalizeLocal();
//                Vector3f dt = target.location.subtract(entity.pos);
//                dr.multLocal(collide * dt.length());
//                repulsor.addLocal(dr);
//            }
//        }
    }

    protected float isCollide(Entity ent) {
        float r1 = 60, r2 = 60;
        float dist = ent.pos.distance(entity.pos);
        return (r1+r2 - dist) / (r1+r2);
    }
}
