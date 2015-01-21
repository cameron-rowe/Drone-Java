package edu.unr.ecsl;

/**
 * Created by cam on 10/10/14.
 */
public class Main {
    public static void main(String[] args)
    {
        System.out.println("Starting Drone-Java");
        try {
            Engine e = new Engine(args);
            e.run();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
