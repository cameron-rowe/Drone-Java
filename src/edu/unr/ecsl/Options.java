package edu.unr.ecsl;

import edu.unr.ecsl.enums.Player;

/**
 * Created by cam on 1/5/15.
 */
public class Options {
    public boolean enableGfx, enableNetworking;
    public boolean isServer;
    public int networkPort;
    public long instanceId;

    public float speedup, timeScalar;

    public int gameNumber, scenario;
    public Player player;

    public long seed;
    public String bitstring;
}
