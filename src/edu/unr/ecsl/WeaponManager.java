package edu.unr.ecsl;

import edu.unr.ecsl.enums.EntityType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cam on 1/8/15.
 */
public class WeaponManager implements Manager {
    public Engine engine;
    public Map<EntityType, WeaponType> weaponTypes;

    public WeaponManager(Engine eng) {
        engine = eng;

        weaponTypes = new HashMap<>(EntityType.values().length);
    }

    @Override
    public void tick(float dt) {

    }

    @Override
    public void init() {
        for(EntityType eType : EntityType.values()) {
            switch (eType) {
                case SCV:
                    break;
                case MARINE:
                    weaponTypes.put(eType, WeaponType.Rifle);
                    break;
                case REAPER:
                    break;
                case TANK:
                    weaponTypes.put(eType, WeaponType.Cannon);
                    break;
                case THRO:
                    break;
                case MARAUDER:
                    break;
                case HELLION:
                    break;
                case BANSHEE:
                    weaponTypes.put(eType, WeaponType.Rifle);
                    break;
            }
        }
    }

    @Override
    public void stop() {

    }

}
