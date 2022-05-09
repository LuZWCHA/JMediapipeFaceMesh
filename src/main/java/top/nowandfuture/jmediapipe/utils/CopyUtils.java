package top.nowandfuture.jmediapipe.utils;

import top.nowandfuture.jmediapipe.ModelFiles;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;


public class CopyUtils {

    public static void copySourcesToParent() throws IOException, URISyntaxException {
        URL url = CopyUtils.class.getProtectionDomain().getCodeSource().getLocation();
        String parentDir = Paths.get(url.toURI()).getParent().toString();
        copySourcesTo(parentDir);
    }

    public static void copySourcesTo(String path) throws IOException {

        final String[] files = new String []{
                ModelFiles.PD_TXT,
                ModelFiles.FACE_DETECT,
                ModelFiles.FACE_LANDMARK,
        };

        for(String file: files){
            try (InputStream is = CopyUtils.class.getResourceAsStream(file)) {
                if (is != null) {
                    Path relPath = Paths.get(file);
                    Path fileName = relPath.getFileName();
                    Path relParentPath = relPath.getParent();
                    Path target = Paths.get(path, relParentPath.toString());
                    Path targetFile = Paths.get(target.toString(), fileName.toString());
                    if(!target.toFile().exists()){
                        Files.createDirectories(target);
                        Files.createFile(targetFile);
                    }
                    Files.copy(is, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }
}

