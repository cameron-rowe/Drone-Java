package edu.unr.ecsl;

import com.jme3.math.Vector3f;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.EntityState;

import java.util.List;

/**
 * Created by cam on 1/8/15.
 */
public class DistanceManager implements Manager {
    public Engine engine;

    public Vector3f[][] distanceVec;
    public Vector3f[][] normalizedDistanceVec;

    public float[][] distance;

    public float[] closestEnemyDistance;
    public int[] closestEnemy;

    public float[] farthestDistance;
    public int[] farthestEnt;

    private int maxEnts;

    public DistanceManager(Engine eng) {
        engine = eng;

        maxEnts = engine.options.maxEntities;

        distanceVec = new Vector3f[maxEnts][maxEnts];
        normalizedDistanceVec = new Vector3f[maxEnts][maxEnts];

        for(int i = 0; i < maxEnts; i++) {
            for(int j = 0; j < maxEnts; j++) {
                distanceVec[i][j] = new Vector3f();
                normalizedDistanceVec[i][j] = new Vector3f();
            }
        }

        distance = new float[maxEnts][maxEnts];

        closestEnemyDistance = new float[maxEnts];
        closestEnemy = new int[maxEnts];

        farthestDistance = new float[maxEnts];
        farthestEnt = new int[maxEnts];
    }

    @Override
    public void tick(float dt) {
        List<Entity> ents = engine.entityManager.ents;
        int nEnts = ents.size();

        for (int i = 0; i < nEnts; i++) {
            Entity ent = ents.get(i);
            distance[i][i] = 0.0f;
            distanceVec[i][i].zero();

            closestEnemyDistance[i] = Float.MAX_VALUE;

            for (int j = 0; j < nEnts; j++) {
                Entity other = ents.get(j);

                other.pos.subtract(ent.pos, distanceVec[i][j]);
                normalizedDistanceVec[i][j].set(distanceVec[i][j]).normalizeLocal();

                ent.pos.subtract(other.pos, distanceVec[j][i]);
                normalizedDistanceVec[j][i].set(distanceVec[j][i]).normalizeLocal();

                distance[i][j] = distanceVec[i][j].length();
                distance[j][i] = distance[i][j];

                if(distance[i][j] < closestEnemyDistance[i] && other.state == EntityState.ALIVE
                        && other.player != ent.player) {
                    closestEnemyDistance[i] = distance[i][j];
                    closestEnemy[i] = j;
                }

                if(distance[j][i] < closestEnemyDistance[j] && other.state == EntityState.ALIVE
                        && other.player != ent.player) {
                    closestEnemyDistance[j] = distance[j][i];
                    closestEnemy[j] = i;
                }

                if(distance[i][j] > farthestDistance[i]) {
                    farthestDistance[i] = distance[i][j];
                    farthestEnt[i] = j;
                }

                if(distance[j][i] > farthestDistance[j]) {
                    farthestDistance[j] = distance[j][i];
                    farthestEnt[j] = i;
                }
            }
        }
    }

    @Override
    public void init() {
        for (int i = 0; i < maxEnts; i++) {
            closestEnemy[i] = -1;
            closestEnemyDistance[i] = Float.MAX_VALUE;
            farthestDistance[i] = 0.0f;
            farthestEnt[i] = -1;
        }

        tick(0f);
    }

    @Override
    public void stop() {

    }
}
