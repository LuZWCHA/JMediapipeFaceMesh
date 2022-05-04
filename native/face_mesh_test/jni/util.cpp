#pragma once

#include "util.h"

char* JniUtil::jStringToChar(JNIEnv* env, jstring jstr) {
    if (env == NULL || jstr == NULL) {
        return nullptr;
    }
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("utf-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray)env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
    char* result = nullptr;
    if (alen > 0) {
        memcpy(result, ba, alen);
        result[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    return result;
}

jstring JniUtil::charToJString(JNIEnv* env, const char* pat) {
    jclass strClass = (env)->FindClass("java/lang/String");
    jmethodID ctorID = (env)->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = (env)->NewByteArray(strlen(pat));
    (env)->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*)pat);
    jstring encoding = (env)->NewStringUTF("utf-8");
    return (jstring)(env)->NewObject(strClass, ctorID, bytes, encoding);
}

jbyteArray JniUtil::ucharToJByteArray(JNIEnv* env, unsigned char* buf, int len) {
    jbyteArray array = env->NewByteArray(len);
    env->SetByteArrayRegion(array, 0, len, reinterpret_cast<jbyte*>(buf));
    return array;
}

unsigned char* JniUtil::jByteArrayToUChar(JNIEnv* env, jbyteArray array) {
    int len = env->GetArrayLength(array);
    unsigned char* buf = new unsigned char[len];
    env->GetByteArrayRegion(array, 0, len, reinterpret_cast<jbyte*>(buf));
    return buf;
}

char* JniUtil::jByteArrayToChar(JNIEnv* env, jbyteArray buf) {
    char* chars = nullptr;
    jbyte* bytes;
    bytes = env->GetByteArrayElements(buf, 0);
    int chars_len = env->GetArrayLength(buf);
    chars = new char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;
    env->ReleaseByteArrayElements(buf, bytes, 0);
    return chars;
}



