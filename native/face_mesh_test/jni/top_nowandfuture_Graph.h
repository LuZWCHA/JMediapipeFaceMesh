/* DO NOT EDIT THIS FILE - it is machine generated */
#pragma once

#include <jni.h>
#include "util.h"
#include <iostream>
#include <string>
#include "../face_mesh.h"
#include "jni_callback.h"
#include "../log_utils.h"

/* Header for class top_nowandfuture_Graph */

#ifndef _Included_top_nowandfuture_Graph
#define _Included_top_nowandfuture_Graph
#ifdef __cplusplus
extern "C" {
#endif
	/*
	 * Class:     top_nowandfuture_Graph
	 * Method:    createGraph
	 * Signature: ()J
	 */
	JNIEXPORT jlong JNICALL Java_top_nowandfuture_Graph_createGraph
	(JNIEnv*, jobject);

	/*
	 * Class:     top_nowandfuture_Graph
	 * Method:    init
	 * Signature: (JLjava/lang/String;)V
	 */
	JNIEXPORT jint JNICALL Java_top_nowandfuture_Graph_init
	(JNIEnv*, jobject, jlong, jstring);

	/*
	 * Class:     top_nowandfuture_Graph
	 * Method:    detect
	 * Signature: (JJ[BII)V
	 */
	JNIEXPORT jint JNICALL Java_top_nowandfuture_Graph_detect
	(JNIEnv*, jobject, jlong, jlong, jbyteArray, jint, jint);

	/*
	 * Class:     top_nowandfuture_Graph
	 * Method:    registerCallback
	 * Signature: (JLtop/nowandfuture/LandmarkCallback;)V
	 */
	JNIEXPORT jint JNICALL Java_top_nowandfuture_Graph_registerCallback
	(JNIEnv*, jobject, jlong, jobject);

	/*
	 * Class:     top_nowandfuture_Graph
	 * Method:    release
	 * Signature: (J)V
	 */
	JNIEXPORT void JNICALL Java_top_nowandfuture_Graph_release
	(JNIEnv*, jobject, jlong);

#ifdef __cplusplus
}
#endif
#endif