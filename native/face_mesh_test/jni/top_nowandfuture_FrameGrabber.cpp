#include "top_nowandfuture_FrameGrabber.h"

JNIEXPORT jlong JNICALL Java_top_nowandfuture_FrameGrabber_createGrabber
(JNIEnv*, jobject) {
	return (jlong) new cvcap::FrameGrabber();
}

/*
 * Class:     top_nowandfuture_FrameGrabber
 * Method:    open
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_top_nowandfuture_FrameGrabber_open
(JNIEnv * env, jobject thisObj, jlong rf, jint idx){
	return ((cvcap::FrameGrabber*)rf)->open(idx) ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     top_nowandfuture_FrameGrabber
 * Method:    close
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_top_nowandfuture_FrameGrabber_close
(JNIEnv* env, jobject thisObj, jlong rf) {

	cvcap::FrameGrabber* grabber = reinterpret_cast<cvcap::FrameGrabber*>(rf);

	bool ret = false;
	if (grabber) {
		ret = grabber->close();
		delete grabber;
		grabber = nullptr;

		jclass clazz = env->GetObjectClass(thisObj);
		jfieldID field_ref = env->GetFieldID(clazz, "reference", "J");
		env->SetLongField(thisObj, field_ref, 0);
		env->DeleteLocalRef(clazz);

		LOGD("grabber released");
	}

	return ret ? JNI_TRUE: JNI_FALSE;
}

/*
 * Class:     top_nowandfuture_FrameGrabber
 * Method:    listDevices
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_top_nowandfuture_FrameGrabber_listDevices
(JNIEnv* env, jobject thisObj, jlong rf) {
	std::vector<std::string> devices;
	int count = reinterpret_cast<cvcap::FrameGrabber*>(rf)->listDevices(devices);

	if (count >= 1) {
		jobjectArray ret = (jobjectArray)env->NewObjectArray(devices.size(), env->FindClass("java/lang/String"), env->NewStringUTF(""));

		
		for (int i = 0; i < devices.size(); i++) {
			std::string device = devices[i];
			env->SetObjectArrayElement(ret, i, env->NewStringUTF(device.c_str()));
		}

		return ret;
	}

	return NULL;
}

jbyteArray matToByteArray(JNIEnv* env, const cv::Mat& image) {
	int size_ = image.elemSize();
	jbyteArray resultImage = env->NewByteArray(image.total() * size_);
	auto _data = std::make_unique<jbyte[]>(image.total() * size_);
	for (int i = 0; i < image.total() * size_; i++) {
		_data[i] = image.data[i];
	}
	env->SetByteArrayRegion(resultImage, 0, image.total() * size_, _data.get());

	return resultImage;
}

/*
 * Class:     top_nowandfuture_FrameGrabber
 * Method:    readFrame
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_top_nowandfuture_FrameGrabber_readFrame
(JNIEnv* env, jobject thisObj, jlong rf, jintArray size) {
	cv::Mat mat;
	reinterpret_cast<cvcap::FrameGrabber*>(rf)->readFrame(mat);
	
	int w = mat.rows;
	int h = mat.cols;

	auto sizeArray = std::make_unique<int[]>(2);
	sizeArray[0] = w; sizeArray[1] = h;
	env->SetIntArrayRegion(size, 0, 2, (jint*)sizeArray.get());
	jbyteArray ret = matToByteArray(env, mat);
	
	return ret;
}

/*
 * Class:     top_nowandfuture_FrameGrabber
 * Method:    isOpened
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_top_nowandfuture_FrameGrabber_isOpened
(JNIEnv* env, jobject thisObj, jlong rf) {
	return ((cvcap::FrameGrabber*)rf)->isOpened() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_top_nowandfuture_FrameGrabber_set
(JNIEnv* env, jobject thisObj, jlong rf, jint key, jdouble value) {
	cvcap::FrameGrabber* grabber = reinterpret_cast<cvcap::FrameGrabber*>(rf);

	if (grabber) {
		return grabber->set(key, value) ? JNI_TRUE : JNI_FALSE;
	}

	return JNI_FALSE;
}

JNIEXPORT jdouble JNICALL Java_top_nowandfuture_FrameGrabber_get
(JNIEnv* env, jobject thisObj, jlong rf, jint key) {
	cvcap::FrameGrabber* grabber =  reinterpret_cast<cvcap::FrameGrabber*>(rf);

	if (grabber) {
		return grabber->get(key);
	}

	return -1.0;
}