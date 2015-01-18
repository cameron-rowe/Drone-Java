package edu.unr.ecsl.aspects;

import edu.unr.ecsl.commands.Command;
import edu.unr.ecsl.commands.Guard;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;
import edu.unr.ecsl.enums.UnitAspectType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by cam on 1/8/15.
 */
public class UnitAI extends UnitAspect {
    public final Deque<Command> commands;
    public Target target;
    public Guard guard;

    public UnitAI(Entity ent) {
        super(ent, UnitAspectType.UNITAI);

        if(entity.engine.options.enableGfx || entity.engine.options.enableNetworking)
            commands = new ConcurrentLinkedDeque<>();

        else
            commands = new ArrayDeque<>();

        guard = null;
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

        if(entity.player != entity.engine.options.player && entity.engine.entityManager.hasFriendlyUnits()) {
            guard = new Guard(entity, target);
            guard.init();
        }
    }

    @Override
    public void tick(float dt) {
        synchronized (commands) {
             if(!commands.isEmpty()) {
                commands.getFirst().tick(dt);

                 if(commands.getFirst().done()) {
                    commands.removeFirst();
                     if(!commands.isEmpty())
                        commands.getFirst().init();
                }
            }
        }

        if(guard != null) {
            guard.startGuarding();
            guard.tick(dt);
        }
    }

    public void addCommand(Command command) {
        synchronized (commands) {
            commands.addLast(command);
        }
    }

    public void setCommand(Command command) {
        synchronized (commands) {
            clearCommands();
            commands.addLast(command);
        }
    }

    public void clearCommands() {
        synchronized (commands) {
            commands.clear();
        }
    }
}
