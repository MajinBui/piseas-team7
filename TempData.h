#ifndef _TEMPDATA_
#define _TEMPDATA_
#include "DataRange.h"

class TempData {
	DataRange tempeSettings;
	
public:
	TempData() {}
	TempData(float, float, bool);
	void setTempData(float, float, bool);
	float getMin();
	float getMax();
	bool getAutoRegulate();
	static float getTemp();
	static void regulate(TempData, bool);
};
#endif
