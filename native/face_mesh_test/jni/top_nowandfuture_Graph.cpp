#include "top_nowandfuture_Graph.h"

/*
 * Class:     top_nowandfuture_Graph
 * Method:    createGraph
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_top_nowandfuture_Graph_createGraph
(JNIEnv* env, jobject thisObj) {
	return (jlong) new mediapipe::desk::Graph();
}

/*
 * Class:     top_nowandfuture_Graph
 * Method:    init
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT jint JNICALL Java_top_nowandfuture_Graph_init
(JNIEnv* env, jobject thisObj, jlong rf, jstring name) {
	const char* path = env->GetStringUTFChars(name, 0);
	int ret = ((mediapipe::desk::Graph*)rf)->InitGraph(path);
	return ret;
}

/*
 * Class:     top_nowandfuture_Graph
 * Method:    detect
 * Signature: (J[BII)V
 */
JNIEXPORT jint JNICALL Java_top_nowandfuture_Graph_detect
(JNIEnv* env, jobject thisObj, jlong rf, jlong idx, jbyteArray buffer, jint width, jint height) {

	jbyte* ptr = env->GetByteArrayElements(buffer, 0);
	cv::Mat img(cv::Size(width, height), CV_8UC3, (unsigned char*)ptr);

	mediapipe::desk::Graph* graph = (mediapipe::desk::Graph*)rf;
	int ret = 0;
	if (graph) {
		ret = graph->DetectFrame(idx, width, height, img.data);
	}
	env->ReleaseByteArrayElements(buffer, ptr, 0);

	return ret;
}

/*
 * Class:     top_nowandfuture_Graph
 * Method:    registerCallback
 * Signature: (Ltop/nowandfuture/LandmarkCallback;)V
 */
JNIEXPORT jint JNICALL Java_top_nowandfuture_Graph_registerCallback
(JNIEnv* env, jobject thisObj, jlong rf, jobject callback) {
	return ((mediapipe::desk::Graph*)rf)->RegisterCallback(new JNI_LandmarkCallback(env, callback));
}

/*
 * Class:     top_nowandfuture_Graph
 * Method:    release
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_top_nowandfuture_Graph_release
(JNIEnv* env, jobject thisObj, jlong rf) {
	mediapipe::desk::Graph* graph = ((mediapipe::desk::Graph*)rf);

	if (graph) {
		int ret = graph->Release();
		if (!ret) { 
			LOGD("graph released"); 
		}else { LOGD("released failed with error code: %d", ret); }
		
		delete graph;
		graph = nullptr;

		if (!ret) {
			jclass clazz = env->GetObjectClass(thisObj);
			jfieldID field_ref = env->GetFieldID(clazz, "reference", "J");
			env->SetLongField(thisObj, field_ref, 0);
			env->DeleteLocalRef(clazz);
		}
	}
	
}

