#ifndef _PH_
#define _PH_
#include "DataRange.h"

float median(float* array, int length);

class PH {
	DataRange pHSettings;

public:
	PH();
	PH(float, float, bool);
	void setPH(float, float, bool);
	float getMin();
	float getMax();
	bool getAutoRegulate();
	static float getPH();
};
#endif
