package edu.unr.ecsl.ents;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import edu.unr.ecsl.Engine;
import edu.unr.ecsl.enums.*;

/**
 * Created by cam on 1/5/15.
 */
public class Entity {
    public static int count = 0;

    public Vector3f pos, vel, acc;
    public Quaternion rot, desiredRot;

    public float yaw, desiredSpeed, desiredHeading, speed;
    public float maxSpeed, minSpeed, speedRange, heading, maxAcceleration;
    public float maxRotationalSpeed, desiredHeight, verticalSpeed, desiredVerticalSpeed;

    public float hitpoints, hitpointsMax;
    public int seekRange, id;

    public float turningRadius, length, width, height, depth;
    public float mass, drag;
    public boolean selectable;

    public EntityType type;
    public EntityState state;
    public EntityClass entityClass;
    public Player player;
    public Side side;

    public String uiname, meshName;

    public Engine engine;

    public Entity(Engine eng, EntityType entType)
    {
        engine = eng;
        type = entType;

        state = EntityState.ALIVE;
    }

    public boolean canFly() {
        return false;
    }

    public void init() {

    }

    @Override
    public String toString() {
        return "Entity {\n" +
                "\tpos = " + pos +
                "\t\nvel = " + vel +
                "\t\nacc = " + acc +
                "\t\nrot = " + rot +
                "\t\ndesiredRot = " + desiredRot +
                "\t\nyaw = " + yaw +
                "\t\ndesiredSpeed = " + desiredSpeed +
                "\t\ndesiredHeading = " + desiredHeading +
                "\t\nspeed = " + speed +
                "\t\nmaxSpeed = " + maxSpeed +
                "\t\nminSpeed = " + minSpeed +
                "\t\nspeedRange = " + speedRange +
                "\t\nheading = " + heading +
                "\t\nmaxAcceleration = " + maxAcceleration +
                "\t\nmaxRotationalSpeed = " + maxRotationalSpeed +
                "\t\ndesiredHeight = " + desiredHeight +
                "\t\nverticalSpeed = " + verticalSpeed +
                "\t\ndesiredVerticalSpeed = " + desiredVerticalSpeed +
                "\t\nhitpoints = " + hitpoints +
                "\t\nhitpointsMax = " + hitpointsMax +
                "\t\nseekRange = " + seekRange +
                "\t\nid = " + id +
                "\t\nturningRadius = " + turningRadius +
                "\t\nlength = " + length +
                "\t\nwidth = " + width +
                "\t\nheight = " + height +
                "\t\ndepth = " + depth +
                "\t\nmass = " + mass +
                "\t\ndrag = " + drag +
                "\t\nselectable = " + selectable +
                "\t\ntype = " + type +
                "\t\nstate = " + state +
                "\t\nentityClass = " + entityClass +
                "\t\nplayer = " + player +
                "\t\nside = " + side +
                "\t\nuiname = '" + uiname + '\'' +
                "\t\nmeshName = '" + meshName + '\'' +
                "\t\nengine = " + engine +
                "\n}";
    }
}