package edu.unr.ecsl.gfx;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import edu.unr.ecsl.Manager;
import edu.unr.ecsl.ai.InfluenceMap3D;
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
    private boolean renderIM = false;
    @Override
    public void tick(float dt) {
        if (selecting) {
            Vector2f cursorPosition = inputManager.getCursorPosition();
            float width = -(selectionStartPos.x - cursorPosition.x);//Math.abs(selectionStartPos.x - cursorPosition.x);
            float height = -(selectionStartPos.y - cursorPosition.y);//Math.abs(selectionStartPos.y - cursorPosition.y);

            selectionGeometry.setLocalTranslation(selectionStartPos.x + width / 2.0f, selectionStartPos.y + height / 2.0f, 1.0f);
            selectionQuad.updatePositions(width / 2.0f, height / 2.0f, 1.0f);
        }

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

        if(renderIM)
            drawInfluenceMap();
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

                case "I":
                    if(keyPressed)
                        renderIM = !renderIM;
                    break;
            }
        };

        inputManager.addMapping("Left-Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Right-Click", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("I", new KeyTrigger(KeyInput.KEY_I));

        inputManager.addListener(al, "Left-Click", "Right-Click", "I");
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

            graphics.selectedNodes.clear();

            for (Spatial obj : graphics.selectables.getChildren()) {
                if (obj.getName().equals("Ground"))
                    continue;

                graphics.getCamera().getScreenCoordinates(obj.getLocalTranslation(), pos);


                if (pos.x >= selectionMinX && pos.x <= selectionMaxX &&
                        pos.y >= selectionMinY && pos.y <= selectionMaxY) {
                    graphics.selectedNodes.add(graphics.nodeMap.get(obj.getName()));
                }
            }
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

    private void drawInfluenceMap() {
        InfluenceMap3D map = graphics.engine.infoManager.map;
        Box box = new Box();
        int maxValue = 0;

        for (int i = 0; i < map.dataSizeX; i++) {
            for (int j = 0; j < map.dataSizeY; j++) {
                for (int k = 0; k < map.dataSizeZ; k++) {
                    int x = (int) ((float) i * (float) map.cellResX + ((float) map.cellResX/2f));
                    int y = (int) ((float) j * (float) map.cellResY + ((float) map.cellResY/2f));
                    int z = (int) ((float) k * (float) map.cellResZ + ((float) map.cellResZ/2f));

                    if(map.map[i][j][k] == 0)
                        continue;

                    int offset = 32;
                    int xMax = x + map.cellResX - offset;
                    int yMax = y + map.cellResY - offset;
                    int zMax = z + map.cellResZ - offset;

                    float map_value = (float)map.map[i][j][k] / 100f;

                    Vector3f min = new Vector3f(x, y, z), max = new Vector3f(xMax, yMax, zMax);
                    max.subtractLocal(min);
                    min.zero();
                    box.updateGeometry(min, max);
                    Geometry geo = new Geometry("Box", box);
                    ColorRGBA color = new ColorRGBA(0, map_value, 1.0f-map_value, 0.2f);
                    Material mat = new Material(graphics.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
                    mat.setColor("Color", color);
                    geo.setMaterial(mat);
                    min.set(x,y,z);
                    geo.setLocalTranslation(min);
                    graphics.debug.attachChild(geo);
                }
            }
        }

        Vector3f point = map.targetPos;
        Vector3f point2 = point.clone();
        point2.y += 1000f;
        graphics.debug.attachChild(graphics.makeLine(point, point2, ColorRGBA.Red));
    }

    @Override
    public void stop() {

    }
}
