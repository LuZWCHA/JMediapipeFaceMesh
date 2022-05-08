# JMediapipeFaceMesh

It is a java wrapper of the mediapipe face landmark model.

The wrapper provide the OpenCV CameraCapture wrapper and the mediapipe facemesh graph wrapper to get the face's 478 landmarks (468 for face and the others are iris landmarks).


## Examples
The examples are under Main.java and FMLandmarkCallback.java

``` java
    //load the libraries before Graph and CV-Clazz initialized.
    try {
        Loader.loadLibs();
        //copy the model files into the parent dir.
        Loader.copySources();
    } catch (IOException | URISyntaxException e) {
        e.printStackTrace();
    }
```

## Download
Download the JMediapipe.jar at [Release](https://github.com/LuZWCHA/JMediapipeFaceMesh/releases).
(Note: the .tflite files will be generated under the parent folder, the structure looks like: ```./mediapipe/**/*.tflite``` and ```./mediapipe/**/*.pbtxt```)
