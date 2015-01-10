package edu.unr.ecsl.ai;

import edu.unr.ecsl.Engine;
import edu.unr.ecsl.Manager;
import edu.unr.ecsl.enums.Player;

/**
 * Created by cam on 1/8/15.
 */
public class InfoManager implements Manager {
    public Engine engine;

    private int frame;
    private float dtime;
    private AIManager ai;

    public InfluenceMap3D map;

    public InfoManager(Engine eng) {
        engine = eng;
    }

    @Override
    public void tick(float dt) {

    }

    @Override
    public void init() {
        frame = 0;
        dtime = 0.0f;

        int sizeX = 64;
        int sizeY = 64;
        int sizeZ = 16;
        int unitSize = 64;

        map = new InfluenceMap3D(InfluenceMap3D.MapType.IM_OCCUPANCE);
        map.init(sizeX, sizeY, sizeZ, sizeX * unitSize, sizeY * unitSize, sizeZ * unitSize);

        ai = new AIManager(engine, map, Player.ONE);
        ai.init();
    }

    @Override
    public void stop() {

    }
}
