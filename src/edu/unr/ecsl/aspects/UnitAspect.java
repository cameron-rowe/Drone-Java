package edu.unr.ecsl.aspects;

import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.UnitAspectType;

/**
 * Created by cam on 1/8/15.
 */
public class UnitAspect implements Aspect {
    public UnitAspectType aspectType;
    public Entity entity;

    public UnitAspect(Entity ent, UnitAspectType type) {
        entity = ent;
        aspectType = type;
    }

    @Override
    public void init() {
    }

    @Override
    public void tick(float dt) {
    }
}
