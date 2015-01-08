package edu.unr.ecsl;

import edu.unr.ecsl.enums.Player;
import edu.unr.ecsl.gfx.Graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by cam on 10/10/14.
 */
public class Engine implements Manager {
    public String[] args;
    public Options options;

    public Graphics graphics = null;
    public EntityManager entityManager;
    public GameManager gameManager;
    public DistanceManager distanceManager;

    public List<Manager> managers = new ArrayList<>();

    private boolean enableGfx = true;
    private boolean running = true;

    public float totalRuntime = 0.0f;

    public Engine(String[] cmdArgs) {
        args = cmdArgs;
        initOptions();
        init();
    }

    public void run() {
        if (enableGfx) {
            graphics.start();
        }

        t1 = System.nanoTime();
        float dt, maxRuntime = 5.0f * options.timeScalar;
        while(running && totalRuntime < maxRuntime) {
            dt = updateDT();
            totalRuntime += dt;
            tick(dt);

        }

        graphics.stop();
    }

    @Override
    public void tick(float dt) {
        for(Manager manager : managers) {
            manager.tick(dt);
        }
    }

    @Override
    public void init() {
        entityManager = new EntityManager(this);
        gameManager = new GameManager(this);
        distanceManager = new DistanceManager(this);

        managers.add(entityManager);
        managers.add(gameManager);
        managers.add(distanceManager);

        for(Manager m : managers)
            m.init();

        if (enableGfx)
            graphics = new Graphics(this);
    }

    @Override
    public void stop() {
        running = false;
    }

    private long t1;

    private float updateDT() {
        long t2 = System.nanoTime();
        float dt = (float) (t2-t1) / 1000000000.0f;
        t1 = t2;

        return dt;
    }

    private void initOptions() {
        Random rand = new Random();
        options = new Options();

        options.enableNetworking = false;
        options.networkPort = 12345;
        options.isServer = true;

        options.instanceId = rand.nextLong();

        options.gameNumber = 1;
        options.scenario = 1;

        options.player = Player.ONE;

        if(args.length >= 2)
            options.seed = Long.valueOf(args[1]);

        else
            options.seed = 0;

        if(args.length >= 3)
            options.bitstring = args[2];

        else
            options.bitstring = "111000101000010011101000011100101011100";

//        if(args.length >= 4 && args[3].equals("-g")) {
//            options.enableGfx = true;
//            options.speedup = 10.0f;
//            options.timeScalar = 2.0f;
//        }
//
//        else {
//            options.enableGfx = false;
//            options.speedup = 50.0f;
//            options.timeScalar = 2.0f;
//        }

        options.enableGfx = true;
        options.speedup = 10.0f;
        options.timeScalar = 2.0f;

        options.maxEntities = 1024;
    }
}
