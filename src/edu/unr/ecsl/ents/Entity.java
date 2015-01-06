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
}