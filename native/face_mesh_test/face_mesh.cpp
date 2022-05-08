#include <vector>

#include "face_mesh.h"
#include "callback.h"


int converToInt(absl::lts_20210324::StatusCode code) {
    switch (code)
    {
    case absl::lts_20210324::StatusCode::kOk:
        return 0;
    case absl::lts_20210324::StatusCode::kCancelled:
        return 1;
    case absl::lts_20210324::StatusCode::kUnknown:
        return 2;
    case absl::lts_20210324::StatusCode::kInvalidArgument:
        return 3;
    case absl::lts_20210324::StatusCode::kDeadlineExceeded:
        return 4;
    case absl::lts_20210324::StatusCode::kNotFound:
        return 5;
    case absl::lts_20210324::StatusCode::kAlreadyExists:
        return 6;
    case absl::lts_20210324::StatusCode::kPermissionDenied:
        return 7;
    case absl::lts_20210324::StatusCode::kUnauthenticated:
        return 8;
    case absl::lts_20210324::StatusCode::kResourceExhausted:
        return 9;
    case absl::lts_20210324::StatusCode::kFailedPrecondition:
        return 10;
    case absl::lts_20210324::StatusCode::kAborted:
        return 11;
    case absl::lts_20210324::StatusCode::kOutOfRange:
        return 12;
    case absl::lts_20210324::StatusCode::kUnimplemented:
        return 13;
    case absl::lts_20210324::StatusCode::kInternal:
        return 14;
    case absl::lts_20210324::StatusCode::kUnavailable:
        return 15;
    case absl::lts_20210324::StatusCode::kDataLoss:
        return 16;
    default:
        return 2;
    }
}


mediapipe::desk::Graph::Graph()
{
    m_bIsInit = false;
    m_bIsRelease = false;
    m_kInputStream = "input_video";
    m_kOutputStream = "output_video";
    m_kWindowName = "MediaPipe";
    m_kOutputLandmarks = "multi_face_landmarks";
    m_LandmarksCallBack = nullptr;
}

mediapipe::desk::Graph::~Graph()
{
    if (!m_bIsRelease)
    {
        Release();
    }
}

int mediapipe::desk::Graph::InitGraph(const char* model_path)
{
    absl::Status run_status = Mediapipe_InitGraph(model_path);
    if (!run_status.ok())
    {
        LOGD(run_status.ToString().c_str());
        return converToInt(run_status.code());
    }
    m_bIsInit = true;
    return  0;
}


int  mediapipe::desk::Graph::RegisterCallback(std::shared_ptr<mediapipe::mycallback::LandmarkCallbcak> callback)
{
    if ( callback )
    {
        m_LandmarksCallBack = callback;
        return 0;
    }

    return 1;
}


int mediapipe::desk::Graph::DetectFrame(int image_index, int image_width, int image_height, std::shared_ptr<uchar> image_data)
{
    if (!m_bIsInit)
        return 17;

    absl::Status run_status = Mediapipe_RunMPPGraph(image_index, image_width, image_height, image_data);

    if (!run_status.ok()) {
        LOGD(run_status.ToString().c_str());
        return converToInt(run_status.code());
    }

    return 0;
}


int mediapipe::desk::Graph::DetectVideo(const char* video_path, int show_image)
{
    if (!m_bIsInit || m_bIsRelease)
        return 17;

    absl::Status run_status = Mediapipe_RunMPPGraph(video_path, show_image);
    if (!run_status.ok()) {
        return converToInt(run_status.code());
    }
    return 0;
}


int mediapipe::desk::Graph::Release()
{
    absl::Status run_status = Mediapipe_ReleaseGraph();

    if (!run_status.ok()) {
        LOGD(run_status.ToString().c_str());
        return converToInt(run_status.code());
    }

    m_bIsRelease = true;
    return 0;
}


