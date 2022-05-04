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

// some codes if copy from https://www.stubbornhuang.com/

typedef void(*LandmarksCallBack)(int image_index, vector<MeshInfo>& infos, int count);


class TestCallback : public mediapipe::mycallback::LandmarkCallbcak {

public:
    void landmark(int image_index, std::vector<MeshInfo>& infos, int count) {
        cout << "callback" << endl;
    }

};

int main()
{
    mediapipe::desk::Graph graph;


    /* ��ʼ��Mediapipe Hand Tracking */
    string mediapipe_face_mesh_model_path = "D:\\Projects\\C++\\mediapipe\\mediapipe\\mediapipe\\graphs\\face_mesh\\face_mesh_desktop_live.pbtxt";
    std::cout << mediapipe_face_mesh_model_path << std::endl;

    if (!graph.InitGraph(mediapipe_face_mesh_model_path.c_str()))
    {
        cout << "��ʼ��ģ�ͳɹ�" << endl;
    }
    else
    {
        cout << "��ʼ��ģ��ʧ��" << endl;
    }

    // ע��ص�����
    if (!graph.RegisterCallback(new TestCallback()))
    {
        cout << "ע������ص������ɹ�" << endl;
    }
    else
    {
        cout << "ע������ص�����ʧ��" << endl;
    }

    //�򿪵�һ������ͷ
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

    //�ж�����ͷ�Ƿ��
    if (!grabber.open(0))
    {
        cout << "����ͷδ�ɹ���" << endl;
    }

    //��������
    namedWindow("������ͷ", 1);

    int image_index = 0;
    while (1)
    {
        Mat frame;

        bool res = grabber.readFrame(frame);

        Mat copyFrame;
        frame.copyTo(copyFrame);

        if (res < 0) {
            break;
        }

        int ret = graph.DetectFrame(image_index, frame.cols, frame.rows, copyFrame.data);

        if (ret == 0)
        {
            //std::cout << "Mediapipe_Hand_Tracking_Detect_Frameִ�гɹ���" << std::endl;
        }
        else
        {
            std::cout << "Mediapipe_Hand_Tracking_Detect_Frameִ��ʧ�ܣ� �����룺" << ret << std::endl;
        }

        //��ʾ����ͷ��ȡ����ͼ��
        imshow("������ͷ", frame);
        //�ȴ�1���룬����������˳�ѭ��
        if (waitKey(1) >= 0)
        {
            break;
        }

        image_index += 1;
    }

    if (!graph.Release())
    {
        std::cout << "Mediapipe�ͷųɹ���" << std::endl;
    }
    else
    {
        std::cout << "Mediapipe�ͷ�ʧ�ܣ�" << std::endl;
    }


    grabber.close();
    cv::destroyAllWindows();

    getchar();

    return 0;
}

#endif