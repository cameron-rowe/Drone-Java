package edu.unr.ecsl.commands;

import edu.unr.ecsl.DistanceManager;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;
import edu.unr.ecsl.enums.CommandType;
import edu.unr.ecsl.enums.EntityState;

/**
 * Created by cam on 1/12/15.
 */
public class Guard extends UnitCommand {
    private DistanceManager distanceManager;
    public MoveDirect3D move;
    public boolean isGuarding;

    private int count;
    public Guard(Entity ent, Target tgt) {
        super(ent, CommandType.RAM, tgt);
    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public void init() {
        distanceManager = entity.engine.distanceManager;
        isGuarding = false;
        move = new MoveDirect3D(entity, target);
        move.init();

        count = 0;
    }

    @Override
    public void tick(float dt) {
        Entity enemy = entity.engine.entityManager.ents.get(distanceManager.closestEnemy[entity.id]);

        if (enemy != null) {
            float dist_o = target.offset.distance(enemy.pos);
            float dist_e = entity.pos.distance(enemy.pos);

            if((dist_o < entity.seekRange || dist_e < entity.seekRange) && enemy.state == EntityState.ALIVE) {
                target.entity = enemy;
                target.location = enemy.pos.clone();
                move.tick(dt);
            }
            else if(dist_o > entity.turningRadius*10) {
                target.location = target.offset;
                target.entity = null;
                move.tick(dt);
            }
            else {
                stopGuarding();
            }
        }
    }

    public void startGuarding() {
        if(!isGuarding) {
            target.location = entity.pos.clone();
            target.offset = entity.pos.clone();
            entity.isAttacking = true;
        }

        isGuarding = true;
    }

    public void stopGuarding() {
        entity.desiredSpeed = 0;
        isGuarding = false;
        entity.isAttacking = false;
    }
}
