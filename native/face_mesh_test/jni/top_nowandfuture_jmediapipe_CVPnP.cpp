#include "top_nowandfuture_jmediapipe_CVPnP.h"

/*
 * Class:     top_nowandfuture_CVPnP
 * Method:    solvePnP
 * Signature: ([D[D[D[D[D[DI)V
 */
JNIEXPORT void JNICALL Java_top_nowandfuture_jmediapipe_CVPnP_solvePnP
(JNIEnv* env, jclass thisObj, jdoubleArray point3d, jdoubleArray point2d, jdoubleArray cameraMatrix, jdoubleArray distCoeffs, jdoubleArray rVec, jdoubleArray tVec, jboolean b, jint flag) {
	
	int len = env->GetArrayLength(point3d);
	jdouble* points = env->GetDoubleArrayElements(point3d, 0);
	std::vector<cv::Point3f> cvPoint3fs;
	for (int i = 0; i < len; i += 3) {
		cvPoint3fs.push_back(cv::Point3f(points[i], points[i + 1], points[i + 2]));
	}
	env->ReleaseDoubleArrayElements(point3d, points, 2);

	len = env->GetArrayLength(point2d);
	std::vector<cv::Point2f> cvPoint2fs;
	points = env->GetDoubleArrayElements(point2d, 0);
	for (int i = 0; i < len; i += 2) {
		cvPoint2fs.push_back(cv::Point2f(points[i], points[i + 1]));
	}
	env->ReleaseDoubleArrayElements(point2d, points, 2);

	points = env->GetDoubleArrayElements(cameraMatrix, 0);
	cv::Mat cameraMat = cv::Mat(3, 3, CV_64FC1);
	for (int i = 0; i < 3; i++) {
		for (int j = 0; j < 3; j++) {
			cameraMat.at<double>(i, j) = points[3 * i + j];
		}
	}
	env->ReleaseDoubleArrayElements(cameraMatrix, points, 2);

	len = env->GetArrayLength(distCoeffs);
	points = env->GetDoubleArrayElements(distCoeffs, 0);
	cv::Mat cvDistCoeffs(len, 1, CV_32FC1);
	for (int i = 0; i < len; i += 1) {
		cvDistCoeffs.at <float> (i) = points[i];
	}
	env->ReleaseDoubleArrayElements(distCoeffs, points,2);

	cv::Mat rvec(3, 1, CV_64FC1);
	cv::Mat tvec(3, 1, CV_64FC1);
	points = env->GetDoubleArrayElements(rVec, 0);
	for (int i = 0; i < 3; i++) {
		rvec.at<double>(i) = points[i];
	}
	env->ReleaseDoubleArrayElements(rVec, points, 0);

	points = env->GetDoubleArrayElements(tVec, 0);
	for (int i = 0; i < 3; i++) {
		tvec.at<double>(i) = points[i];
	}
	env->ReleaseDoubleArrayElements(tVec, points, 0);

	bool ret = pose::getPose(cvPoint3fs, cvPoint2fs, cameraMat, cvDistCoeffs, rvec, tvec, b, flag);

	if (ret) {
		points = env->GetDoubleArrayElements(rVec, 0);
		for (int i = 0; i < 3; i++) {
			points[i] = rvec.at<double>(i);
		}
		env->ReleaseDoubleArrayElements(rVec, points, 0);

		points = env->GetDoubleArrayElements(tVec, 0);
		for (int i = 0; i < 3; i++) {
			points[i] = tvec.at<double>(i);
		}
		env->ReleaseDoubleArrayElements(tVec, points, 0);
	}
	else {
		std::cout << "error" << std::endl;
	}
}
