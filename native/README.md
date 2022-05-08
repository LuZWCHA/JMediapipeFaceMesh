# Mediapipe Facemesh

Make sure you can compile the ```helloworld``` example application successfully.

Then, you need to put the ```face_mesh_test``` folder into mediapipe C++ source folder ```\mediapipe\examples``` and replace the ```BUILD.bazel``` at the root of the mediapipe project (where the WORKSPACE file is) by the ```patches/BUILD.bazel```.

```mediapipe\modules\face_landmark```,  ```mediapipe\modules\face_detection```, ```mediapipe\graphs\face_mesh``` store the pbtxt files.
 
```mediapipe\calculators``` store the ```xxx_calculator.cc``` files.

At last, compile the Windows DLL library using the build.sh script at the root of the mediapipe project.
My compile env is Windows 11 with Bazel-5.0.
----
For more details, please follow the Mediapipe [Guide](https://google.github.io/mediapipe/getting_started/cpp.html):

