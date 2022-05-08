package top.nowandfuture.jmediapipe.callback;

public interface LandmarkCallback {
    void landmark(int idx, float[] landmarks, int count);
}
