package edu.unr.ecsl.aspects;

import edu.unr.ecsl.commands.Command;
import edu.unr.ecsl.commands.Guard;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;
import edu.unr.ecsl.enums.UnitAspectType;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by cam on 1/8/15.
 */
public class UnitAI extends UnitAspect {
    public Deque<Command> commands;
    public Target target;
    private Guard guard;

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

        if(entity.player != entity.engine.options.player) {
            guard = new Guard(entity, target);
            guard.init();
        }
    }

    @Override
    public void tick(float dt) {
        try {
            if(!commands.isEmpty()) {
                commands.getFirst().tick(dt);

                if(commands.getFirst().done()) {
                    commands.removeFirst();
                    if(!commands.isEmpty())
                        commands.getFirst().init();
                }
            }

            else if(entity.player != entity.engine.options.player) {
                guard.startGuarding();
                guard.tick(dt);
            }
        } catch (Exception e) {
            System.err.println("Thread Access Error: UnitAI::tick");
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