absl::Status mediapipe::desk::Graph::Mediapipe_InitGraph(const char* model_path)
{
    std::string calculator_graph_config_contents;
    MP_RETURN_IF_ERROR(mediapipe::file::GetContents(model_path, &calculator_graph_config_contents));
    mediapipe::CalculatorGraphConfig config =
        mediapipe::ParseTextProtoOrDie<mediapipe::CalculatorGraphConfig>(
            calculator_graph_config_contents);
    MP_RETURN_IF_ERROR(m_Graph.Initialize(config));

    auto sop = m_Graph.AddOutputStreamPoller(m_kOutputStream);

    assert(sop.ok());
    m_pPoller = std::make_unique<mediapipe::OutputStreamPoller>(std::move(sop.value()));

    mediapipe::StatusOrPoller sop_landmark = m_Graph.AddOutputStreamPoller(m_kOutputLandmarks);
    assert(sop_landmark.ok());
    m_pPoller_landmarks = std::make_unique<mediapipe::OutputStreamPoller>(std::move(sop_landmark.value()));

    MP_RETURN_IF_ERROR(m_Graph.StartRun({}));
    return absl::OkStatus();
}

absl::Status mediapipe::desk::Graph::Mediapipe_RunMPPGraph(int image_index, int image_width, int image_height, std::shared_ptr<uchar> image_data)
{
    cv::Mat raw_camera_frame(image_height, image_width, CV_8UC3, image_data.get());
    cv::Mat camera_frame;
    cv::cvtColor(raw_camera_frame, camera_frame, cv::COLOR_BGR2RGB);
    cv::flip(camera_frame, camera_frame, /*flipcode=HORIZONTAL*/ 1);

    //cv::namedWindow("show");
    //cv::imshow("show", camera_frame);

    // Wrap Mat into an ImageFrame.
    auto input_frame = absl::make_unique<mediapipe::ImageFrame>(
        mediapipe::ImageFormat::SRGB, camera_frame.cols, camera_frame.rows,
        mediapipe::ImageFrame::kDefaultAlignmentBoundary);
    cv::Mat input_frame_mat = mediapipe::formats::MatView(input_frame.get());
    camera_frame.copyTo(input_frame_mat);

     //Send image packet into the graph.
    size_t frame_timestamp_us = (double)cv::getTickCount() / (double)cv::getTickFrequency() * 1e6;
    MP_RETURN_IF_ERROR(m_Graph.AddPacketToInputStream(
        m_kInputStream, mediapipe::Adopt(input_frame.release())
        .At(mediapipe::Timestamp(frame_timestamp_us))));


    // Get the graph result packet, or stop if that fails.
    mediapipe::Packet packet;
    mediapipe::Packet packet_landmarks;
    if (!m_pPoller->Next(&packet))
        return absl::OkStatus();

    if (m_pPoller_landmarks->QueueSize() > 0)
    {
        if (m_pPoller_landmarks->Next(&packet_landmarks))
        {
            std::vector<mediapipe::NormalizedLandmarkList> output_landmarks = packet_landmarks.Get<std::vector<mediapipe::NormalizedLandmarkList>>();

            std::vector<MeshInfo> face_mesh_landmarks;
            face_mesh_landmarks.clear();

            std::vector<MeshInfo> mesh_info;
            std::vector<cv::Point3f> points;
            for (int m = 0; m < output_landmarks.size(); ++m)
            {
                mediapipe::NormalizedLandmarkList single_face_NormalizedLandmarkList = output_landmarks[m];
                std::vector<Point> face_mesh_landmarks;

                for (int i = 0; i < single_face_NormalizedLandmarkList.landmark_size(); ++i)
                {
                    
                    Point p;
                    const mediapipe::NormalizedLandmark landmark = single_face_NormalizedLandmarkList.landmark(i);


                    p.x = landmark.x() * camera_frame.cols;
                    p.y = landmark.y() * camera_frame.rows;

                    p.z = landmark.z();

                    face_mesh_landmarks.push_back(p);
                }

                MeshInfo mi;
                mi.meshlandmarks = face_mesh_landmarks;
                mesh_info.push_back(mi);
            }

            if (m_LandmarksCallBack)
            {
                m_LandmarksCallBack->landmark(image_index, mesh_info, output_landmarks.size());
            }

        }
    }
    return absl::OkStatus();
}


