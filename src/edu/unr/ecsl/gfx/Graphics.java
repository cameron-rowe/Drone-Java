package edu.unr.ecsl.gfx;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;
import edu.unr.ecsl.Engine;
import edu.unr.ecsl.aspects.UnitAI;
import edu.unr.ecsl.commands.Command;
import edu.unr.ecsl.commands.UnitCommand;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.Side;
import edu.unr.ecsl.enums.UnitAspectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cam on 10/10/14.
 */
public class Graphics extends SimpleApplication {
    public Engine engine;
    private RTSCamera rtsCamera;

    public Graphics(Engine e) {
        super(new StatsAppState(), new DebugKeysAppState());
        engine = e;
    }

    private DirectionalLight sun;
    private UIManager uiManager;
    public Node selectables, selected, debug;
    public List<GFXNode> selectedNodes = new ArrayList<>();
    public GFXNode[] gfxNodes;
    public Map<String, GFXNode> nodeMap = new HashMap<>();
    public int nGFXNodes = 0;

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("Assets", FileLocator.class);

        selectables = new Node("Selectable");
        selected = new Node("Selected");
        debug = new Node("Debug");
        rootNode.attachChild(selectables);
        rootNode.attachChild(selected);
        rootNode.attachChild(debug);

        uiManager = new UIManager(this);
        uiManager.init();
        setDisplayStatView(false);

        gfxNodes = new GFXNode[engine.options.maxEntities];
        for (int i = 0; i < engine.options.maxEntities; i++) {
            gfxNodes[i] = new GFXNode();
        }

