package top.nowandfuture;

import top.nowandfuture.math.Matrix3d;
import top.nowandfuture.math.Vec2d;
import top.nowandfuture.math.Vec3d;
import top.nowandfuture.math.Vec4d;

import java.util.HashMap;
import java.util.Map;

// TODO: 2022/5/5 Head pose estimate use PnP may be?
public class HeadPoseEstimator {
    private int width, height;
    private double focalLength;
    private Vec3d cameraCenter;
    private Matrix3d cameraMatrix;
    private Vec4d distCoeffs;
    private Vec3d rVec, tVec;

    private static Vec3d[] defaultFaceModel3D;
    private static Map<Integer, Integer> idxMap = new HashMap<>();

    static {
        //canonical_landmarks
        defaultFaceModel3D = new Vec3d[]{
                new Vec3d(0.0, -0.46317, 7.58658),
                new Vec3d(0.0, 2.473255, 5.788627),
                new Vec3d(0.0, 8.261778, 4.481535),
                new Vec3d(-4.445859, 2.663991, 3.173422),
                new Vec3d(-6.279331, 6.615427, 1.42585),
                new Vec3d(-2.456206, -4.342621, 4.283884),
                new Vec3d(-3.523964, 8.005976, 3.729163),
                new Vec3d(-5.258659, 0.945811, 2.974312),
                new Vec3d(-3.300681, 0.861641, 3.872784),
                new Vec3d(-1.820731, 1.467954, 4.224124),
                new Vec3d(-7.743095, 2.364999, -2.005167),
                new Vec3d(-1.785794, -0.978284, 4.85047),
                new Vec3d(-7.270895, -2.890917, -2.252455),
                new Vec3d(-1.856432, 2.585245, 3.757904),
                new Vec3d(-5.085276, -7.17859, 0.714711),
                new Vec3d(-6.407571, 2.236021, 1.560843),
                new Vec3d(-6.234883, -1.94443, 1.663542),
                new Vec3d(-1.246815, 0.230297, 5.681036),
                new Vec3d(0.0, -7.942194, 5.181173),
                new Vec3d(-3.832928, -1.537326, 4.137731),
                new Vec3d(4.445859, 2.663991, 3.173422),
                new Vec3d(6.279331, 6.615427, 1.42585),
                new Vec3d(2.456206, -4.342621, 4.283884),
                new Vec3d(3.523964, 8.005976, 3.729163),
                new Vec3d(5.258659, 0.945811, 2.974312),
                new Vec3d(3.300681, 0.861641, 3.872784),
                new Vec3d(1.820731, 1.467954, 4.224124),
                new Vec3d(7.743095, 2.364999, -2.005167),
                new Vec3d(1.785794, -0.978284, 4.85047),
                new Vec3d(7.270895, -2.890917, -2.252455),
                new Vec3d(1.856432, 2.585245, 3.757904),
                new Vec3d(5.085276, -7.17859, 0.714711),
                new Vec3d(6.407571, 2.236021, 1.560843),
                new Vec3d(6.234883, -1.94443, 1.663542),
                new Vec3d(1.246815, 0.230297, 5.681036),
                new Vec3d(3.832928, -1.537326, 4.137731)
        };
        int[] idx = new int[]{4, 6, 10, 33, 54, 61, 67, 117, 119, 121, 127, 129, 132, 133, 136, 143, 147, 198, 199, 205, 263, 284, 291, 297, 346, 348, 350, 356, 358, 361, 362, 365, 372, 376, 420, 425};
        for(int i = 0; i < idx.length; i++){
            idxMap.put(idx[i], i);
        }
    }

    public HeadPoseEstimator(int width, int height) {
        this.width = width;
        this.height = height;

        this.focalLength = width;
        this.cameraCenter = new Vec3d(width * .5, height * .5);
        this.cameraMatrix = new Matrix3d(
                focalLength, 0, cameraCenter.x,
                0, focalLength, cameraCenter.y,
                0, 0, 1
        );

        this.distCoeffs = new Vec4d(0, 0, 0, 0);

//        this.rVec = new Vec3d(0.01891013, 0.08560084, -3.14392813);
//        this.tVec = new Vec3d(-14.97821226, -10.62040383, -2053.03596872);
    }

    public Vec3d[] getHeadPose(Vec2d[] keyPoints){
        Vec3d[] rets;
//        if(this.rVec == null) {
            rets = CVPnP.solvePnP(defaultFaceModel3D, keyPoints, this.cameraMatrix, this.distCoeffs, 0);
//        }else {
//            rets = CVPnP.solvePnP(defaultFaceModel3D, keyPoints, this.cameraMatrix, this.distCoeffs,this.rVec, this.tVec, 0);
//
//            this.rVec = rets[0];
//            this.tVec = rets[1];
//        }

        return rets;
    }

