package top.nowandfuture.callback;

import top.nowandfuture.HeadPoseEstimator;
import top.nowandfuture.Vec3d;
import top.nowandfuture.Vec3dPool;

public class FMLandmarkCallback implements LandmarkCallback{
    @Override
    public void landmark(int idx, float[] landmarks, int count) {
        //oder to Vec3d List
        //478 * 3 * count we only take the first if count > 1
        Vec3dPool p = Vec3dPool.POOL;
        Vec3d[] fmlmk = new Vec3d[468];
        Vec3d[] irislmk = new Vec3d[10];
        Vec3d[] keyPoint3D = new Vec3d[2];//nose 197. forehead 9.

        for(int i = 0; i < 478 * 3; i += 3){
            int l_idx = i / 3;
            Vec3d vec3d = p.get(landmarks[i], landmarks[i + 1], 0);

            if(l_idx < 468){
                fmlmk[l_idx] = vec3d;
            }else{
                irislmk[l_idx - 468] = vec3d;
            }
        }

        double leftAsR = HeadPoseEstimator.FacialFeatures.eyeAspectRatio(fmlmk, HeadPoseEstimator.Eyes.LEFT);
        double rightAsR = HeadPoseEstimator.FacialFeatures.eyeAspectRatio(fmlmk, HeadPoseEstimator.Eyes.RIGHT);
        double mouthAsR = HeadPoseEstimator.FacialFeatures.mouthAspectRatio(fmlmk);
        double[] irisLXY = HeadPoseEstimator.FacialFeatures.detectIris(fmlmk, irislmk, HeadPoseEstimator.Eyes.LEFT);
        double[] irisRXY = HeadPoseEstimator.FacialFeatures.detectIris(fmlmk, irislmk, HeadPoseEstimator.Eyes.RIGHT);

        System.out.println(leftAsR + ", " + rightAsR + ", " + mouthAsR);
        System.out.println(irisLXY[0]+ ", " + irisRXY[1]);

        for(Vec3d lmk: fmlmk){
            p.recycle(lmk);
        }

        for(Vec3d lmk: irislmk){
            p.recycle(lmk);
        }
    }
}
