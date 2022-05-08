#ifndef HAND_TEST_H
#define HAND_TEST_H

#include <iostream> 
#include <opencv2/core/core.hpp> 
#include<opencv2/highgui/highgui.hpp> 
#include <opencv2/opencv.hpp>
#include<opencv2/objdetect/objdetect.hpp>

#include "face_mesh.h"
#include "windows_cv_capture/cv_capture.h"

using namespace cv;
using namespace std;

// some codes are copied from https://www.stubbornhuang.com/

typedef void(*LandmarksCallBack)(int image_index, vector<MeshInfo>& infos, int count);

std::vector<MeshInfo> mesh_info;

class TestCallback : public mediapipe::mycallback::LandmarkCallbcak {

public:
    void landmark(int image_index, std::vector<MeshInfo>& infos, int count) {

        mesh_info = infos;
    }
};

int main()
{
    mediapipe::desk::Graph graph;


    /* 初始化Mediapipe Hand Tracking */
    string mediapipe_face_mesh_model_path = "D:\\Projects\\C++\\mediapipe\\mediapipe\\mediapipe\\graphs\\face_mesh\\face_mesh_desktop_live.pbtxt";
    std::cout << mediapipe_face_mesh_model_path << std::endl;

    if (!graph.InitGraph(mediapipe_face_mesh_model_path.c_str(), "D:\\Projects\\C++\\mediapipe\\mediapipe"))
    {
        cout << "初始化模型成功" << endl;
    }
    else
    {
        cout << "初始化模型失败" << endl;
    }

    // 注册回调函数
    if (!graph.RegisterCallback(make_shared<TestCallback>()))
    {
        cout << "注册坐标回调函数成功" << endl;
    }
    else
    {
        cout << "注册坐标回调函数失败" << endl;
    }

    //打开第一个摄像头
    cvcap::FrameGrabber grabber;

    vector<string> devices;

    int count = grabber.listDevices(devices);

    if (count <= 0) {
        cout << "failed to find device." << endl;
        return -1;
    }

    for (string name : devices) {
        cout << name << endl;
    }

    //判断摄像头是否打开
    if (!grabber.open(0))
    {
        cout << "摄像头未成功打开" << endl;
    }

    //创建窗口
    namedWindow("打开摄像头", 1);
    int image_index = 0;
    while (1)
    {
        Mat frame;
        bool res = grabber.readFrame(frame);

        int image_width = frame.cols;
        int image_height = frame.rows;

        uchar* copy_data = new uchar[image_height * image_width * 3];
        std::memcpy(copy_data, frame.data, image_height * image_width * 3);

        if (res < 0) {
            break;
        }

        auto frameData = shared_ptr<uchar>(copy_data);

        int ret = graph.DetectFrame(image_index, frame.cols, frame.rows, frameData);

        if (ret == 0)
        {
            //std::cout << "Mediapipe_Hand_Tracking_Detect_Frame执行成功！" << std::endl;
        }
        else
        {
            std::cout << "Mediapipe_Hand_Tracking_Detect_Frame执行失败！ 错误码：" << ret << std::endl;
        }

        for (MeshInfo info : mesh_info) {
            for (auto landmark : info.meshlandmarks) {
                float x = landmark.x;
                float y = landmark.y;
                
                cv::circle(frame,cv::Point(frame.cols - x, y), 2, cv::Scalar(255, 0, 0));
            }
        }

        //显示摄像头读取到的图像
        imshow("打开摄像头", frame);


        //等待1毫秒，如果按键则退出循环
        if (waitKey(1) >= 0)
        {
            break;
        }

        image_index += 1;
    }

    if (!graph.Release())
    {
        std::cout << "Mediapipe释放成功！" << std::endl;
    }
    else
    {
        std::cout << "Mediapipe释放失败！" << std::endl;
    }


    grabber.close();
    cv::destroyAllWindows();

    getchar();

    return 0;
}

#endif