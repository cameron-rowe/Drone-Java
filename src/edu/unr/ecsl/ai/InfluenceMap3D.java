package edu.unr.ecsl.ai;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.enums.EntityState;
import edu.unr.ecsl.enums.Side;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cam on 1/8/15.
 */
public class InfluenceMap3D {

    public int[][][] map;

    public int dataSizeX, dataSizeY, dataSizeZ;
    public int numCells;
    public int worldSizeX, worldSizeY, worldSizeZ;
    public int cellResX, cellResY, cellResZ;
    public int currentMax;

    public Vector3f targetPos;

    public Map<Entity, RegObj3D> registeredObjects;

    protected float updatedt;

    public MapType influenceType;

    public InfluenceMap3D(MapType type) {
        registeredObjects = new TreeMap<>();
        targetPos = new Vector3f();
        influenceType = type;
        updatedt = 0.0f;
    }

    public void tick(float dt) {
        if(updatedt < 3.0f) {
            updatedt += dt;
            return;
        }

        updatedt = 0;

        if(registeredObjects.size() == 0)
            return;

        for (int i = 0; i < dataSizeX; i++)
            for (int j = 0; j < dataSizeY; j++)
                for (int k = 0; k < dataSizeZ; k++)
                    map[i][j][k] = 0;

        currentMax = Integer.MAX_VALUE;

        int value = 30, radius = 13;

        for(Map.Entry<Entity, RegObj3D> entry : registeredObjects.entrySet()) {
            if(!entry.getValue().exist) continue;
            if(entry.getKey().state != EntityState.ALIVE) continue;

            int posX = (int) entry.getKey().pos.x;
            int posY = (int) entry.getKey().pos.y;
            int posZ = (int) entry.getKey().pos.z;

            if(posX > worldSizeX) posX = entry.getValue().lastPosX;
            if(posY > worldSizeY) posY = entry.getValue().lastPosY;
            if(posZ > worldSizeZ) posZ = entry.getValue().lastPosZ;

            //float hp = entry.getKey().hitpoints;
            stampInfluenceGradientSum(map, posX, posY, posZ, value, radius);
        }

        for (int i = 0; i < dataSizeX; i++)
            for (int j = 0; j < dataSizeY; j++)
                for (int k = 0; k < dataSizeZ; k++) {
                    if(map[i][j][k] > 100)
                        map[i][j][k] = 100;

                    if(map[i][j][k] < currentMax && map[i][j][k] != 0) {
                        int x = i * cellResX + (cellResX/2);
                        int y = j * cellResY + (cellResY/2);
                        int z = k * cellResZ + (cellResZ/2);

                        targetPos.set(x,z,y);
                        currentMax = map[i][j][k];
                    }

                }
    }

    public void init(int sizeX, int sizeY, int sizeZ, int wSizeX, int wSizeY, int wSizeZ) {
        dataSizeX = sizeX;
        dataSizeY = sizeY;
        dataSizeZ = sizeZ;
        numCells = dataSizeX * dataSizeY * dataSizeZ;

        worldSizeX = wSizeX;
        worldSizeY = wSizeY;
        worldSizeZ = wSizeZ;

        cellResX = worldSizeX / dataSizeX;
        cellResY = worldSizeY / dataSizeY;
        cellResZ = worldSizeZ / dataSizeZ;

        map = new int[dataSizeX][dataSizeY][dataSizeZ];
    }

    public void stampInfluenceGradientSum(int[][][] pMap, int pos_x, int pos_y, int pos_z, int initValue, int radius) {
        int gridX = (int) FastMath.floor((float)pos_x/ (float)cellResX);
        int gridY = (int) FastMath.floor((float)pos_y/ (float)cellResY);
        int gridZ = (int) FastMath.floor((float)pos_z/ (float)cellResZ);
        int startX = gridX - radius;
        int stopX  = gridX + radius;
        if(startX < 0) startX = 0;
        if(stopX > dataSizeX) stopX = dataSizeX;
        int startY = gridY - radius;
        int stopY  = gridY + radius;
        if(startY < 0) startY = 0;
        if(stopY > dataSizeY) stopY = dataSizeY;
        int startZ = gridZ - radius;
        int stopZ = gridZ + radius;
        if(startZ < 0) startZ = 0;
        if(stopZ > dataSizeZ) stopZ = dataSizeZ;

        for(int x = startX; x < stopX; x++) {
            for(int y = startY; y < stopY; y++) {
                for(int z = startZ; z < stopZ; z++) {
                    int value = 0;
                    int distX = (int) FastMath.abs((float)(x - gridX));
                    int distY = (int) FastMath.abs((float) (y - gridY));
                    int distZ = (int) FastMath.abs((float)(z - gridZ));

                    float destD = 0;
                    if(!(x==gridX && y==gridY && z==gridZ)) {
                        destD = FastMath.sqrt((float) ((x - gridX) * (x - gridX) + (y - gridY) * (y - gridY) + (z - gridZ) * (z - gridZ)));
                    }

                    int destI = (int) FastMath.ceil(destD);

                    if(radius >= destI) {
                        value += initValue*(radius-destI)/radius;
                    }

                    pMap[x][y][z] += value;


                }
            }
        }
    }

    public void reset() {
        map = new int[dataSizeX][dataSizeY][dataSizeZ];
        registeredObjects.clear();
    }

    public void registerGameObject(Entity ent) {
        if(ent.player == ent.engine.options.player || ent.side == Side.NEUTRAL) {
            System.out.println("IM Ignored: " + ent.uiname);
            return;
        }

        RegObj3D obj = new RegObj3D();
        obj.lastPosX = (int) ent.pos.x;
        obj.lastPosY = (int) ent.pos.y;
        obj.lastPosZ = (int) ent.pos.z;
        obj.stamped = false;
        obj.exist = true;

        registeredObjects.put(ent, obj);
    }

    public void removeGameObject(Entity ent) {
        registeredObjects.get(ent).exist = false;
        registeredObjects.remove(ent);
    }

    static class RegObj3D {
        public Entity entity;

        public int objSizeX, objSizeY, objSizeZ;
        public int objType;
        public int lastPosX, lastPosY, lastPosZ;
        public int objSide, currentGrid;

        public boolean stamped, isBuilding, exist;

        public String typeName;

    }

    enum MapType {
        IM_NONE,
        IM_OCCUPANCE,
        IM_CONTROL,
        IM_BITWISE
    }
}
