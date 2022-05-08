package top.nowandfuture.jmediapipe;

import top.nowandfuture.jmediapipe.utils.CopyUtils;
import top.nowandfuture.jmediapipe.utils.NativeUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Loader {

    public static void copySources() throws IOException, URISyntaxException {
        CopyUtils.copySourcesToParent();
    }

    public static void loadLibs() throws IOException {
        try{
            NativeUtils.loadLibraryFromJar("/libs/opencv_world3410.dll");
            NativeUtils.loadLibraryFromJar("/libs/face_mesh_cpu_desk.dll");
        }catch (IOException e){
            e.printStackTrace();
            String projectPath = new File("").getAbsolutePath();
            System.load(Paths.get(projectPath, "libs/opencv_world3410.dll").toString());
            System.load(Paths.get(projectPath, "libs/face_mesh_cpu_desk.dll").toString());
        }
    }

}
