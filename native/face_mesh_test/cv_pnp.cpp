#pragma once

#include "cv_pnp.h"

bool pose::getPose(
	std::vector<cv::Point3f> objectPoints, std::vector<cv::Point2f> imagePoints,
	cv::Mat cameraMatrix, std::vector<float> distCoeffs,
	cv::Mat& rVec, cv::Mat& tVec,
	bool useExtrinsicGuess, int flags) {

	bool ret = cv::solvePnP(objectPoints, imagePoints, cameraMatrix, distCoeffs, rVec , tVec, useExtrinsicGuess, cv::SOLVEPNP_ITERATIVE);

	//cv::Mat rmat(3, 3, CV_64F), mtxR(3, 3, CV_64F), mtxQ(3, 3, CV_64F), Qx(3, 3, CV_64F), Qy(3, 3, CV_64F), Qz(3, 3, CV_64F);
	//cv::Rodrigues(rVec, rmat);
	//cv::Vec3d angles = cv::RQDecomp3x3(rmat, mtxR, mtxQ, Qx, Qy, Qz);
	return ret;

}