    public static int getDefaultKeyPointIndex(int idx){
        return idxMap.getOrDefault(idx, -1);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public enum Eyes {
        LEFT,
        RIGHT
    }

    public static class FacialFeatures {
        public static int[] leftEye = new int[]{
                //LowerContour
                33, 7, 163, 144, 145, 153, 154, 155, 133,
                //UpperContour
                246, 161, 160, 159, 158, 157, 173
        };

        public static int[] rightEye = new int[]{
                //LowerContour
                263, 249, 390, 373, 374, 380, 381, 382, 362,
                //UpperContour
                466, 388, 387, 386, 385, 384, 398
        };


        public static double eyeAspectRatio(Vec3d[] imagePoints, Eyes side){
            Vec3d p1, p2, p3, p4, p5, p6;
            Vec3d tipOfEyebrow;

            // get the contour points at img pixel first
            // following the eye aspect ratio formula with little modifications
            // to match the facemesh model
            if(side == Eyes.LEFT){
                int[] eyeKey = leftEye;
                Vec3d sum = imagePoints[eyeKey[10]].add(imagePoints[eyeKey[11]]);
                p2 = sum.scale(0.5f);
                sum = imagePoints[eyeKey[13]].add(imagePoints[eyeKey[14]]);
                p3 = sum.scale(0.5f);
                sum = imagePoints[eyeKey[2]].add(imagePoints[eyeKey[3]]);
                p6 = sum.scale(0.5f);
                sum = imagePoints[eyeKey[5]].add(imagePoints[eyeKey[6]]);
                p5 = sum.scale(0.5f);
                p1 = imagePoints[eyeKey[0]];
                p4 = imagePoints[eyeKey[8]];
                tipOfEyebrow = imagePoints[105];
            }else{
                int[] eyeKey = rightEye;
                Vec3d sum = imagePoints[eyeKey[10]].add(imagePoints[eyeKey[11]]);
                p2 = sum.scale(0.5f);
                sum = imagePoints[eyeKey[13]].add(imagePoints[eyeKey[14]]);
                p3 = sum.scale(0.5f);
                sum = imagePoints[eyeKey[2]].add(imagePoints[eyeKey[3]]);
                p6 = sum.scale(0.5f);
                sum = imagePoints[eyeKey[5]].add(imagePoints[eyeKey[6]]);
                p5 = sum.scale(0.5f);
                p1 = imagePoints[eyeKey[8]];
                p4 = imagePoints[eyeKey[0]];
                tipOfEyebrow = imagePoints[334];
            }

            // https://downloads.hindawi.com/journals/cmmm/2020/1038906.pdf
            // Fig (3)
            double ear = p2.sub(p6).normalize() + p3.sub(p5).normalize();
            ear /= (2 * p1.sub(p4).normalize() + 1e-6);
            ear = ear * (tipOfEyebrow.sub(imagePoints[2]).normalize() / imagePoints[6].sub(imagePoints[2]).normalize());
            return ear;
        }

        public static double mouthAspectRatio(Vec3d[] imagePoints){
            Vec3d p1 = imagePoints[78];
            Vec3d p2 = imagePoints[81];
            Vec3d p3 = imagePoints[13];
            Vec3d p4 = imagePoints[311];
            Vec3d p5 = imagePoints[308];
            Vec3d p6 = imagePoints[402];
            Vec3d p7 = imagePoints[14];
            Vec3d p8 = imagePoints[178];

            double mar = p2.sub(p8).normalize() + p3.sub(p7).normalize() + p4.sub(p6).normalize();
            mar /= (2 * p1.sub(p5).normalize() + 1e-6);
            return mar;
        }

        public static double mouthDistance(Vec3d[] imagePoints){
            Vec3d p1 = imagePoints[78];
            Vec3d p5 = imagePoints[308];
            return p1.sub(p5).normalize();
        }


        public static double mouthHeight(Vec3d[] imagePoints){
            Vec3d p3 = imagePoints[13];
            Vec3d p7 = imagePoints[14];
            return p3.sub(p7).normalize() - 0.5;
        }


        /**
         * @param imagePoints All face landmarks (points)
         * @param irisImagePoints Iris points
         * @param side Eye side
         * @return x_rate: how much the iris is toward the left. 0 means totally left and 1 is totally right.
         *         y_rate: how much the iris is toward the top. 0 means totally top and 1 is totally bottom.
         */
        public static double[] detectIris(Vec3d[] imagePoints/*468 points*/, Vec3d[] irisImagePoints /*10 points*/, Eyes side){
            int iris_img_point;
            Vec3d p1, p4;
            Vec3d eye_y_high , eye_y_low;
            double x_rate, y_rate;
            // get the corresponding image coordinates of the landmarks
            if(side == Eyes.LEFT){
                iris_img_point = 468;

                int[] eyeKey = leftEye;
                p1 = imagePoints[eyeKey[0]];
                p4 = imagePoints[eyeKey[8]];

                eye_y_high = imagePoints[eyeKey[12]];
                eye_y_low = imagePoints[eyeKey[4]];
            }else{
                iris_img_point = 473;

                int[] eyeKey = rightEye;
                p1 = imagePoints[eyeKey[8]];
                p4 = imagePoints[eyeKey[0]];

                eye_y_high = imagePoints[eyeKey[12]];
                eye_y_low = imagePoints[eyeKey[4]];
            }

            Vec3d p_iris = irisImagePoints[iris_img_point - 468];

            // find the projection of iris_image_point on the straight line formed by p1 and p4
            // through vector dot product
            // to get x_rate
            Vec3d vecP1Iris = p_iris.sub(p1);
            Vec3d vecP1P4 = p4.sub(p1);

            double p1P4N = p1.sub(p4).normalize() + 1e-06;
            x_rate = (vecP1Iris.dot(vecP1P4) / p1P4N) / p1P4N;

            Vec3d vecEyeHIris = p_iris.sub(eye_y_high);
            Vec3d vecEyeHEyeL = eye_y_low.sub(eye_y_high);

            double yHYLN = eye_y_high.sub(eye_y_low).normalize() + 1e-06;
            y_rate = (vecEyeHEyeL.dot(vecEyeHIris) / yHYLN) / yHYLN;

            return new double[]{x_rate, y_rate};
        }

    }


}
