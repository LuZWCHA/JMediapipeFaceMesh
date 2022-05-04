#pragma once

#include <jni.h>
#include <string>


namespace JniUtil {
	char* jStringToChar(JNIEnv* env, jstring jstr);

	jstring charToJString(JNIEnv* env, const char* pat);

	jbyteArray ucharToJByteArray(JNIEnv* env, unsigned char* buf, int len);

	unsigned char* jByteArrayToUChar(JNIEnv* env, jbyteArray array);

	char* jByteArrayToChar(JNIEnv* env, jbyteArray buf);
}








