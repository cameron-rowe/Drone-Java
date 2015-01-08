package edu.unr.ecsl.gfx;

import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.debug.WireSphere;
import com.jme3.scene.shape.Quad;
import edu.unr.ecsl.Manager;

/**
 * Created by cam on 1/7/15.
 */
public class UIManager implements Manager {
    private Graphics graphics;
    private InputManager inputManager;

    public UIManager(Graphics gfx) {
        graphics = gfx;
        inputManager = gfx.getInputManager();
    }

    private WireBox selectionQuad = new WireBox();

    @Override
    public void tick(float dt) {
        if (selecting) {
            Vector2f cursorPosition = inputManager.getCursorPosition();
            float width = -(selectionStartPos.x - cursorPosition.x);//Math.abs(selectionStartPos.x - cursorPosition.x);
            float height = -(selectionStartPos.y - cursorPosition.y);//Math.abs(selectionStartPos.y - cursorPosition.y);


            selectionGeometry.setLocalTranslation(selectionStartPos.x + width / 2.0f, selectionStartPos.y + height / 2.0f, 1.0f);
            selectionQuad.updatePositions(width / 2.0f, height / 2.0f, 1.0f);

            //selectionGeometry.getWorldTransform().setTranslation(cursorPosition.x, cursorPosition.y, 0.0f);


            //selectionGeometry.getWorldTransform().


        }
    }

    @Override
    public void init() {
        selectionQuad.setMode(Quad.Mode.LineStrip);
        initInput();
    }

    private boolean selecting = false;
    private Vector2f selectionStartPos = new Vector2f();
    private Geometry selectionGeometry;

    private void initInput() {
        ActionListener al = (name, keyPressed, tpf) -> {
            switch (name) {
                case "Left-Click":
                    selecting = keyPressed;
                    //System.out.println("Left: " + keyPressed);
                    if (selecting) {
                        //selectionQuad = new Quad();
                        selectionStartPos.set(inputManager.getCursorPosition());

                        //selectionPoints[0] = selectionStartPos.x;
                        //selectionPoints[1] = selectionStartPos.y;

                        //selectionQuad.

                        selectionGeometry = new Geometry("SelectionQuad", selectionQuad);
                        Material mat = new Material(graphics.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                        mat.setColor("Color", ColorRGBA.White);
                        selectionGeometry.setMaterial(mat);
                        //graphics.getRootNode().attachChild(selectionGeometry);
                        selectionGeometry.setLocalTranslation(selectionStartPos.x, selectionStartPos.y, 1.0f);

                        graphics.getGuiNode().attachChild(selectionGeometry);
                    } else {
                        handleVolumeSelection();

                    }
                    break;

                case "Right-Click":
                    System.out.println("Right: " + keyPressed);
                    break;
            }
        };

        inputManager.addMapping("Left-Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Right-Click", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addListener(al, "Left-Click", "Right-Click");
    }

    private void handleVolumeSelection() {
        graphics.getGuiNode().detachChild(selectionGeometry);

        Vector2f endPos = inputManager.getCursorPosition();

        float selectionMinX = selectionStartPos.x < endPos.x ? selectionStartPos.x : endPos.x;
        float selectionMaxX = selectionStartPos.x > endPos.x ? selectionStartPos.x : endPos.x;
        float selectionMinY = selectionStartPos.y < endPos.y ? selectionStartPos.y : endPos.y;
        float selectionMaxY = selectionStartPos.y > endPos.y ? selectionStartPos.y : endPos.y;

        Vector3f pos = new Vector3f();

        graphics.selected.detachAllChildren();
        graphics.selectedNodes.clear();

        for(Spatial obj : graphics.selectables.getChildren()) {
            if(obj.getName().equals("Ground"))
                continue;

            graphics.getCamera().getScreenCoordinates(obj.getLocalTranslation(), pos);



            if(pos.x >= selectionMinX && pos.x <= selectionMaxX &&
                    pos.y >= selectionMinY && pos.y <= selectionMaxY)
                graphics.selectedNodes.add(obj);


        }

        int i = 0;
        for(Spatial obj : graphics.selectedNodes) {
            //System.out.println("Selected: " + obj.getName());
            WireSphere sphere = new WireSphere(15.0f);
            Geometry geo = new Geometry("selected" + i, sphere);
            Material mat = new Material(graphics.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Red);
            geo.setMaterial(mat);
            graphics.selected.attachChild(geo);
            i++;
        }

    }

    @Override
    public void stop() {

    }
}
