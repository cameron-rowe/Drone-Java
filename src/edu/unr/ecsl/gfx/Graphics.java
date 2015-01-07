package edu.unr.ecsl.gfx;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.input.KeyInput;
import com.jme3.scene.shape.Quad;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import edu.unr.ecsl.Engine;

/**
 * Created by cam on 10/10/14.
 */
public class Graphics extends SimpleApplication {
    private Engine engine;
    private RTSCamera rtsCamera;

    public Graphics(Engine e)
    {
        super(new StatsAppState(), new DebugKeysAppState());
        engine = e;
    }
    /**
     *
     * <pre>
     * getStateManager().detach(getStateManager().getState(FlyCamAppState.class));
     * RtsCam rtsCam = new RtsCam(UpVector.Y_UP);
     * rtsCam.setCenter(new Vector3f(0, 0, 0));
     * rtsCam.setDistance(200);
     * rtsCam.setMaxSpeed(DoF.FWD, 100, 0.5f);
     * rtsCam.setMaxSpeed(DoF.SIDE, 100, 0.5f);
     * rtsCam.setMaxSpeed(DoF.DISTANCE, 100, 0.5f);
     * rtsCam.setHeightProvider(new HeightProvider() {
     *     public float getHeight(Vector2f coord) {
     *         return terrain.getHeight(coord)+10;
     *     }
     * });
     * getStateManager().attach(rtsCam);
     * </pre>
     *
     */

    private DirectionalLight sun;
    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("Assets", FileLocator.class);
        setDisplayStatView(false);

        setupCamera();

        //setupInput();
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
    public void simpleUpdate(float dt)
    {
    }

    @Override
    public void stop() {
        engine.stop();
        super.stop();
    }

    private void setupScene() {
        Spatial drone = assetManager.loadModel("Models/Test/Monkey.obj");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //Texture tex = assetManager.loadTexture("Textures/BeachStones.jpg");
        //mat.setTexture("ColorMap", tex);
        drone.setMaterial(mat);
        drone.getLocalTransform().setTranslation(0.0f, 10.0f, 0.0f);
        //drone.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(drone);

        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-0.5f,-0.5f,-0.5f).normalizeLocal());
        //sun.setDirection(new Vector3f(0,-1.0f,0).normalizeLocal());
        rootNode.addLight(sun);

//        AmbientLight amb = new AmbientLight();
//        amb.setColor(ColorRGBA.White.mult(0.8f));
//        rootNode.addLight(amb);

        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
    }

    private void setupCamera() {
        cam.setFrustumFar(3000.0f);
        getStateManager().detach(getStateManager().getState(FlyCamAppState.class));
        rtsCamera = new RTSCamera(RTSCamera.UpVector.Y_UP);
        rtsCamera.setCenter(Vector3f.ZERO);
        rtsCamera.setDistance(400.0f);
        //rtsCamera.setTilt(-FastMath.QUARTER_PI);
        rtsCamera.setRot(FastMath.TWO_PI);

        rtsCamera.setMaxSpeed(RTSCamera.DoF.FWD, 100.0f, 0.5f);
        rtsCamera.setMaxSpeed(RTSCamera.DoF.SIDE, 100.0f, 0.5f);
        rtsCamera.setMaxSpeed(RTSCamera.DoF.DISTANCE, 100.0f, 0.5f);
        //cam.setFrustumNear(0.01f);
        getStateManager().attach(rtsCamera);

    }

    private void setupWater() {
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        WaterFilter water = new WaterFilter(rootNode, sun.getDirection());
        //water.setWaterHeight(5.0f);
        fpp.addFilter(water);
        viewPort.addProcessor(fpp);
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
        AnalogListener al = (name, keypressed, tfp) -> {
          switch(name) {
              case "W":
                  System.out.println("w!");

                  break;

              case "A":
                  System.out.println("a!");
                  break;

              case "S":
                  System.out.println("s!");
                  break;

              case "D":
                  System.out.println("d!");
                  break;

          }
        };
        inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));

        inputManager.addListener(al, "W", "A", "S", "D");

        inputManager.setCursorVisible(true);
    }
}
