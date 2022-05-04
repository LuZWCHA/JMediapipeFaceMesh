#pragma once

#include <vector>
#include "face_mesh_data.h"
#include "log_utils.h"

namespace mediapipe {
	namespace mycallback {

		class LandmarkCallbcak {
		public:
			LandmarkCallbcak() {}
			virtual ~LandmarkCallbcak() {};
			virtual void landmark(int image_index, std::vector<MeshInfo>& infos, int count) = 0;
		};
	}
}
