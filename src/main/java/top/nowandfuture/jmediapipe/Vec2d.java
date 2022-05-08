package top.nowandfuture.jmediapipe.math;

import top.nowandfuture.jmediapipe.Vec3d;

public class Vec2d {
    public double x, y;

    public Vec2d(){
        x = y = 0;
    }

    public Vec2d(Vec3d vec3d){
        this.x = vec3d.x;
        this.y = vec3d.y;
    }

    @Override
    public String toString() {
        return "Vec2d{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
