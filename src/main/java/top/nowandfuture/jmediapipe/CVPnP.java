package top.nowandfuture.jmediapipe;

import top.nowandfuture.jmediapipe.math.Matrix3d;
import top.nowandfuture.jmediapipe.math.Vec2d;
import top.nowandfuture.jmediapipe.math.Vec3d;
import top.nowandfuture.jmediapipe.math.Vec4d;

public class CVPnP {

    public static Vec3d[] solvePnP(Vec3d[] model3d, Vec2d[] imagePoints, Matrix3d cameraMatrix, Vec4d distCoeffs, Vec3d rVec, Vec3d tVec, int flag){
        double[] model3dArray = new double[model3d.length * 3];
        double[] imagePointsArray = new double[imagePoints.length * 2];
        double[] matrix = new double[9];
        double[] distCoeffsArray = new double[4];

        double[] tVecArray = new double[3], rVecArray = new double[3];

        int i = 0;
        for(Vec3d v: model3d){
            model3dArray[i++] = v.x;
            model3dArray[i++] = v.y;
            model3dArray[i++] = v.z;
        }

        i = 0;
        for(Vec2d v: imagePoints){
            imagePointsArray[i++] = v.x;
            imagePointsArray[i++] = v.y;
        }

        for(int j = 0; j < 3; j++){
            Vec3d row = new Vec3d();
            cameraMatrix.getRow(j, row);
            matrix[j * 3] = row.x;
            matrix[j * 3 + 1] = row.y;
            matrix[j * 3 + 2] = row.z;
        }

        distCoeffsArray[0] = distCoeffs.x;
        distCoeffsArray[1] = distCoeffs.y;
        distCoeffsArray[2] = distCoeffs.z;
        distCoeffsArray[3] = distCoeffs.w;

        rVecArray[0] = rVec.x;
        rVecArray[1] = rVec.y;
        rVecArray[2] = rVec.z;

        tVecArray[0] = tVec.x;
        tVecArray[1] = tVec.y;
        tVecArray[2] = tVec.z;

        solvePnP(model3dArray, imagePointsArray, matrix, distCoeffsArray, rVecArray, tVecArray, true, flag);

        return new Vec3d[]{new Vec3d(rVecArray), new Vec3d(tVecArray)};
    }

    public static Vec3d[] solvePnP(Vec3d[] model3d, Vec2d[] imagePoints, Matrix3d cameraMatrix, Vec4d distCoeffs, int flag){
        double[] model3dArray = new double[model3d.length * 3];
        double[] imagePointsArray = new double[imagePoints.length * 2];
        double[] matrix = new double[9];
        double[] distCoeffsArray = new double[4];

        double[] tVec = new double[3], rVec = new double[3];

        int i = 0;
        for(Vec3d v: model3d){
            model3dArray[i++] = v.x;
            model3dArray[i++] = v.y;
            model3dArray[i++] = v.z;
        }

        i = 0;
        for(Vec2d v: imagePoints){
            imagePointsArray[i++] = v.x;
            imagePointsArray[i++] = v.y;
        }

        for(int j = 0; j < 3; j++){
            Vec3d row = new Vec3d();
            cameraMatrix.getRow(j, row);
            matrix[j * 3] = row.x;
            matrix[j * 3 + 1] = row.y;
            matrix[j * 3 + 2] = row.z;
        }

        distCoeffsArray[0] = distCoeffs.x;
        distCoeffsArray[1] = distCoeffs.y;
        distCoeffsArray[2] = distCoeffs.z;
        distCoeffsArray[3] = distCoeffs.w;

        solvePnP(model3dArray, imagePointsArray, matrix, distCoeffsArray, rVec, tVec, false, flag);
        return new Vec3d[]{new Vec3d(rVec), new Vec3d(tVec)};
    }

    private static native void solvePnP(double[] model3d, double[] imagePoints, double[] cameraMatrix, double[]disCoeffs, double[] rVec, double[] tVec, boolean b, int flag);

}
