package top.nowandfuture.jmediapipe;

import top.nowandfuture.jmediapipe.callback.LandmarkCallback;

public class Graph {

    private long reference;

    public Graph(){
        reference = createGraph();
    }

    public synchronized int init(String pbtxtPath, String rootPath){
        return init(reference, pbtxtPath, rootPath);
    }

    public synchronized int setCallback(LandmarkCallback callback){
        if(reference > 0){
            return registerCallback(reference, callback);
        }
        return 1;
    }

    public synchronized int landmarks(long idx, CVFrame frame){
        return landmarks_inner(idx, frame.getData(), frame.getWidth(), frame.getHeight());
    }

    private int landmarks_inner(long idx, byte[] imageBuff, int width, int height){
        if(reference > 0){
            return detect(reference, idx, imageBuff, width, height);
        }
        return 0;
    }

    public synchronized void release(){
        if(reference > 0){
            release(reference);
        }
    }

    private native long createGraph();
    private native int init(long rf, String pbtxtPath, String rootPath);
    private native int detect(long rf, long index, byte[] frame, int width, int height);
    private native int registerCallback(long rf, LandmarkCallback callback);
    private native void release(long rf);
}
