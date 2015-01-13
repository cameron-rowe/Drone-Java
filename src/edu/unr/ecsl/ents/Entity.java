package edu.unr.ecsl.ents;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import edu.unr.ecsl.Engine;
import edu.unr.ecsl.aspects.*;
import edu.unr.ecsl.enums.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cam on 1/5/15.
 */
public class Entity implements Comparable<Entity> {
    public static int count = 0;

    public Vector3f pos, vel, acc, potentialVec;
    public Quaternion rot, desiredRot;

    public float yaw, desiredSpeed, desiredHeading, speed, attractivePotential;
    public float maxSpeed, minSpeed, speedRange, heading, maxAcceleration;
    public float maxRotationalSpeed, desiredHeight, verticalSpeed, desiredVerticalSpeed;

    public float hitpoints, hitpointsMax;
    public int seekRange, id;

    public float turningRadius, length, width, height, depth;
    public float mass, drag;
    public boolean selectable, isAttacking;

    public EntityType type;
    public EntityState state;
    public EntityClass entityClass;
    public Player player;
    public Side side;

    public List<UnitAspect> aspects = new ArrayList<>(3);

    public String uiname, meshName;

    public Engine engine;

    public Entity(Engine eng, EntityType entType)
    {
        engine = eng;
        type = entType;

        state = EntityState.ALIVE;

        pos = new Vector3f();
        vel = new Vector3f();
        acc = new Vector3f();
        potentialVec = new Vector3f();

        rot = new Quaternion();
        desiredRot = new Quaternion();

        id = count++;
    }

    public boolean canFly() {
        return false;
    }

    public void init() {
        aspects.add(new Physics2D(this));
        aspects.add(new UnitAI(this));
        aspects.add(new WeaponAspect(this));

        for(Aspect asp : aspects)
            asp.init();
    }

    public void tick(float dt) {
        switch (state) {
            case ALIVE:
                int i = 0;
                for(UnitAspect aspect : aspects) {
                    aspect.tick(dt);
                }
                break;
            case DYING:
                pos.y = -10000f;
                state = EntityState.DEAD;
                break;
        }
    }

    @Override
    public String toString() {
        return "Entity {" +
                "\n\tpos = " + pos +
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

    public void print() {
        System.out.println(this.toString());
    }

    public UnitAspect getAspect(UnitAspectType type) {
        for(UnitAspect aspect : aspects) {
            if(aspect.aspectType == type)
                return aspect;
        }

        return null;
    }

    @Override
    public int compareTo(Entity o) {
        if(id == o.id)
            return 0;

        return (id < o.id) ? -1 : 1;
    }
}