        setupCamera();
        setupInput();
        setupScene();
        setupWater();

//        TerrainQuad terrain = new TerrainQuad("Ground", 257, 257, null);
//        Material terrain_mat = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
//        Texture terrain_texture = assetManager.loadTexture("Textures/terrain_texture.jpg");
//        terrain_texture.setWrap(Texture.WrapMode.Repeat);
//        terrain_mat.setTexture("DiffuseMap", terrain_texture);
//        terrain.setMaterial(terrain_mat);
//        terrain.setShadowMode(RenderQueue.ShadowMode.Receive);
//        rootNode.attachChild(terrain);

    }

    @Override
    public void simpleUpdate(float dt) {
        selected.detachAllChildren();
        debug.detachAllChildren();

        uiManager.tick(dt);

        for (int i = 0; i < nGFXNodes; i++) {
            Spatial spatial = gfxNodes[i].node;
            spatial.setLocalTranslation(engine.entityManager.ents.get(gfxNodes[i].id).pos);
            spatial.setLocalRotation(engine.entityManager.ents.get(gfxNodes[i].id).rot);
        }

        decorateSelectedEntities();
    }

    @Override
    public void stop() {
        engine.stop();
        super.stop();
    }

    private void setupScene() {
        for (Entity ent : engine.entityManager.ents) {
            addEntity(ent);
        }

        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-0.5f, -0.5f, -0.5f).normalizeLocal());
        //sun.setDirection(new Vector3f(0,-1.0f,0).normalizeLocal());
        rootNode.addLight(sun);

        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
    }

    private void setupCamera() {
        cam.setFrustumFar(3000.0f);
        getStateManager().detach(getStateManager().getState(FlyCamAppState.class));
        rtsCamera = new RTSCamera(RTSCamera.UpVector.Y_UP);
        rtsCamera.setCenter(new Vector3f(250f, 100f, 250f));
        rtsCamera.setDistance(400.0f);
        //rtsCamera.setTilt(-FastMath.QUARTER_PI);
        rtsCamera.setRot(FastMath.TWO_PI);

        rtsCamera.setMaxSpeed(RTSCamera.DoF.FWD, 250f, 0.5f);
        rtsCamera.setMaxSpeed(RTSCamera.DoF.SIDE, 250f, 0.5f);
        rtsCamera.setMaxSpeed(RTSCamera.DoF.DISTANCE, 250f, 0.5f);
        //cam.setFrustumNear(0.01f);
        getStateManager().attach(rtsCamera);

    }

    private void setupWater() {
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        WaterFilter water = new WaterFilter(rootNode, sun.getDirection());
        //water.setWaterHeight(5.0f);
        fpp.addFilter(water);
        viewPort.addProcessor(fpp);

        Quad q = new Quad(10000.0f, 10000.0f);
        Geometry groundPlane = new Geometry("Ground", q);
        groundPlane.rotate(FastMath.HALF_PI, 0.0f, 0.0f);
        groundPlane.setLocalTranslation(-5000.0f, 0.0f, -5000.0f);
        groundPlane.setCullHint(Spatial.CullHint.Always);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        groundPlane.setMaterial(mat);

        selectables.attachChild(groundPlane);
//        SimpleWaterProcessor waterP = new SimpleWaterProcessor(assetManager);
//
//        waterP.setReflectionScene(mainScene);
//
//        Vector3f waterLoc = new Vector3f(0, -3, 0);
//        waterP.setPlane(new Plane(Vector3f.UNIT_Y, waterLoc.dot(Vector3f.UNIT_Y)));
//        viewPort.addProcessor(waterP);
//
//        waterP.setWaterDepth(40.0f);
//        waterP.setDistortionScale(0.05f);
//        waterP.setWaveSpeed(0.05f);
//        Quad quad = new Quad(400,400);
//        quad.scaleTextureCoordinates(new Vector2f(6.0f,6.0f));
//
//        Geometry water = new Geometry("water", quad);
//        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
//        water.setLocalTranslation(200,-3,250);
//        water.setShadowMode(RenderQueue.ShadowMode.Receive);
//        water.setMaterial(waterP.getMaterial());
//        rootNode.attachChild(water);
    }

    private void setupInput() {
        inputManager.setCursorVisible(true);
    }

    public void addEntity(Entity ent) {
        Spatial gfxNode;
        try {
            gfxNode = assetManager.loadModel("Models/" + ent.meshName);
        } catch (AssetNotFoundException e) {
            gfxNode = assetManager.loadModel("Models/Test/CornellBox.j3o");
        }

        gfxNode.setName(ent.uiname);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex;

        if (ent.side == Side.BLUE) {
            tex = assetManager.loadTexture("Textures/ecslDark.bmp");
        } else {
            tex = assetManager.loadTexture("Textures/ecsl.bmp");
        }

        mat.setTexture("ColorMap", tex);
        gfxNode.setMaterial(mat);
        gfxNode.getLocalTransform().setTranslation(ent.pos);
        //drone.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        selectables.attachChild(gfxNode);

        gfxNodes[nGFXNodes].node = gfxNode;
        gfxNodes[nGFXNodes].selectable = ent.selectable;
        gfxNodes[nGFXNodes].actionable = true;
        gfxNodes[nGFXNodes].id = ent.id;

        nodeMap.put(ent.uiname, gfxNodes[nGFXNodes]);

        nGFXNodes += 1;
    }

    private void decorateSelectedEntities() {

        for(GFXNode gfxNode : selectedNodes) {
            Vector3f pos = gfxNode.node.getLocalTranslation().clone();
            pos.y -= 8.0f;
            selected.attachChild(makeDisk(pos, 3.0f, ColorRGBA.Blue));

            UnitAI ai = (UnitAI) engine.entityManager.ents.get(gfxNode.id).getAspect(UnitAspectType.UNITAI);

            if(ai != null) {
                if(!ai.commands.isEmpty()) {
                    UnitCommand command = (UnitCommand) ai.commands.getFirst();

                    if(command != null) {
                        selected.attachChild(makeLine(pos, command.target.location, ColorRGBA.Cyan));
                    }
                }
            }
        }

//        for(GFXNode gfxNode : selectedNodes) {
//            WireSphere sphere = new WireSphere(15.0f);
//            Geometry geo = new Geometry("Selected", sphere);
//            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//            mat.setColor("Color", ColorRGBA.Red);
//            geo.setMaterial(mat);
//            geo.setLocalTranslation(gfxNode.node.getLocalTranslation());
//            selected.attachChild(geo);
//        }
    }

    public Geometry makeLine(Vector3f startPos, Vector3f stopPos, ColorRGBA color) {
        Line line = new Line(startPos, stopPos);
        line.setLineWidth(2.0f);
        Geometry geo = new Geometry("Line", line);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geo.setMaterial(mat);

        return geo;
    }

    public Geometry makeDisk(Vector3f pos, float radius, ColorRGBA color) {
        Cylinder cylinder = new Cylinder(30, 30, radius, 1.0f, true);
        Geometry geo = new Geometry("Disk", cylinder);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geo.setMaterial(mat);

        geo.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X));
        geo.setLocalTranslation(pos.x, pos.y+1.0f, pos.z);

        return geo;
    }
}
