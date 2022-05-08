#pragma once

#include <cstdlib>
#include "absl/flags/flag.h"
#include "absl/flags/parse.h"
#include "mediapipe/framework/calculator_framework.h"
#include "mediapipe/framework/formats/image_frame.h"
#include "mediapipe/framework/formats/image_frame_opencv.h"
#include "mediapipe/framework/port/file_helpers.h"
#include "mediapipe/framework/port/opencv_highgui_inc.h"
#include "mediapipe/framework/port/opencv_imgproc_inc.h"
#include "mediapipe/framework/port/opencv_video_inc.h"
#include "mediapipe/framework/port/parse_text_proto.h"
#include "mediapipe/framework/port/status.h"

#include "mediapipe/framework/formats/detection.pb.h"
#include "mediapipe/framework/formats/landmark.pb.h"
#include "mediapipe/framework/formats/rect.pb.h"

#include "face_mesh_data.h"
#include "callback.h"
#include "log_utils.h"


namespace mediapipe {
    namespace desk {
        typedef void(*MediaPipeCallBack)(int image_index, std::vector<MeshInfo>& infos, int count);

        class Graph
        {
        public:
            Graph();
            virtual ~Graph();
            Graph(const Graph&) = delete;
            Graph& operator=(const Graph&) = delete;

        public:
            int InitGraph(const char* model_path);
            int RegisterCallback(std::shared_ptr<mediapipe::mycallback::LandmarkCallbcak> callback);
            int DetectFrame(int image_index, int image_width, int image_height, std::shared_ptr<uchar> image_data);
            int DetectVideo(const char* video_path, int show_image);
            int Release();

        private:
            absl::Status Mediapipe_InitGraph(const char* model_path);
            absl::Status Mediapipe_RunMPPGraph(int image_index, int image_width, int image_height, std::shared_ptr<uchar> image_data);
            absl::Status Mediapipe_RunMPPGraph(const char* video_path, int show_image);
            absl::Status Mediapipe_ReleaseGraph();

        private:
            bool m_bIsInit;
            bool m_bIsRelease;

            const char* m_kInputStream;
            const char* m_kOutputStream;
            const char* m_kWindowName;
            const char* m_kOutputLandmarks;

            std::shared_ptr <mediapipe::mycallback::LandmarkCallbcak> m_LandmarksCallBack;

            mediapipe::CalculatorGraph m_Graph;

            std::unique_ptr<mediapipe::OutputStreamPoller> m_pPoller;
            std::unique_ptr<mediapipe::OutputStreamPoller> m_pPoller_landmarks;

        };
}
}
