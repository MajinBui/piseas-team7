#ifndef _TEMPDATA_
#define _TEMPDATA_
#include "DataRange.h"
#include "Log.h"

class TempData {
	DataRange tempSettings;
	
public:
	TempData();
	TempData(float, float, bool);
	void setTempData(float, float, bool);
	float getMin();
	float getMax();
	bool getAutoRegulate();
	static float getTemp();
	static void regulate(TempData, Log&, Log&);
	static void toggleFan(bool);
	static void toggleHeater(bool);
};
#endif
