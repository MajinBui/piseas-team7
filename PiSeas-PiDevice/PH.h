#ifndef _PH_
#define _PH_
#include "DataRange.h"

class PH {
	DataRange phSettings;
public:
	PH() {}
	PH(float, float, bool);
	float getMin();
	float getMax();
	bool getAutoRegulate();
	static float getPH();
	static void regulate(PH, bool);
};
#endif