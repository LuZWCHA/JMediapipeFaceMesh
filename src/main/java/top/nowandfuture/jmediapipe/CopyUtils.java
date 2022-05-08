package top.nowandfuture.jmediapipe.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;


public class CopyUtils {

    public static void copySourcesToParent() throws IOException, URISyntaxException {
        URL url = CopyUtils.class.getProtectionDomain().getCodeSource().getLocation();
        String parentDir = Paths.get(url.toURI()).getParent().toString();

        String[] files = new String []{
                "/mediapipe/graphs/face_mesh_desktop_live.pbtxt",
                "/mediapipe/modules/face_detection/face_detection_short_range.tflite",
                "/mediapipe/modules/face_landmark/face_landmark_with_attention.tflite",
        };

        for(String file: files){
            try (InputStream is = CopyUtils.class.getResourceAsStream(file)) {
                if (is != null) {
                    Path relPath = Paths.get(file);
                    Path fileName = relPath.getFileName();
                    Path relParentPath = relPath.getParent();
                    Path target = Paths.get(parentDir, relParentPath.toString());
                    Path targetFile = Paths.get(target.toString(), fileName.toString());
                    if(!target.toFile().exists()){
                        Files.createDirectories(target);
                        Files.createFile(targetFile);
                    }
                    Files.copy(is, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }


    }
}

