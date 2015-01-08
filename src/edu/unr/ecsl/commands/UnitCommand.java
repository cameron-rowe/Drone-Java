package edu.unr.ecsl.commands;

import com.jme3.math.Vector3f;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;
import edu.unr.ecsl.enums.CommandType;

/**
 * Created by cam on 1/8/15.
 */
public abstract class UnitCommand extends Command {
    public Entity entity;
    public Target target;

    public Vector3f relativePos, relativeVel;
    public Vector3f predictedPos, interceptPos;

    public float predictedTimeToClose, relativeSpeed;

    public UnitCommand(Entity ent, CommandType ct, Target targ) {
        super(ct);
        entity = ent;
        target = targ;
    }
}
