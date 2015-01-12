package edu.unr.ecsl.ai;

import com.jme3.math.Vector3f;
import edu.unr.ecsl.Engine;
import edu.unr.ecsl.Manager;
import edu.unr.ecsl.commands.Command;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.Player;

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
    private Vector3f target;

    private List<Entity> friendly = new ArrayList<>();
    private List<Entity> enemies = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();

    private float totalDistance, maxDistance, averageFriendlyDistance;

    private long numTicks, numFriendTicks;

    public AIManager(Engine eng, InfluenceMap3D m, Player p) {
        engine = eng;

        map = m;
        player = p;

        totalDistance = 0.0f;
        numTicks = 0;
        numFriendTicks = 0;

        maxDistance = 0.0f;
    }


    private float totalDT = 0f;
    private Vector3f friendPos = new Vector3f(), enemyPos = new Vector3f();

    @Override
    public void tick(float dt) {
        totalDT += dt;

        if(totalDT > 0.5f) {
            totalDT = 0.0f;

            if(enemies.isEmpty())
                return;

            for(Entity fEnt : friendly) {
                if(!fEnt.isAttacking)
                    Command.createPotentialMove3DForEnt(fEnt, map.targetPos);

                Entity closest = engine.entityManager.ents.get(engine.distanceManager.closestEnemy[fEnt.id]);

                fEnt.isAttacking = false;

                if(engine.distanceManager.distance[fEnt.id][closest.id] < 500.0f) {
                    Command.createPotentialMove3DForEnt(fEnt, closest.pos);
                    fEnt.isAttacking = true;

                }
            }
        }
    }

    public void addEntity(Entity ent) {
        if(ent.player != player)
            enemies.add(ent);

        else
            friendly.add(ent);

        entities.add(ent);
    }

    public void evaluateMatch() {
        String fileName = "fitness_" + engine.options.seed + ".txt";

        float fitness = 0.0f;

        try(PrintWriter fout = new PrintWriter(fileName)) {
            fout.println(fitness);
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
