# Copyright 2019 The MediaPipe Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

licenses(["notice"])

package(default_visibility = ["//mediapipe/examples:__subpackages__"])

cc_binary(
    name = "face_mesh_tflite",
    deps = [
        "//mediapipe/examples/desktop:simple_run_graph_main",
        "//mediapipe/graphs/face_mesh:desktop_calculators",
    ],
)

cc_binary(
    name = "face_mesh_cpu_desk",

    visibility = ["//visibility:public"],

    srcs = [
        "@bazel_tools//tools/jdk:current_java_runtime",
        "test.cpp", "face_mesh.h","face_mesh.cpp","face_mesh_data.h", "callback.h", 
        "windows_cv_capture/cv_capture.h", "windows_cv_capture/cv_capture.cpp", 
        "jni/jni_callback.h","jni/top_nowandfuture_FrameGrabber.h", "jni/top_nowandfuture_FrameGrabber.cpp", 
        "jni/top_nowandfuture_Graph.h", "jni/top_nowandfuture_Graph.cpp", "jni/util.h", "jni/util.cpp", "log_utils.h"
    ],

    linkshared=True,


    deps = [
        "//mediapipe/graphs/face_mesh:desktop_live_calculators",
        "//:jni_headers",
    ],

)

# Linux only
cc_binary(
    name = "face_mesh_gpu",
    deps = [
        "//mediapipe/examples/desktop:demo_run_graph_main_gpu",
        "//mediapipe/graphs/face_mesh:desktop_live_gpu_calculators",
    ],
)
