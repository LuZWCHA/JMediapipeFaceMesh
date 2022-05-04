#pragma once
#include "../callback.h"

constexpr auto FACE_MESH_LANDMARK_NUM = 468 + 10;
constexpr auto DIMEMSION = 3;

class JNI_LandmarkCallback : public mediapipe::mycallback::LandmarkCallbcak {

private:
	jobject obj;
	jmethodID jmid;

	JNIEnv* env;

public:

	JNI_LandmarkCallback(JNIEnv* env, jobject jobj) {
		jobj = env->NewGlobalRef(jobj);

		this->obj = jobj;
		this->env = env;

		jclass clz = env->GetObjectClass(jobj);
		if (!clz)
		{
			LOGD("get jclass wrong");
			return;
		}
		jmethodID jmid = env->GetMethodID(clz, "landmark", "(I[FI)V");
		if (!jmid)
		{
			LOGD("get jmethodID wrong");
			return;
		}
		this->jmid = jmid;

	}

	~JNI_LandmarkCallback() {
		this->env->DeleteGlobalRef(this->obj);
		LOGD("landmark callback released");
	}

	void landmark(int image_index, std::vector<MeshInfo>& infos, int count) {

		if (env && jmid) {
			const int _size = count * FACE_MESH_LANDMARK_NUM * DIMEMSION;

			jfloatArray jfa = env->NewFloatArray(_size);
			float* landmark_flat = new float[_size];

			if (!jfa) {
				return;
			}

			int i = 0;
			for (MeshInfo mi : infos) {

				std::vector<Point> points = mi.meshlandmarks;

				for (Point p : points) {
					landmark_flat[i++] = p.x;
					landmark_flat[i++] = p.y;
					landmark_flat[i++] = p.z;
				}

			}

			env->SetFloatArrayRegion(jfa, 0, _size, landmark_flat);
			env->CallVoidMethod(obj, jmid, image_index, jfa, count);

			delete[] landmark_flat;
		}
	}

};