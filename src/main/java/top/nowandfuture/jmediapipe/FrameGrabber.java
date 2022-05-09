package top.nowandfuture.jmediapipe;

import top.nowandfuture.jmediapipe.exception.CameraHasOpenedException;
import top.nowandfuture.jmediapipe.exception.CameraHasReleasedException;
import top.nowandfuture.jmediapipe.exception.CameraNotOpenedException;

import java.util.Arrays;
import java.util.List;


//        cv2.VideoCapture.get(0)	视频文件的当前位置（播放）以毫秒为单位
//        cv2.VideoCapture.get(1)	基于以0开始的被捕获或解码的帧索引
//        cv2.VideoCapture.get(2)	视频文件的相对位置（播放）：0=电影开始，1=影片的结尾。
//        cv2.VideoCapture.get(3)	在视频流的帧的宽度
//        cv2.VideoCapture.get(4)	在视频流的帧的高度
//        cv2.VideoCapture.get(5)	帧速率
//        cv2.VideoCapture.get(6)	编解码的4字-字符代码
//        cv2.VideoCapture.get(7)	视频文件中的帧数
//        cv2.VideoCapture.get(8)	返回对象的格式
//        cv2.VideoCapture.get(9)	返回后端特定的值，该值指示当前捕获模式
//        cv2.VideoCapture.get(10)	图像的亮度(仅适用于照相机)
//        cv2.VideoCapture.get(11)	图像的对比度(仅适用于照相机)
//        cv2.VideoCapture.get(12)	图像的饱和度(仅适用于照相机)
//        cv2.VideoCapture.get(13)	色调图像(仅适用于照相机)
//        cv2.VideoCapture.get(14)	图像增益(仅适用于照相机)（Gain在摄影中表示白平衡提升）
//        cv2.VideoCapture.get(15)	曝光(仅适用于照相机)
//        cv2.VideoCapture.get(16)	指示是否应将图像转换为RGB布尔标志
//        cv2.VideoCapture.get(17)	× 暂时不支持
//        cv2.VideoCapture.get(18)	立体摄像机的矫正标注（目前只有DC1394 v.2.x后端支持这个功能）

public class FrameGrabber implements AutoCloseable{

    public enum CV_PARAM{
        CAP_WIDTH(3),
        CAP_HEIGHT(4),
        CAP_FPS(5);
        int key;
        CV_PARAM(int key){
            this.key = key;
        }
    }

    private long reference;

    public FrameGrabber(){
        reference = 0;
        reference = create();
    }

    public synchronized void open(int idx) throws Exception{
        if(reference > 0){
            boolean res = open(reference, idx);
            if(!res){
                throw new CameraNotOpenedException();
            }
        }else{
            throw new CameraHasReleasedException();
        }
    }

    public boolean setSize(int width, int height) {
        if (reference > 0) {
            boolean ws = set(reference, CV_PARAM.CAP_WIDTH.key, width);
            boolean hs = set(reference, CV_PARAM.CAP_HEIGHT.key, height);

            return ws & hs;
        }
        return false;
    }

    public double get(int key){
        if (reference > 0) {
            return get(reference, key);
        }
        return -1;
    }

    public static List<String> listCameras(){
        return Arrays.asList(listDevices_inner());
    }

    private static String[] listDevices_inner(){
        return listDevices();
    }

    private long create(){
        return createGrabber();
    }

    @Override
    public synchronized void close() throws Exception {
        if(reference > 0){
            boolean success = close(reference);
            if(!success){
                throw new CameraNotOpenedException();
            }
        }else{
            throw new CameraNotOpenedException("Camera has been closed.");
        }
    }

    public synchronized boolean isOpened(){
        if(reference > 0){
            return isOpened(reference);
        }
        return false;
    }

    public synchronized CVFrame readFrame() throws CameraHasReleasedException {
        int[] size = new int[2];
        byte[] data = readFrame_inner(size);

        return new CVFrame(data, size);
    }

    private byte[] readFrame_inner(int[] size) throws CameraHasReleasedException {
        if(reference > 0){
            return readFrame(reference, size);
        }else{
            throw new CameraHasReleasedException();
        }
    }

    private native long createGrabber();
    private native boolean open(long rf, int idx);
    private native boolean close(long rf);
    private static native String[] listDevices();
    private native byte[] readFrame(long rf, int[] size);
    private native boolean isOpened(long rf);
    private native boolean set(long rf, int key, double value);
    private native double get(long rf, int key);
}
