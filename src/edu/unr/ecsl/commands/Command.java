package edu.unr.ecsl.commands;

import com.jme3.math.Vector3f;
import edu.unr.ecsl.aspects.UnitAI;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;
import edu.unr.ecsl.enums.CommandType;
import edu.unr.ecsl.enums.UnitAspectType;

/**
 * Created by cam on 1/8/15.
 */
public abstract class Command {
    public CommandType type;

    public Command(CommandType ct) {
        type = ct;
    }

    public abstract boolean done();
    public abstract void init();
    public abstract void tick(float dt);

    public static void createPotentialMoveForEnt(Entity ent, Vector3f pos) {
        createPotentialMoveForEnt(ent, pos, false);
    }

    public static void createPotentialMoveForEnt(Entity ent, Vector3f pos, boolean append) {
        Target target = new Target();
        target.entity = null;
        target.location = pos;
        target.offset = new Vector3f();

        PotentialMove pm = new PotentialMove(ent, target);
        pm.init();

        UnitAI ai = (UnitAI) ent.getAspect(UnitAspectType.UNITAI);

        if(append)
            ai.addCommand(pm);

        else
            ai.setCommand(pm);
    }

    public static void createPotentialMove3DForEnt(Entity ent, Vector3f pos) {
        createPotentialMoveForEnt(ent, pos, false);
    }

    public static void createPotentialMove3DForEnt(Entity ent, Vector3f pos, boolean append) {
        Target target = new Target();
        target.entity = null;
        target.location = pos;
        target.offset = new Vector3f();

        PotentialMove3D pm = new PotentialMove3D(ent, target);
        pm.init();

        UnitAI ai = (UnitAI) ent.getAspect(UnitAspectType.UNITAI);

        if(append)
            ai.addCommand(pm);

        else
            ai.setCommand(pm);
    }

    public static void createMove2DForEnt(Entity ent, Vector3f pos) {
        createMove2DForEnt(ent, pos, false);
    }

    public static void createMove2DForEnt(Entity ent, Vector3f pos, boolean append) {
        Target target = new Target();
        target.entity = null;
        target.location = pos;
        target.offset = new Vector3f();

        Move mv = new Move(ent, target);
        mv.init();

        UnitAI ai = (UnitAI) ent.getAspect(UnitAspectType.UNITAI);

        if(append)
            ai.addCommand(mv);

        else
            ai.setCommand(mv);
    }

    public static void createMove3DForEnt(Entity ent, Vector3f pos) { createMove3DForEnt(ent, pos, false);}

    public static void createMove3DForEnt(Entity ent, Vector3f pos, boolean append) {
        Target target = new Target();
        target.entity = null;
        target.location = pos;
        target.offset = new Vector3f();

        MoveDirect3D mv = new MoveDirect3D(ent, target);
        mv.init();

        UnitAI ai = (UnitAI) ent.getAspect(UnitAspectType.UNITAI);

        if(append)
            ai.addCommand(mv);

        else
            ai.setCommand(mv);

        System.out.println("Created Command");
    }
}
