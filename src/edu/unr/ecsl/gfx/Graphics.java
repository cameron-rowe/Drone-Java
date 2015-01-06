package edu.unr.ecsl.gfx;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.input.KeyInput;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;
import edu.unr.ecsl.Engine;

/**
 * Created by cam on 10/10/14.
 */
public class Graphics extends SimpleApplication {
    private Engine engine;

    public Graphics(Engine e)
    {
        super(new StatsAppState(), new DebugKeysAppState());

        engine = e;
    }

    @Override
    public void simpleInitApp() {
        setupWater();
        setupInput();
    }

    @Override
    public void simpleUpdate(float dt)
    {
    }

    @Override
    public void stop() {
        engine.stop();
        super.stop();
    }

    private void setupWater() {
        SimpleWaterProcessor waterP = new SimpleWaterProcessor(assetManager);

        waterP.setReflectionScene(rootNode);

        Vector3f waterLoc = new Vector3f(0, -3, 0);
        waterP.setPlane(new Plane(Vector3f.UNIT_Y, waterLoc.dot(Vector3f.UNIT_Y)));
        viewPort.addProcessor(waterP);

        waterP.setWaterDepth(40.0f);
        waterP.setDistortionScale(0.05f);
        waterP.setWaveSpeed(0.05f);
        Quad quad = new Quad(400,400);
        quad.scaleTextureCoordinates(new Vector2f(6.0f,6.0f));

        Geometry water = new Geometry("water", quad);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setLocalTranslation(200,-3,250);
        water.setShadowMode(RenderQueue.ShadowMode.Receive);
        water.setMaterial(waterP.getMaterial());
        rootNode.attachChild(water);
    }

    private void setupInput() {
        ActionListener a_w = (name, keypressed, tfp) -> {
            if(!keypressed)
                System.out.println("w!");
        };

        ActionListener a_a = (name, keypressed, tfp) -> {
            if(!keypressed)
                System.out.println("a!");
        };

        ActionListener a_s = (name, keypressed, tfp) -> {
            if(!keypressed)
                System.out.println("s!");
        };

        ActionListener a_d = (name, keypressed, tfp) -> {
            if(!keypressed)
                System.out.println("d!");
        };
        inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));

        inputManager.addListener(a_w, "W");
        inputManager.addListener(a_a, "A");
        inputManager.addListener(a_s, "S");
        inputManager.addListener(a_d, "D");

        inputManager.setCursorVisible(true);
    }
}
