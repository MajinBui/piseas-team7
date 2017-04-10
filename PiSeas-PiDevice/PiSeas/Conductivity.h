#ifndef _CONDUCTIVITY_
#define _CONDUCTIVITY_
#include "DataRange.h"

float median(float* array, int length);

class Conductivity {
	DataRange conductivitySettings;

public:
	Conductivity();
	Conductivity(float, float, bool);
	void setConductivityData(float, float, bool);
	float getMin();
	float getMax();
	bool getAutoRegulate();
	static float getConductivity();
};
#endif
