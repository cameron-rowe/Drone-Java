package edu.ecsl.drone;

import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
import com.jme3.input.KeyInput;

/**
 * Created by cam on 10/10/14.
 */
public class Graphics extends SimpleApplication {
    private Engine engine;

    public Graphics(Engine e)
    {
        engine = e;
    }

    private Geometry cube;

    @Override
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1); // create cube shape
        cube = new Geometry("Box", b);  // create cube geometry from the shape
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
        cube.setMaterial(mat);                   // set the cube's material
        rootNode.attachChild(cube);              // make the cube appear in the scene

        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(al, "Pause");
    }

    @Override
    public void simpleUpdate(float dt)
    {
        cube.rotate(0, 2*dt, 0);
    }

    private boolean isRunning = true;

    private ActionListener al = (String name, boolean keyPressed, float dt) -> {
        if(name.equals("Pause") && !keyPressed)
            System.out.println("Paused!");
    };
}
