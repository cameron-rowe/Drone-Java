package edu.unr.ecsl.ai;

import com.jme3.math.Vector3f;
import edu.unr.ecsl.Engine;
import edu.unr.ecsl.Manager;
import edu.unr.ecsl.aspects.WeaponAspect;
import edu.unr.ecsl.commands.Command;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.EntityState;
import edu.unr.ecsl.enums.Player;
import edu.unr.ecsl.enums.UnitAspectType;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cam on 1/8/15.
 */
public class AIManager implements Manager {
    public Engine engine;

    private Player player;
    private InfluenceMap3D map;

    private List<Entity> friendly = new ArrayList<>();
    private List<Entity> enemies = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();

    private double totalDistance, maxDistance, averageFriendlyDistance;

    private long numTicks, numFriendTicks;

    public AIManager(Engine eng, InfluenceMap3D m, Player p) {
        engine = eng;

        map = m;
        player = p;

        totalDistance = 0.0f;
        numTicks = 0;
        numFriendTicks = 0;

        maxDistance = 10000.0;
    }


    private float totalDT = 0f;
    private Vector3f friendPos = new Vector3f(), enemyPos = new Vector3f();
    //private float entDistance = 0f;

    @Override
    public void tick(float dt) {
        totalDT += dt;

        if(totalDT > 0.5f) {
            totalDT = 0.0f;

            if(enemies.isEmpty() || friendly.isEmpty())
                return;

            friendPos.zero();
            enemyPos.zero();

            float entDistance = 0f;

            int count = 0;
            for(Entity fEnt : friendly) {
                if(fEnt.state == EntityState.DEAD) {
                    continue;
                }

                if(!fEnt.isAttacking) {
                    Command.createPotentialMove3DForEnt(fEnt, map.targetPos);
                }

                Entity closest = engine.entityManager.getEntity(engine.distanceManager.closestEnemy[fEnt.id]);

                fEnt.isAttacking = false;

                if (engine.distanceManager.distance[fEnt.id][closest.id]
                        < engine.weaponManager.weaponTypes.get(fEnt.type).maxRange) {
                    Command.createPotentialMove3DForEnt(fEnt, closest.pos);
                    fEnt.isAttacking = true;

                    ((WeaponAspect) fEnt.getAspect(UnitAspectType.WEAPON)).target.entity = closest;
                }

//                for(Entity eEnt : enemies) {
//                    if(eEnt.state == EntityState.DEAD)
//                        continue;
//
//                    entDistance += engine.distanceManager.distance[fEnt.id][eEnt.id];
//                    count += 1;
//                }

                entDistance += fEnt.pos.distance(engine.infoManager.map.targetPos);
                friendPos.addLocal(fEnt.pos);
            }

            entDistance /= friendly.size();

            //System.out.println("Dist: " + entDistance);
            friendPos.divideLocal((float) friendly.size());

            for(Entity eEnt : enemies) {
                enemyPos.addLocal(eEnt.pos);
            }

            enemyPos.divideLocal((float) enemies.size());

            totalDistance += entDistance;//friendPos.distance(enemyPos);
            numTicks++;
        }
    }

    public void addEntity(Entity ent) {
        if(ent.player != player)
            enemies.add(ent);

        else
            friendly.add(ent);

        entities.add(ent);
    }

    //public static final double MAX_DISTANCE = 15000.0;

    public void evaluateMatch() {
        String fileName = "fitness_" + engine.options.seed + ".txt";

        double fitness;
        double totalEnemyHealth = 0;
        double totalFriendlyHealth = 0;

        int enemyCount = 0, enemyDeadCount = 0, friendCount = 0, friendDeadCount = 0;
        boolean combatOccurred = false;

        for(Entity ent : engine.entityManager.ents) {
            if(ent.player != engine.options.player) {
                enemyCount++;
                if(ent.hitpoints > 0f)
                    totalEnemyHealth += ent.hitpoints;

                if(ent.state == EntityState.DEAD)
                    enemyDeadCount++;

                if(ent.hitpoints != ent.hitpointsMax)
                    combatOccurred = true;
            }

            else {
                friendCount++;

                if(ent.hitpoints > 0f)
                    totalFriendlyHealth += ent.hitpoints;

                if(ent.state == EntityState.DEAD)
                    friendDeadCount++;

                if(ent.hitpoints != ent.hitpointsMax)
                    combatOccurred = true;
            }
        }

        if(combatOccurred) {
            double healthChange = totalFriendlyHealth - totalEnemyHealth;
            if(healthChange < 0) {
                fitness = healthChange * (1.0 - (engine.totalRuntime / engine.maxRuntime)) + 500.0;
            }

            else {
                fitness = healthChange * (1.0 - (engine.totalRuntime / engine.maxRuntime)) + 600;
            }
//            fitness = (totalFriendlyHealth - totalEnemyHealth) +
//                    ((1.0 - (engine.totalRuntime / engine.maxRuntime)) * 100.0) + 600f;
        }

        else {
            fitness = ((1.0 - ((totalDistance / numTicks) / 7500.0)) * 100.0) + 500f;
        }

        //System.out.println("Dist: " + entDistance);
        if(combatOccurred) {
            System.out.printf("Fitness -- combat: %.2f\n", fitness);
            System.out.printf("Friendly Lost: %d, Enemy Lost: %d\n", friendDeadCount, enemyDeadCount);
        }

        else {
            System.out.printf("Fitness -- distance: %.2f\n", fitness);
        }

        try(PrintWriter fout = new PrintWriter(fileName)) {
            fout.printf("%.2f\n", fitness);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void stop() {

    }
}
