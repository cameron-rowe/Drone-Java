package edu.unr.ecsl.aspects;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import edu.unr.ecsl.DistanceManager;
import edu.unr.ecsl.WeaponManager;
import edu.unr.ecsl.WeaponType;
import edu.unr.ecsl.ents.Entity;
import edu.unr.ecsl.ents.Target;
import edu.unr.ecsl.enums.EntityState;
import edu.unr.ecsl.enums.UnitAspectType;

/**
 * Created by cam on 1/8/15.
 */
public class WeaponAspect extends UnitAspect {
    private float onfire, beingAttacked, cooldown;
    public Target target;
    private boolean isAttacking;
    private int standstill;

    private final static float BEING_ATTACKED = 100f;
    private final static int STANDSTILL = 8;

    private DistanceManager distanceManager;
    private WeaponManager weaponManager;
    private WeaponType weaponType;


    public WeaponAspect(Entity ent) {
        super(ent, UnitAspectType.WEAPON);
    }

    @Override
    public void tick(float dt) {
        if(beingAttacked > 0f)
            beingAttacked -= dt;

        if(onfire > 0f) {
            onfire -= dt;
            entity.speed = 0;
            return;
        }

        if(entity.isAttacking && cooldown <= 0f) {
            if(target.entity == null && distanceManager.closestEnemyDistance[entity.id] <= weaponType.maxRange) {
                target.entity = entity.engine.entityManager.getEntity(distanceManager.closestEnemy[entity.id]);
            }

            if(target.entity != null && target.entity.state == EntityState.ALIVE
                    && target.entity.pos.distance(entity.pos) <= weaponType.maxRange) {

                System.out.println(String.format("%s DAMAGE: %s", entity.uiname, target.entity.uiname));
                dealDamageToTarget(target, dt);
            }

            if(target.entity != null) {
                Vector3f direct = target.entity.pos.subtract(entity.pos);
                float yaw = -FastMath.atan2(direct.z, direct.x);
                entity.desiredHeading = yaw;
            }
        }

        else if(cooldown >= 0f)
            cooldown -= dt;

        if(target.entity != null && target.entity.state != EntityState.ALIVE)
            target.entity = null;
    }

    @Override
    public void init() {
        distanceManager = entity.engine.distanceManager;
        weaponManager = entity.engine.weaponManager;
        weaponType = weaponManager.weaponTypes.get(entity.type);

        target = new Target();
        target.entity = null;

        cooldown = 0;
        standstill = 8;
        onfire = 0;

        beingAttacked = 0f;
    }

    private void dealDamageToTarget(Target tgt, float dt) {
        float damage = weaponType.damageAmount;
        WeaponAspect wa = (WeaponAspect) tgt.entity.getAspect(UnitAspectType.WEAPON);
        if(wa != null)
            wa.takeDamage(damage);
        cooldown = weaponType.damageCooldown;
        onfire = standstill;
    }

    private void takeDamage(float amount) {
        if(entity.state == EntityState.ALIVE) {
            beingAttacked = BEING_ATTACKED;
            entity.hitpoints -= amount;

            if(entity.hitpoints <= 0) {
                entity.state = EntityState.DYING;
                entity.hitpoints = 0;
            }
        }
    }
}
