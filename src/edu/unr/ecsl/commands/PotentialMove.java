package edu.unr.ecsl.commands;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import edu.unr.ecsl.DistanceManager;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;

import java.util.List;

/**
 * Created by cam on 1/8/15.
 */
public class PotentialMove extends Move {
    protected float A, B, B2, m, m2, n, repulsionThresholdDistance;

    public PotentialMove(Entity ent, Target targ) {
        super(ent, targ);
    }

    @Override
    public boolean done() {
        return entity.pos.distanceSquared(target.location) <= (FastMath.sqr(entity.turningRadius) * 20.0f);
    }

    @Override
    public void init() {
        entity.desiredSpeed = entity.maxSpeed;
        entity.potentialVec = new Vector3f();
    }

    @Override
    public void tick(float dt) {
        List<Entity> ents = entity.engine.entityManager.ents;
        int nEnts = ents.size();
        DistanceManager distanceManager = entity.engine.distanceManager;

        if(!done()) {
            float repulsivePotential = 0.0f;
            entity.potentialVec.zero();
            Vector3f temp;
            int nInRange = 1;

            for(int i = 0; i < nEnts; i++) {
                if(i != entity.id) {
                    if(distanceManager.distance[entity.id][i] < repulsionThresholdDistance) {
                        nInRange += 1;
                        temp = distanceManager.normalizedDistanceVec[i][entity.id];
                        repulsivePotential = (B * ents.get(i).mass)
                                / FastMath.pow(distanceManager.distance[entity.id][i], m);

                        entity.potentialVec.addLocal(temp.mult(repulsivePotential));
                    }
                }
            }

            temp = entity.pos.subtract(target.location);

            float targetDistance = temp.length();
            entity.attractivePotential = -A / FastMath.pow(targetDistance, n);
            entity.potentialVec.addLocal(temp.normalizeLocal().multLocal(entity.attractivePotential * nInRange));

            entity.desiredHeading = FastMath.atan2(-entity.potentialVec.z, entity.potentialVec.x);
            float cosDiffFrac = (1.0f - FastMath.cos(entity.vel.angleBetween(entity.potentialVec))) / 2.0f;
            entity.desiredSpeed = (entity.maxSpeed - entity.minSpeed) * (1.0f - cosDiffFrac);

            entity.rot.fromAngleAxis(entity.heading, Vector3f.UNIT_Y);
        }

        else {
            entity.desiredSpeed = 0.0f;
            entity.desiredHeading = entity.heading;
        }
    }
}
