package edu.unr.ecsl;

import edu.unr.ecsl.ai.InfoManager;
import edu.unr.ecsl.enums.Player;
import edu.unr.ecsl.gfx.Graphics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

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
    public WeaponManager weaponManager;
    public InfoManager infoManager;

    public List<Manager> managers = new ArrayList<>();

    private boolean running = true;

    public float totalRuntime = 0.0f;
    public float maxRuntime;

    private static Engine instance = null;
    public static Engine getInstance() {
        return instance;
    }

    public Engine(String[] cmdArgs) {
        args = cmdArgs;
        instance = this;

        initOptions();
        init();
    }

    public void run() {
        if (options.enableGfx) {
            graphics.start();
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        t1 = System.nanoTime();
        float dt;
        maxRuntime = 10f * options.timeScalar;
        while(running && totalRuntime < maxRuntime) {
            dt = updateDT();
            //dt = 1.2E-5f;
            totalRuntime += dt;
            tick(dt * options.speedup);
        }

        if(options.enableGfx)
            graphics.stop();

        infoManager.ai.evaluateMatch();
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
        weaponManager = new WeaponManager(this);
        infoManager = new InfoManager(this);

        managers.add(weaponManager);
        managers.add(infoManager);
        managers.add(entityManager);
        managers.add(gameManager);
        managers.add(distanceManager);

        managers.forEach(Manager::init);

        if (options.enableGfx)
            graphics = new Graphics(this);
    }

    @Override
    public void stop() {
        running = false;
    }

    private long t1;

    private float updateDT() {
        long t2 = System.nanoTime();
        float dt = (float) (t2-t1) / 1000000000f;
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

        options.player = Player.ONE;

        options.enableGfx = false;
        options.seed = 1;
        options.bitstring = "111000101000010011101000011100101011100";

        options.gameNumber = 1;
        options.scenario = 2;

        for (int i = 0; i < args.length; i++) {
            if(Pattern.matches("[0-9]|10", args[i]))
                options.seed = Long.valueOf(args[i]);

            else if(Pattern.matches("[01]+", args[i]))
                options.bitstring = args[i];

            else if(Pattern.matches("-s[1-9]", args[i]))
                options.scenario = Character.getNumericValue(args[i].charAt(2));

            else if(args[i].equals("-s") && i != (args.length - 1))
                options.scenario = Integer.valueOf(args[++i]);

            else if(args[i].equals("-g"))
                options.enableGfx = true;

            else
                System.err.println("Unknown cmd arg: " + args[i]);
        }

        options.speedup = 15f;
        options.timeScalar = 6f;

        options.maxEntities = 1024;

//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 39; i++) {
//            sb.append(rand.nextInt(2));
//        }
//        options.bitstring = sb.toString();
//
//        System.out.println(options.bitstring);
    }
}
