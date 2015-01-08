package edu.unr.ecsl.aspects;

import edu.unr.ecsl.commands.Command;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;
import edu.unr.ecsl.enums.UnitAspectType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by cam on 1/8/15.
 */
public class UnitAI extends UnitAspect {
    public Deque<Command> commands;
    public Target target;

    public UnitAI(Entity ent) {
        super(ent, UnitAspectType.UNITAI);

        commands = new ArrayDeque<>();
    }

    @Override
    public void init() {
        if(!commands.isEmpty()) {
            commands.getFirst().init();
        }

        target = new Target();
        target.entity = null;
        target.location = entity.pos.clone();
        target.offsetDistance = entity.seekRange;
    }

    @Override
    public void tick(float dt) {
        if(!commands.isEmpty()) {
            commands.getFirst().tick(dt);

            if(commands.getFirst().done()) {
                commands.removeFirst();
                if(!commands.isEmpty())
                    commands.getFirst().init();
            }
        }
    }

    public void addCommand(Command command) {
        commands.addLast(command);
    }

    public void setCommand(Command command) {
        clearCommands();
        commands.addLast(command);
    }

    public void setCommandList(Deque<Command> cmds) {
        commands = cmds;
    }

    public void clearCommands() {
        commands.clear();
    }
}
