package edu.unr.ecsl;

/**
 * Created by cam on 1/8/15.
 */
public class WeaponType {
    public static final WeaponType Fusion_Cutter, Rifle, Hellfire, Cannon, None, Unknown;

    public int damageAmount, damageBonus, damageCooldown, damageFactor;
    public int minRange, maxRange, innerSplashRadius, medianSpashRadius, outerSplashRadius;
    public boolean targetsAir, targetsGround, targetsOwn, valid;

    public String name;

    private void set(String n, int dmgAmount, int dmgBonus, int dmgCooldown, int dmgFactor,
                    int minR, int maxR, int inSplash, int medSplash, int outSplash, boolean tAir,
                    boolean tGround, boolean tOwn)
    {
        name = n;
        damageAmount = dmgAmount;
        damageBonus = dmgBonus;
        damageCooldown = dmgCooldown;
        damageFactor = dmgFactor;

        minRange = minR;
        maxRange = maxR;
        innerSplashRadius = inSplash;
        medianSpashRadius = medSplash;
        outerSplashRadius = outSplash;
        targetsAir = tAir;
        targetsGround = tGround;
        targetsOwn = tOwn;

        valid = true;
    }

    static {
        Fusion_Cutter = new WeaponType();
        Rifle = new WeaponType();
        Hellfire = new WeaponType();
        Cannon = new WeaponType();
        None = new WeaponType();
        Unknown = new WeaponType();

        Fusion_Cutter.set("Fusion_Cutter", 16, 1, 22, 1, 0,  30, 0, 0, 0, false, true, false);
        Rifle        .set("Rifle",         30, 2, 22, 1, 0, 320, 0, 0, 0, true, true, false);
        Hellfire     .set("Hellfire",      25, 1, 22, 1, 0, 200, 200, 0, 300, false, true, false);
        Cannon       .set("Cannon",        30, 3, 37, 1, 0, 300, 50, 70, 100, false, true, false);
        None         .set("None",           0, 0,  0, 0, 0,   0,  0,  0,   0, false, false, false);
        Unknown      .set("Unknown",        0, 0,  0, 0, 0,   0,  0,  0,   0, false, false ,false);
    }
}
