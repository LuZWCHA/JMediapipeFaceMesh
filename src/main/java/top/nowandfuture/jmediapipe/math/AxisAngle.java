package top.nowandfuture.jmediapipe.math;

public class AxisAngle {
    public Vec3d axis;
    public double angle;

    public AxisAngle(){

    }

    public AxisAngle(Vec3d axis, double angle){
        this.axis = axis;
        this.angle = angle;
    }

    public static AxisAngle createAxisX(float angle){
        return new AxisAngle(new Vec3d(1, 0, 0), angle);
    }

    public static AxisAngle createAxisY(float angle){
        return new AxisAngle(new Vec3d(0, 1, 0), angle);
    }

    public static AxisAngle createAxisZ(float angle){
        return new AxisAngle(new Vec3d(0, 0, 1), angle);
    }
}
