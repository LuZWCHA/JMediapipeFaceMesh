package top.nowandfuture.math;

public class Vec4d {
    public double x, y ,z, w;

    public Vec4d(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 0;
    }

    public static Vec4d ones(){
        return new Vec4d(1, 1, 1, 1);
    }

    public static Vec4d zeros(){
        return new Vec4d(0, 0, 0, 0);
    }
}
