package top.nowandfuture.callback;

import top.nowandfuture.HeadPoseEstimator;
import top.nowandfuture.math.Utils;
import top.nowandfuture.math.Vec2d;
import top.nowandfuture.math.Vec3d;
import top.nowandfuture.Vec3dPool;

public class FMLandmarkCallback implements LandmarkCallback{

    private final Vec3dPool p = Vec3dPool.POOL;
    private final HeadPoseEstimator estimator = new HeadPoseEstimator(1280, 720);

    /**
     * @param idx The input image index, start from 0.
     * @param landmarks The output of the facemesh model, contains 478 landmarks (every landmark is a 3D point).
     *                  The landmarks may larger than 478 * 3 while the output has multi-face results.
     * @param count The detected face number.
     */
    @Override
    public void landmark(int idx, float[] landmarks, int count) {
        //oder to Vec3d List
        //the number of landmarks is 478 * 3 * count. we only take the first (multi-faces) face, if count > 1
        Vec3d[] fmlmk = new Vec3d[468];
        Vec3d[] irislmk = new Vec3d[10];
        Vec2d[] keyPoint2D = new Vec2d[36];

        for(int i = 0; i < 478 * 3; i += 3){
            int l_idx = i / 3;
            Vec3d vec3d = p.get(estimator.getWidth() - landmarks[i], landmarks[i + 1], 0);
            int keyPointIdx = HeadPoseEstimator.getDefaultKeyPointIndex(l_idx);
            if(keyPointIdx != -1){
                keyPoint2D[keyPointIdx] = new Vec2d(vec3d);
            }

            if(l_idx < 468){
                fmlmk[l_idx] = vec3d;
            }else{
                irislmk[l_idx - 468] = vec3d;
            }
        }
        Vec3d[] ret = estimator.getHeadPose(keyPoint2D);

        Vec3d eular = Utils.rotationVector2Eular(ret[0]).scale(Utils.RAD2ANG);
        eular.z = eular.z > 0 ? 180 - eular.z : -eular.z - 180;

//        System.out.println(eular);

        double leftAsR = HeadPoseEstimator.FacialFeatures.eyeAspectRatio(fmlmk, HeadPoseEstimator.Eyes.LEFT);
        double rightAsR = HeadPoseEstimator.FacialFeatures.eyeAspectRatio(fmlmk, HeadPoseEstimator.Eyes.RIGHT);
        double mouthAsR = HeadPoseEstimator.FacialFeatures.mouthAspectRatio(fmlmk);
        double[] irisLXY = HeadPoseEstimator.FacialFeatures.detectIris(fmlmk, irislmk, HeadPoseEstimator.Eyes.LEFT);
        double[] irisRXY = HeadPoseEstimator.FacialFeatures.detectIris(fmlmk, irislmk, HeadPoseEstimator.Eyes.RIGHT);

//        System.out.println(leftAsR + ", " + rightAsR + ", " + mouthAsR);
        System.out.println(irisLXY[0]);

//        if(mouthAsR > 0.5){
//            System.out.println("mouse Open");
//        }
//        if(irisLXY)

        for(Vec3d lmk: fmlmk){
            p.recycle(lmk);
        }

        for(Vec3d lmk: irislmk){
            p.recycle(lmk);
        }
    }
}
