# Mediapipe Facemesh

Make sure you can compile the ```helloworld``` example application successfully.

Then, you need to put the ```face_mesh_test``` folder into mediapipe C++ source folder ```\mediapipe\examples``` and replace the ```BUILD.bazel``` at the root of the mediapipe project (where the WORKSPACE file is) by the ```patches/BUILD.bazel```.

At last, compile the Windows DLL library using the build.sh script at the root of the mediapipe project.
My compile env is Windows 11 with Bazel-5.0.
