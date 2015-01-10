package edu.unr.ecsl.aspects;

import edu.unr.ecsl.DistanceManager;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;
import edu.unr.ecsl.enums.UnitAspectType;

/**
 * Created by cam on 1/8/15.
 */
public class WeaponAspect extends UnitAspect {
    private float onfire, beingAttacked, cooldown;
    public Target target;
    private boolean isAttacking;

    DistanceManager distanceManager;


    public WeaponAspect(Entity ent) {
        super(ent, UnitAspectType.WEAPON);
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);
    }

    @Override
    public void init() {
        super.init();
    }
}
