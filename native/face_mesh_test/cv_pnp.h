#pragma once

#include <opencv2/core/core_c.h>
#include <opencv2/calib3d.hpp>
#include <vector>
#include <iostream>

namespace pose {

	bool getPose(
		std::vector<cv::Point3f> objectPoints, std::vector<cv::Point2f> imagePoints,
		cv::Mat cameraMatrix, std::vector<float> distCoeffs,
		cv::Mat& rVec, cv::Mat& tVec,
		bool useExtrinsicGuess = false, int flags = cv::SOLVEPNP_ITERATIVE);

}