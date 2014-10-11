package edu.ecsl.drone;

/**
 * Created by cam on 10/10/14.
 */
public class Engine {
    public String[] args;
    Graphics graphics;

    public Engine(String[] cmdArgs)
    {
        args = cmdArgs;

        graphics = new Graphics(this);
    }

    public void run()
    {
        graphics.start();

        System.out.println("Graphics Started!");
    }

    private void init()
    {

    }

}
