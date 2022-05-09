package top.nowandfuture.jmediapipe;

public class ModelFiles {

    public final static String PD_TXT = "/mediapipe/graphs/face_mesh_desktop_live.pbtxt";
    public final static String FACE_DETECT = "/mediapipe/modules/face_detection/face_detection_short_range.tflite";
    public final static String FACE_LANDMARK = "/mediapipe/modules/face_landmark/face_landmark_with_attention.tflite";

    public static String[] getModelFiles(){
        return new String[]{FACE_DETECT, FACE_LANDMARK};
    }
}
