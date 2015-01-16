package edu.unr.ecsl;

import com.jme3.math.Vector3f;
import edu.unr.ecsl.ents.Drone;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Marine;
import edu.unr.ecsl.enums.EntityType;
import edu.unr.ecsl.enums.Player;
import edu.unr.ecsl.enums.Side;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cam on 1/5/15.
 */
public class EntityManager implements Manager {
    public Engine engine;

    public List<Entity> ents = new ArrayList<>();

    public EntityManager(Engine eng) {
        engine = eng;
    }

    @Override
    public void tick(float dt) {
        for(Entity ent : ents) {
            ent.tick(dt);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void stop() {

    }

    private int addEntityToGame(Entity ent) {
        ents.add(ent);

        engine.infoManager.addEntity(ent);

        return ent.id;
    }

    public Entity createEntityForPlayerAndSide(EntityType eType, Vector3f pos, float heading, Side side, Player player) {
        Entity ent = createEntity(eType, pos, heading);

        ent.side = side;
        ent.player = player;

        ent.desiredSpeed = 0.0f;

        addEntityToGame(ent);
        return ent;
    }

    public Entity createEntity(EntityType etype, Vector3f pos, float heading) {
        Entity ent = null;
        switch (etype) {
            case SCV:
                break;
            case MARINE:
                ent = new Marine(engine);
                break;
            case REAPER:
                break;
            case TANK:
                break;
            case THRO:
                break;
            case MARAUDER:
                break;
            case HELLION:
                break;
            case DRONE:
                ent = new Drone(engine);
                break;

            default:
                System.err.println("EntityManager::createEntity -- unknown entity type");
                break;
        }

        if (ent == null) {
            ent = new Drone(engine);
        }

        ent.pos = pos;
        ent.heading = heading;


        return ent;
    }
}
