package edu.unr.ecsl.gfx;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.debug.WireSphere;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import edu.unr.ecsl.Manager;
import edu.unr.ecsl.commands.Command;
import edu.unr.ecsl.ents.Entity;

import java.util.ArrayList;
import java.util.List;

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
        }

        graphics.debug.detachAllChildren();

        if(handlingMove) {
            Vector2f mousePos = inputManager.getCursorPosition();

            float yDiff = mousePos.y - mouseMoveY;
            moveTarget.y += yDiff / 30.0f;
            if(moveTarget.y < 1.0f) moveTarget.y = 1.0f;
            else if(moveTarget.y > 600.0f) moveTarget.y = 600.0f;

            graphics.debug.attachChild(graphics.makeDisk(moveTarget, 5.0f, ColorRGBA.Green));
            graphics.debug.attachChild(graphics.makeDisk(rightClickStartPos, 5.0f, ColorRGBA.Green));
            graphics.debug.attachChild(graphics.makeLine(rightClickStartPos, moveTarget, ColorRGBA.Green));

            for(GFXNode gfxNode : graphics.selectedNodes) {
                graphics.debug.attachChild(graphics.makeLine(gfxNode.node.getLocalTranslation(), moveTarget, ColorRGBA.Green));

            }
        }
    }

    @Override
    public void init() {
        selectionQuad.setMode(Quad.Mode.LineStrip);
        initInput();
    }

    private boolean selecting = false, handlingMove = false;
    private Vector2f selectionStartPos = new Vector2f();
    private Geometry selectionGeometry;
    private Vector3f rightClickStartPos, moveTarget;
    private float mouseMoveY;
    public List<Line> debugLines = new ArrayList<>();

    private void initInput() {
        ActionListener al = (name, keyPressed, tpf) -> {
            switch (name) {
                case "Left-Click":
                    selecting = keyPressed;
                    handleVolumeSelection();
                    break;

                case "Right-Click":
                    handlingMove = keyPressed;
                    handleRightClick();
                    break;
            }
        };

        inputManager.addMapping("Left-Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Right-Click", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addListener(al, "Left-Click", "Right-Click");
    }

    private void handleVolumeSelection() {

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
        }

        else {
            graphics.getGuiNode().detachChild(selectionGeometry);

            Vector2f endPos = inputManager.getCursorPosition();

            float selectionMinX = selectionStartPos.x < endPos.x ? selectionStartPos.x : endPos.x;
            float selectionMaxX = selectionStartPos.x > endPos.x ? selectionStartPos.x : endPos.x;
            float selectionMinY = selectionStartPos.y < endPos.y ? selectionStartPos.y : endPos.y;
            float selectionMaxY = selectionStartPos.y > endPos.y ? selectionStartPos.y : endPos.y;

            Vector3f pos = new Vector3f();

            graphics.selected.detachAllChildren();
            graphics.selectedNodes.clear();

            int count = 0;
            for (Spatial obj : graphics.selectables.getChildren()) {
                if (obj.getName().equals("Ground"))
                    continue;

                graphics.getCamera().getScreenCoordinates(obj.getLocalTranslation(), pos);


                if (pos.x >= selectionMinX && pos.x <= selectionMaxX &&
                        pos.y >= selectionMinY && pos.y <= selectionMaxY) {
                    graphics.selectedNodes.add(graphics.nodeMap.get(obj.getName()));
//                    WireSphere sphere = new WireSphere(15.0f);
//                    Geometry geo = new Geometry("Selected", sphere);
//                    Material mat = new Material(graphics.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//                    mat.setColor("Color", ColorRGBA.Red);
//                    geo.setMaterial(mat);
//                    graphics.selected.attachChild(geo);
//
//                    GFXNode gfxNode = new GFXNode();
//                    gfxNode.node = geo;
//                    gfxNode.id = count++;
//                    gfxNode.selectable = false;
//                    gfxNode.actionable = false;
//
//                    graphics.selectedNodes.add(gfxNode);
                }


            }

            //System.out.println(graphics.selectedNodes.size());

//            for (int i = 0; i < graphics.selectedNodes.size(); i++) {
//                //System.out.println("Selected: " + obj.getName());
//                WireSphere sphere = new WireSphere(15.0f);
//                Geometry geo = new Geometry("selected" + i, sphere);
//                Material mat = new Material(graphics.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//                mat.setColor("Color", ColorRGBA.Red);
//                geo.setMaterial(mat);
//                graphics.selected.attachChild(geo);
//            }
        }

    }

    private void handleRightClick() {
        if(handlingMove) {
            mouseMoveY = inputManager.getCursorPosition().y;
            rightClickStartPos = getGroundPosFromClick();

            if(rightClickStartPos == null) {
                handlingMove = false;
                return;
            }

            moveTarget = rightClickStartPos.clone();
        }

        else {
            for(GFXNode gfxNode : graphics.selectedNodes) {
                Entity ent = graphics.engine.entityManager.ents.get(gfxNode.id);
                Command.createMove3DForEnt(ent, moveTarget.clone());

                System.out.println("Move3D: " + moveTarget.toString());
            }
        }
    }

    public Vector3f getGroundPosFromClick() {
        CollisionResults results = new CollisionResults();
        Vector2f clickPos = inputManager.getCursorPosition();
        Vector3f clickPos3D = graphics.getCamera().getWorldCoordinates(clickPos.clone(), 0.0f).clone();
        Vector3f dir = graphics.getCamera().getWorldCoordinates(clickPos.clone(), 1.0f).subtractLocal(clickPos3D).normalizeLocal();
        Ray ray = new Ray(clickPos3D, dir);

        graphics.selectables.getChild("Ground").collideWith(ray, results);

        if(results.getClosestCollision() != null) {
            return results.getClosestCollision().getContactPoint();
        }

        return null;
    }

    @Override
    public void stop() {

    }
}
