package top.nowandfuture.jmediapipe;

import top.nowandfuture.jmediapipe.callback.FMLandmarkCallback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Loader.loadLibs();
            Loader.copySources();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        FrameGrabber grabber = new FrameGrabber();
        List<String> devices = grabber.listDevices();

        for (int i = 0; i < devices.size(); i++) {
            System.out.println(i + ", " + devices.get(i));
        }

        Graph graph = new Graph();

        int ret = graph.init("./mediapipe/graphs/face_mesh_desktop_live.pbtxt");
        if(ret != 0){
            System.out.println("Graph init failed: " + ret);
        }
        ret = graph.setCallback(new FMLandmarkCallback());
        if(ret != 0){
            System.out.println("Callback register failed: " + ret);
        }

        try {
            grabber.open(0);
            grabber.setSize(1280, 720);

            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    try {
                        grabber.close();
                        graph.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            long i = 0;
            while(true){
                if(!grabber.isOpened()){
                    break;
                }

                CVFrame frame = grabber.readFrame();
                if(frame != null){
                    ret = graph.landmarks(i ++, frame);
                    if(ret > 0){
                        System.out.println("Graph Running error: " + ret);
                    }
                }else{
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(grabber.isOpened()){
                    grabber.close();
                }
                graph.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
