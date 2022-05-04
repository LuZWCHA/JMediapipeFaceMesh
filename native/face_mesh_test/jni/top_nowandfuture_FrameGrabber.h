/* DO NOT EDIT THIS FILE - it is machine generated */
#pragma once

#include <jni.h>
#include "util.h"
#include "../windows_cv_capture/cv_capture.h"
#include "../log_utils.h"

/* Header for class top_nowandfuture_FrameGrabber */

#ifndef _Included_top_nowandfuture_FrameGrabber
#define _Included_top_nowandfuture_FrameGrabber
#ifdef __cplusplus
extern "C" {
#endif
	/*
	 * Class:     top_nowandfuture_FrameGrabber
	 * Method:    createGrabber
	 * Signature: ()J
	 */
	JNIEXPORT jlong JNICALL Java_top_nowandfuture_FrameGrabber_createGrabber
	(JNIEnv*, jobject);

	/*
	 * Class:     top_nowandfuture_FrameGrabber
	 * Method:    open
	 * Signature: (JI)Z
	 */
	JNIEXPORT jboolean JNICALL Java_top_nowandfuture_FrameGrabber_open
	(JNIEnv*, jobject, jlong, jint);

	/*
	 * Class:     top_nowandfuture_FrameGrabber
	 * Method:    close
	 * Signature: (J)Z
	 */
	JNIEXPORT jboolean JNICALL Java_top_nowandfuture_FrameGrabber_close
	(JNIEnv*, jobject, jlong);

	/*
	 * Class:     top_nowandfuture_FrameGrabber
	 * Method:    listDevices
	 * Signature: (J)Ljava/util/List;
	 */
	JNIEXPORT jobject JNICALL Java_top_nowandfuture_FrameGrabber_listDevices
	(JNIEnv*, jobject, jlong);

	/*
	 * Class:     top_nowandfuture_FrameGrabber
	 * Method:    readFrame
	 * Signature: (J[I)[B
	 */
	JNIEXPORT jbyteArray JNICALL Java_top_nowandfuture_FrameGrabber_readFrame
	(JNIEnv*, jobject, jlong, jintArray);

	/*
	 * Class:     top_nowandfuture_FrameGrabber
	 * Method:    isOpened
	 * Signature: (J)Z
	 */
	JNIEXPORT jboolean JNICALL Java_top_nowandfuture_FrameGrabber_isOpened
	(JNIEnv*, jobject, jlong);

	/*
	* Class:     top_nowandfuture_FrameGrabber
	* Method:    set
	* Signature: (JID)B
	*/
	JNIEXPORT jboolean JNICALL Java_top_nowandfuture_FrameGrabber_set
	(JNIEnv*, jobject, jlong, jint, jdouble);


	/*
	* Class:     top_nowandfuture_FrameGrabber
	* Method:    get
	* Signature: (JI)D
	*/
	JNIEXPORT jdouble JNICALL Java_top_nowandfuture_FrameGrabber_get
	(JNIEnv*, jobject, jlong, jint);
#ifdef __cplusplus
}
#endif
#endif
