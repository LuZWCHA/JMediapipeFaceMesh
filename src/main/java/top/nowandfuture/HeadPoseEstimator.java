package top.nowandfuture;

// TODO: 2022/5/5 Head pose estimate use PnP may be?
public class HeadPoseEstimator {
    int width, height;
    double focalLength;
    Vec3d cameraCenter;
    Matrix3d cameraMatrix;
    Vec4d distCoeffs;
    Vec4d rVec, tVec;

    public HeadPoseEstimator(int width, int height) {
        this.width = width;
        this.height = height;

        this.focalLength = height;
        this.cameraCenter = new Vec3d(height * .5, width * .5);
        this.cameraMatrix = new Matrix3d(
                focalLength, 0, cameraCenter.x,
                0, focalLength, cameraCenter.y,
                0, 0, 1
        );
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


        public static double eyeAspectRatio(Vec3d[] image_points, Eyes side){
            Vec3d p1, p2, p3, p4, p5, p6;
            Vec3d tip_of_eyebrow;

            // get the contour points at img pixel first
            // following the eye aspect ratio formula with little modifications
            // to match the facemesh model
            if(side == Eyes.LEFT){
                int[] eyeKey = leftEye;
                Vec3d sum = image_points[eyeKey[10]].add(image_points[eyeKey[11]]);
                p2 = sum.scale(0.5f);
                sum = image_points[eyeKey[13]].add(image_points[eyeKey[14]]);
                p3 = sum.scale(0.5f);
                sum = image_points[eyeKey[2]].add(image_points[eyeKey[3]]);
                p6 = sum.scale(0.5f);
                sum = image_points[eyeKey[5]].add(image_points[eyeKey[6]]);
                p5 = sum.scale(0.5f);
                p1 = image_points[eyeKey[0]];
                p4 = image_points[eyeKey[8]];
                tip_of_eyebrow = image_points[105];
            }else{
                int[] eyeKey = rightEye;
                Vec3d sum = image_points[eyeKey[10]].add(image_points[eyeKey[11]]);
                p2 = sum.scale(0.5f);
                sum = image_points[eyeKey[13]].add(image_points[eyeKey[14]]);
                p3 = sum.scale(0.5f);
                sum = image_points[eyeKey[2]].add(image_points[eyeKey[3]]);
                p6 = sum.scale(0.5f);
                sum = image_points[eyeKey[5]].add(image_points[eyeKey[6]]);
                p5 = sum.scale(0.5f);
                p1 = image_points[eyeKey[8]];
                p4 = image_points[eyeKey[0]];
                tip_of_eyebrow = image_points[334];
            }

            // https://downloads.hindawi.com/journals/cmmm/2020/1038906.pdf
            // Fig (3)
            double ear = p2.sub(p6).normalize() + p3.sub(p5).normalize();
            ear /= (2 * p1.sub(p4).normalize() + 1e-6);
            ear = ear * (tip_of_eyebrow.sub(image_points[2]).normalize() / image_points[6].sub(image_points[2]).normalize());
            return ear;
        }

        public static double mouthAspectRatio(Vec3d[] image_points){
            Vec3d p1 = image_points[78];
            Vec3d p2 = image_points[81];
            Vec3d p3 = image_points[13];
            Vec3d p4 = image_points[311];
            Vec3d p5 = image_points[308];
            Vec3d p6 = image_points[402];
            Vec3d p7 = image_points[14];
            Vec3d p8 = image_points[178];

            double mar = p2.sub(p8).normalize() + p3.sub(p7).normalize() + p4.sub(p6).normalize();
            mar /= (2 * p1.sub(p5).normalize() + 1e-6);
            return mar;
        }

        public static double mouthDistance(Vec3d[] image_points){
            Vec3d p1 = image_points[78];
            Vec3d p5 = image_points[308];
            return p1.sub(p5).normalize();
        }


        public static double mouthHeight(Vec3d[] image_points){
            Vec3d p3 = image_points[13];
            Vec3d p7 = image_points[14];
            return p3.sub(p7).normalize() - 0.5;
        }

        // detect iris through new landmark coordinates produced by mediapipe
        // replacing the old image processing method
        //
        // return:
        // x_rate: how much the iris is toward the left. 0 means totally left and 1 is totally right.
        // y_rate: how much the iris is toward the top. 0 means totally top and 1 is totally bottom.
        public static double[] detectIris(Vec3d[] image_points/*468 points*/, Vec3d[] iris_image_points /*10 points*/, Eyes side){
            int iris_img_point = -1;
            Vec3d p1, p4;
            Vec3d eye_y_high , eye_y_low;
            double x_rate = 0.5, y_rate = 0.5;
            // get the corresponding image coordinates of the landmarks
            if(side == Eyes.LEFT){
                iris_img_point = 468;

                int[] eyeKey = leftEye;
                p1 = image_points[eyeKey[0]];
                p4 = image_points[eyeKey[8]];

                eye_y_high = image_points[eyeKey[12]];
                eye_y_low = image_points[eyeKey[4]];
            }else{
                iris_img_point = 473;

                int[] eyeKey = rightEye;
                p1 = image_points[eyeKey[8]];
                p4 = image_points[eyeKey[0]];

                eye_y_high = image_points[eyeKey[12]];
                eye_y_low = image_points[eyeKey[4]];
            }

            Vec3d p_iris = iris_image_points[iris_img_point - 468];

            // find the projection of iris_image_point on the straight line fromed by p1 and p4
            // through vector dot product
            // to get x_rate
            Vec3d vec_p1_iris = p_iris.sub(p1);
            Vec3d vec_p1_p4 = p4.sub(p1);

            double p1_p4_n = p1.sub(p4).normalize() + 1e-06;
            x_rate = (vec_p1_iris.dot(vec_p1_p4) / p1_p4_n) / p1_p4_n;

            Vec3d vec_eye_h_iris = p_iris.sub(eye_y_high);
            Vec3d vec_eye_h_eye_l = eye_y_low.sub(eye_y_high);

            double y_h_y_l_n = eye_y_high.sub(eye_y_low).normalize() + 1e-06;
            y_rate = (vec_eye_h_eye_l.dot(vec_eye_h_iris) / y_h_y_l_n) / y_h_y_l_n;

            return new double[]{x_rate, y_rate};
        }

    }


}
