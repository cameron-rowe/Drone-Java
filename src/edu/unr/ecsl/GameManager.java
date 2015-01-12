package edu.unr.ecsl;

import com.jme3.math.Vector3f;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.EntityType;
import edu.unr.ecsl.enums.Player;
import edu.unr.ecsl.enums.Side;

/**
 * Created by cam on 1/5/15.
 */
public class GameManager implements Manager {
    public Engine engine;

    public GameManager(Engine eng) {
        engine = eng;
    }

    @Override
    public void tick(float dt) {

    }

    @Override
    public void init() {
        switch (engine.options.gameNumber) {
            case 1:
                droneCraft();
                break;
        }
    }

    @Override
    public void stop() {

    }

    private void droneCraft() {
        Entity ent;

        switch (engine.options.scenario) {
            case 1:

                for (int i = 0; i < 10; i++) {
                    ent = engine.entityManager.createEntityForPlayerAndSide(EntityType.BANSHEE,
                            new Vector3f(i * 20.0f, 20f, 0.0f),
                            0.0f, Side.BLUE, Player.ONE);

                    ent.init();
                }
            break;

            case 2:
                ent = engine.entityManager.createEntityForPlayerAndSide(EntityType.BANSHEE,
                        new Vector3f(0.0f, 20f, 0.0f),
                        0.0f, Side.BLUE, Player.ONE);

                ent.init();
            break;

            case 3:
                for (int i = 0; i < 5; i++) {
                    ent = engine.entityManager.createEntityForPlayerAndSide(EntityType.BANSHEE,
                            new Vector3f(200f + (i * 20f), 20f, 0f),
                            0f, Side.BLUE, Player.ONE);
                    ent.init();


                    ent = engine.entityManager.createEntityForPlayerAndSide(EntityType.BANSHEE,
                            new Vector3f(200f + (i * 20f), 20f, 500f),
                            0f, Side.RED, Player.TWO);
                    ent.init();
                }
            break;
        }
    }
}
