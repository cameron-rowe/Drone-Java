package edu.unr.ecsl.ents;

import com.jme3.math.Vector3f;

/**
 * Created by cam on 1/8/15.
 */
public class Target {
    public Vector3f location;
    public Entity entity;
    public float waitTime;

    public Vector3f offset;
    public float offsetYaw, offsetDistance;
}
