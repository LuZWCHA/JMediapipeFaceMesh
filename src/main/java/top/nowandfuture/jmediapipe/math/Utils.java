package top.nowandfuture.jmediapipe.math;

public class Utils {
    public static float RAD2ANG = (float)( 180 / Math.PI);
    public static double epsilon = 1e-5;

    public static AxisAngle rotationVector2AxisAngle(Vec3d rotationVector) {

        double mo = Math.sqrt(
                rotationVector.x * rotationVector.x + rotationVector.y * rotationVector.y + rotationVector.z * rotationVector.z);

        return new AxisAngle(rotationVector.scale(1 / mo), mo);
    }

    public static Vec3d quaternion2Eular(Vec4d quaternion) {
        // roll (x-axis rotation)
        Vec3d euler = new Vec3d();
        double sinr_cosp = 2 * (quaternion.w * quaternion.x + quaternion.y * quaternion.z);
        double cosr_cosp = 1 - 2 * (quaternion.x * quaternion.x + quaternion.y * quaternion.y);
        euler.z = Fast.atan2(sinr_cosp, cosr_cosp);

        // pitch (y-axis rotation)
        double sinp = 2 * (quaternion.w * quaternion.y - quaternion.z * quaternion.x);
        if (Math.abs(sinp) >= 1)
            euler.y = Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
        else
            euler.y = Math.asin(sinp);

        // yaw (z-axis rotation)
        double siny_cosp = 2 * (quaternion.w * quaternion.z + quaternion.x * quaternion.y);
        double cosy_cosp = 1 - 2 * (quaternion.y * quaternion.y + quaternion.z * quaternion.z);
        euler.x = Fast.atan2(siny_cosp, cosy_cosp);

        return euler;
    }

    public static Vec4d rotationVector2Quaternion(Vec3d rotationVector) {
        AxisAngle axisAngle = rotationVector2AxisAngle(rotationVector);
        final double a = Math.sin(axisAngle.angle / 2);
        return new Vec4d(Math.cos(axisAngle.angle / 2),
                axisAngle.axis.x * a,
                axisAngle.axis.y * a,
                axisAngle.axis.z * a);
    }

    public static Vec3d rotationVector2Eular(Vec3d rotationVector){
        return quaternion2Eular(rotationVector2Quaternion(rotationVector));
    }

    public static int sign(double value) {
        if (value > 0) {
            return 1;
        } else if (value == 0){
            return 0;
        } else {
            return -1;
        }
    }


}
