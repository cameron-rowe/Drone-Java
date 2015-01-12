package edu.unr.ecsl.ai;

import edu.unr.ecsl.Engine;
import edu.unr.ecsl.Manager;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.Player;

/**
 * Created by cam on 1/8/15.
 */
public class InfoManager implements Manager {
    public Engine engine;

    private int frame;
    private float dtime;
    public AIManager ai;

    public InfluenceMap3D map;

    public InfoManager(Engine eng) {
        engine = eng;
    }

    @Override
    public void tick(float dt) {
        map.tick(dt);
        ai.tick(dt);
    }

    @Override
    public void init() {
        frame = 0;
        dtime = 0.0f;

        int sizeX = 64;
        int sizeY = 16;
        int sizeZ = 64;
        int unitSize = 64;

        map = new InfluenceMap3D(InfluenceMap3D.MapType.IM_OCCUPANCE);
        map.init(sizeX, sizeY, sizeZ, sizeX * unitSize, sizeY * unitSize, sizeZ * unitSize);

        ai = new AIManager(engine, map, Player.ONE);
        ai.init();
    }

    public void addEntity(Entity ent) {
        ai.addEntity(ent);
        map.registerGameObject(ent);
    }

    @Override
    public void stop() {

    }
}
