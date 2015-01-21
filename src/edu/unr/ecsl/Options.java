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

    public int maxEntities;
    public float levelSize;

    @Override
    public String toString() {
        return "Options {" +
                "\n\tenableGfx = " + enableGfx +
                "\n\tenableNetworking = " + enableNetworking +
                "\n\tisServer = " + isServer +
                "\n\tnetworkPort = " + networkPort +
                "\n\tinstanceId = " + instanceId +
                "\n\tspeedup = " + speedup +
                "\n\ttimeScalar = " + timeScalar +
                "\n\tgameNumber = " + gameNumber +
                "\n\tscenario = " + scenario +
                "\n\tplayer = " + player +
                "\n\tseed = " + seed +
                "\n\tbitstring = '" + bitstring + '\'' +
                "\n\tmaxEntities = " + maxEntities +
                "\n\tlevelSize = " + levelSize +
                "\n}";
    }
}
