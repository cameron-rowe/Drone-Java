package edu.unr.ecsl;

import com.jme3.math.FastMath;

/**
 * Created by cam on 1/7/15.
 */
public class Util {
    public static float makeAnglePosNeg(float angle)
    {
        while (angle > FastMath.PI)
            angle -= FastMath.TWO_PI;
        while (angle < -FastMath.PI)
            angle += FastMath.TWO_PI;

        return angle;
    }

    public static float differenceBetweenAngles(float angle1, float angle2){
        return makeAnglePosNeg(angle1 - angle2);
    }

    public static float feet(float x){
        return 0.3048f * x;
    }
    public static float meters(float x){
        return x;
    }

    public static float knots(float x) {
        return 0.514444444f * x;
    }
    public static float feetPerSecondSquared(float x) {
        return 0.3048f * x;
    }

    public static float degreesPerSecond(float x) {
        return 0.0174532925f * x;
    }

    public static float tons(float x){
        return x * 907.18474f;
    }
    public static float pounds(float x){
        return x * 0.45359237f;
    }
}
