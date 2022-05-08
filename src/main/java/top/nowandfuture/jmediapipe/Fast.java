package top.nowandfuture.jmediapipe.math;

import top.nowandfuture.jmediapipe.Utils;

public class Fast {

    public static float atan2(float dy, float dx){
        float ax = Math.abs(dx), ay = Math.abs(dy);
        float a = Math.min(ax, ay)/(Math.max(ax, ay)+(float) Utils.epsilon);
        float s = a*a;
        float r = ((-0.0464964749f * s + 0.15931422f) * s - 0.327622764f) * s * a + a;
        if(ay > ax) r = 1.57079637f - r;
        if(dx < 0) r = 3.14159274f - r;
        if(dy<0) r = -r;

        return r;
    }

    public static double atan2(double dy, double dx){
        double ax = Math.abs(dx), ay = Math.abs(dy);
        double a = Math.min(ax, ay)/(Math.max(ax, ay)+(float) top.nowandfuture.jmediapipe.math.Utils.epsilon);
        double s = a*a;
        double r = ((-0.0464964749 * s + 0.15931422) * s - 0.327622764) * s * a + a;
        if(ay > ax) r = 1.57079637 - r;
        if(dx < 0) r = 3.14159274 - r;
        if(dy<0) r = -r;

        return r;
    }

    public static void main(String[] args) {
        //Test fast version
        double res = 0;
        long start = System.nanoTime();
        for(int i = 0; i < 1000000; i++){
            res = Math.atan2(12, 3);
        }
        double res2 = res / 2;
        System.out.println(System.nanoTime() - start);

        start = System.nanoTime();
        for(int i = 0; i < 1000000; i++){
            res = atan2(12, 3);
        }
        res2 = res / 2;
        System.out.println(System.nanoTime() - start);
    }
}
