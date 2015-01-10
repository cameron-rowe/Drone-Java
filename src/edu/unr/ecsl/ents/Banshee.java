package edu.unr.ecsl.ents;

import com.jme3.math.Vector3f;
import edu.unr.ecsl.Engine;
import edu.unr.ecsl.Util;
import edu.unr.ecsl.aspects.Aspect;
import edu.unr.ecsl.aspects.Physics3D;
import edu.unr.ecsl.aspects.UnitAI;
import edu.unr.ecsl.commands.Command;
import edu.unr.ecsl.enums.EntityType;
import edu.unr.ecsl.enums.Side;

/**
 * Created by cam on 1/5/15.
 */
public class Banshee extends Entity {
    public static int count = 0;

    public Banshee(Engine eng) {
        super(eng, EntityType.BANSHEE);

        meshName = "Test/Monkey.obj";
        uiname = String.format("Drone.%d", count++);

        length = Util.meters(5.0f);
        width = Util.meters(2.0f);
        height = Util.meters(5.0f);
        maxSpeed = Util.knots(60.0f);
        minSpeed = Util.knots(0.0f);
        speedRange = maxSpeed - minSpeed;

        maxAcceleration = Util.feetPerSecondSquared(80.0f);
        maxRotationalSpeed = Util.degreesPerSecond(30.0f);
        turningRadius = 50.0f;
        mass = Util.tons(0.6f);

        selectable = true;

        hitpoints = hitpointsMax = 100.0f;
        seekRange = 1500;

        side = Side.YELLOW;
    }

    @Override
    public boolean canFly() {
        return true;
    }

    @Override
    public void init() {
        aspects.add(new Physics3D(this));
        aspects.add(new UnitAI(this));

        for(Aspect asp : aspects)
            asp.init();

        //Command.createPotentialMove3DForEnt(this, new Vector3f(0.0f, 10.0f, -1000.0f));
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);

        //pos.addLocal(5.0f * dt, 0.0f, 0.0f);
    }
}
