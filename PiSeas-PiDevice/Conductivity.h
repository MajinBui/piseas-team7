#ifndef _CONDUCTIVITY_
#define _CONDICTIVITY_
#include "DataRange.h"

class Conductivity {
	DataRange condSettings;
public:
	Conductivity() {}
	Conductivity(float, float, bool);
	float getMin();
	float getMax();
	bool getAutoRegulate();
	void setMin(float);
	void setMax(float);
	void setAutoRegulate(bool);
	static float getConductivity();
	static void regulate(Conductivity, bool);
};
#endif
