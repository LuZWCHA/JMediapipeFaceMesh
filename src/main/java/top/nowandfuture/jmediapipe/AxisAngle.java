package top.nowandfuture.jmediapipe.math;

import top.nowandfuture.jmediapipe.Vec3d;

public class AxisAngle {
    public Vec3d axis;
    public double angle;

    public AxisAngle(){

    }

    public AxisAngle(top.nowandfuture.jmediapipe.math.Vec3d axis, double angle){
        this.axis = axis;
        this.angle = angle;
    }

    public static AxisAngle createAxisX(float angle){
        return new AxisAngle(new top.nowandfuture.jmediapipe.math.Vec3d(1, 0, 0), angle);
    }

    public static AxisAngle createAxisY(float angle){
        return new AxisAngle(new top.nowandfuture.jmediapipe.math.Vec3d(0, 1, 0), angle);
    }

    public static AxisAngle createAxisZ(float angle){
        return new AxisAngle(new top.nowandfuture.jmediapipe.math.Vec3d(0, 0, 1), angle);
    }
}
