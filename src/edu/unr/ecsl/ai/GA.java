package edu.unr.ecsl.ai;

import com.jme3.math.FastMath;
import edu.unr.ecsl.Engine;

import java.util.Arrays;

/**
 * Created by cam on 1/11/15.
 */
public class GA {
    private static GA instance = null;

    public static GA getInstance() {
        if(instance == null)
            instance = new GA();

        return instance;
    }

    private MicroParams params;
    private int chromoLen, signalLen;
    private String bitstring;

    public MicroParams getParams() {
        return params;
    }

    private GA() {
        params = new MicroParams();
        init();
    }

    private void init() {
        bitstring = Engine.getInstance().options.bitstring;
        chromoLen = bitstring.length();
        signalLen = 4;

        parseParameterInput();
    }

    private void parseParameterInput() {
        int[] buffer = new int[chromoLen];

        for (int i = 0; i < chromoLen; i++) {
            buffer[i] = Character.getNumericValue(bitstring.charAt(i));
        }


        int offset = 0;

        int[] param1 = Arrays.copyOfRange(buffer, offset, offset+5);
        offset += 5;

        int[] param2 = Arrays.copyOfRange(buffer, offset, offset+4);
        offset += 4;

        int[] param3 = Arrays.copyOfRange(buffer, offset, offset+6);
        offset += 6;

        int[] param4 = Arrays.copyOfRange(buffer, offset, offset+6);
        offset += 6;

        int[] param5 = Arrays.copyOfRange(buffer, offset, offset+6);
        offset += 6;

        int[] param6 = Arrays.copyOfRange(buffer, offset, offset+4);
        offset += 4;

        int[] param7 = Arrays.copyOfRange(buffer, offset, offset+4);
        offset += 4;

        int[] param8 = Arrays.copyOfRange(buffer, offset, offset+4);

        params.unitValue = decode(param1)+1;
        params.unitRadius = decode(param2)+1;

        params.A = (decode(param3)+1) * 100;
        params.B = (decode(param4)+1) * 1000;
        params.B2 = (decode(param5)+1) * 1000;

        params.m = (decode(param6)+1) * 0.1f;
        params.m2 = (decode(param7)+1) * 0.1f;
        params.n = (decode(param8)+1) * 0.1f;

        System.out.println(params.toString());
    }

    private int decode(int[] param) {
        int result = 0, length = param.length;
        for (int i = 0; i < length; i++) {
            result += (int) (param[i] * FastMath.pow(2f, (float)length-i-1));
        }

        return result;
    }

    public static class MicroParams {
        public int unitValue;
        public int unitRadius;
        public float A;
        public float B;
        public float B2;
        public float m;
        public float m2;
        public float n;

        @Override
        public String toString() {
            return "MicroParams {" +
                    "\n\tunitValue = " + unitValue +
                    "\n\tunitRadius = " + unitRadius +
                    "\n\tA = " + A +
                    "\n\tB = " + B +
                    "\n\tB2 = " + B2 +
                    "\n\tm = " + m +
                    "\n\tm2 = " + m2 +
                    "\n\tn = " + n +
                    "\n}";
        }
    }
}
