package edu.unr.ecsl;

import com.jme3.math.Vector3f;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.EntityState;
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
        int friendCount = 0, enemyCount = 0, friendDeadCount = 0, enemyDeadCount = 0;
        for(Entity ent : engine.entityManager.ents) {
            if(ent.player == engine.options.player) {
                friendCount++;

                if(ent.state == EntityState.DEAD)
                    friendDeadCount++;
            }

            else {
                enemyCount++;

                if(ent.state == EntityState.DEAD)
                    enemyDeadCount++;
            }
        }

        if(enemyCount == 0 || friendCount == 0)
            return;

        if(enemyCount == enemyDeadCount || friendCount == friendDeadCount) {
            System.out.printf("\n\nGame Over\n");
            System.out.printf("Enemy Deaths: %d, Friendly Deaths: %d\n", enemyDeadCount, friendDeadCount);
            engine.stop();
        }
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
                    ent = engine.entityManager.createEntityForPlayerAndSide(EntityType.DRONE,
                            new Vector3f(i * 20.0f, 20f, 0.0f),
                            0.0f, Side.BLUE, Player.ONE);

                    ent.init();
                }
            break;

            case 2:
                ent = engine.entityManager.createEntityForPlayerAndSide(EntityType.DRONE,
                        new Vector3f(0.0f, 20f, 0.0f),
                        0.0f, Side.BLUE, Player.ONE);

                ent.init();
            break;

            case 3:
                for (int i = 0; i < 5; i++) {
                    ent = engine.entityManager.createEntityForPlayerAndSide(EntityType.DRONE,
                            new Vector3f(500f + (i * 20f), 20f, 500f),
                            0f, Side.BLUE, Player.ONE);
                    ent.init();


                    ent = engine.entityManager.createEntityForPlayerAndSide(EntityType.DRONE,
                            new Vector3f(500f + (i * 20f), 20f, 2000f),
                            0f, Side.RED, Player.TWO);
                    ent.init();
                }
            break;

            case 4:
                ent = engine.entityManager.createEntityForPlayerAndSide(EntityType.DRONE,
                        new Vector3f(500f, 20f, 1500f),
                        0f, Side.RED, Player.TWO);
                ent.init();
            break;
        }
    }
}
