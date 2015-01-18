package edu.unr.ecsl.aspects;

import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.UnitAspectType;

/**
 * Created by cam on 1/8/15.
 */
public abstract class UnitAspect implements Aspect {
    public UnitAspectType aspectType;
    public Entity entity;

    public UnitAspect(Entity ent, UnitAspectType type) {
        entity = ent;
        aspectType = type;
    }

    @Override
    public abstract void init();

    @Override
    public abstract void tick(float dt);
}
