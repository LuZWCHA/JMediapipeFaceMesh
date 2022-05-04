#pragma once

#include <iostream>
#include <vector>

#include "windows.h"
#include "dshow.h"

#include <opencv2/core/core.hpp> 
#include<opencv2/highgui/highgui.hpp> 
#include <opencv2/opencv.hpp>
#include<opencv2/objdetect/objdetect.hpp>

#pragma comment(lib, "strmiids.lib")
#pragma comment(lib, "ole32.lib")
#pragma comment(lib, "oleAut32.lib")

namespace cvcap {

	class FrameGrabber {

	public:

		FrameGrabber(){
			opened = false;
		}

		virtual ~FrameGrabber(){
			if(cap) close();
			cap = nullptr;
		}

		int listDevices(std::vector<std::string>& list);
		bool open(int idx);
		int readFrame(cv::Mat& frame);
		bool close();
		bool set(int k, double v);
		double get(int k);
		inline bool isOpened() { return opened; };

	private:
		cv::VideoCapture* cap = nullptr;
		bool opened;

	};
}