absl::Status mediapipe::desk::Graph::Mediapipe_RunMPPGraph(const char* video_path, int show_image)
{
    cv::VideoCapture capture;
    capture.open(video_path);

    RET_CHECK(capture.isOpened());

    LOGD("capture.isOpened()");

    if (show_image) {
        cv::namedWindow(m_kWindowName, /*flags=WINDOW_AUTOSIZE*/ 1);
    }
#if (CV_MAJOR_VERSION >= 3) && (CV_MINOR_VERSION >= 2)
    capture.set(cv::CAP_PROP_FRAME_WIDTH, 640);
    capture.set(cv::CAP_PROP_FRAME_HEIGHT, 480);
    capture.set(cv::CAP_PROP_FPS, 30);
#endif

    bool grab_frames = true;
    int image_index = 0;
    while (grab_frames) {
        // Capture opencv camera or video frame.
        cv::Mat camera_frame_raw;
        capture >> camera_frame_raw;
        if (camera_frame_raw.empty())
            break;

        cv::Mat camera_frame;
        cv::cvtColor(camera_frame_raw, camera_frame, cv::COLOR_BGR2RGB);
        cv::flip(camera_frame, camera_frame, /*flipcode=HORIZONTAL*/ 1);

        // Wrap Mat into an ImageFrame.
        auto input_frame = absl::make_unique<mediapipe::ImageFrame>(
            mediapipe::ImageFormat::SRGB, camera_frame.cols, camera_frame.rows,
            mediapipe::ImageFrame::kDefaultAlignmentBoundary);
        cv::Mat input_frame_mat = mediapipe::formats::MatView(input_frame.get());
        camera_frame.copyTo(input_frame_mat);

        // Send image packet into the graph.
        size_t frame_timestamp_us =
            (double)cv::getTickCount() / (double)cv::getTickFrequency() * 1e6;

        MP_RETURN_IF_ERROR(m_Graph.AddPacketToInputStream(
            m_kInputStream, mediapipe::Adopt(input_frame.release())
            .At(mediapipe::Timestamp(frame_timestamp_us))));

        // Get the graph result packet, or stop if that fails.
        mediapipe::Packet packet;
        mediapipe::Packet packet_landmarks;
        if (!m_pPoller->Next(&packet)) break;
        if (m_pPoller_landmarks->QueueSize() > 0) {
            if (m_pPoller_landmarks->Next(&packet_landmarks))
            {
                std::vector<mediapipe::NormalizedLandmarkList> output_landmarks = packet_landmarks.Get<std::vector<mediapipe::NormalizedLandmarkList>>();

                std::vector<MeshInfo> face_mesh_landmarks;
                face_mesh_landmarks.clear();

                std::vector<MeshInfo> mesh_info;

                for (int m = 0; m < output_landmarks.size(); ++m)
                {
                    mediapipe::NormalizedLandmarkList single_face_NormalizedLandmarkList = output_landmarks[m];
                    std::vector<Point> face_mesh_landmarks;

                    for (int i = 0; i < single_face_NormalizedLandmarkList.landmark_size(); ++i)
                    {
                        Point p;
                        const mediapipe::NormalizedLandmark landmark = single_face_NormalizedLandmarkList.landmark(i);
                        p.x = landmark.x() * camera_frame.cols;
                        p.y = landmark.y() * camera_frame.rows;
                        face_mesh_landmarks.push_back(p);
                    }

                    MeshInfo mi;
                    mi.meshlandmarks = face_mesh_landmarks;
                    mesh_info.push_back(mi);
                }

                if (m_LandmarksCallBack)
                {
                    m_LandmarksCallBack->landmark(image_index, mesh_info, output_landmarks.size());
                }

            }
        }
        if (show_image)
        {
            auto& output_frame = packet.Get<mediapipe::ImageFrame>();
            // Convert back to opencv for display or saving.
            cv::Mat output_frame_mat = mediapipe::formats::MatView(&output_frame);
            cv::cvtColor(output_frame_mat, output_frame_mat, cv::COLOR_RGB2BGR);
            cv::Mat dst;
            cv::imshow(m_kWindowName, dst);
            cv::waitKey(1);
        }

        image_index += 1;
    }
    if (show_image)
        capture.release();
    cv::destroyWindow(m_kWindowName);
    return absl::OkStatus();
}


absl::Status mediapipe::desk::Graph::Mediapipe_ReleaseGraph()
{
    MP_RETURN_IF_ERROR(m_Graph.CloseInputStream(m_kInputStream));
    return m_Graph.WaitUntilDone();
}
