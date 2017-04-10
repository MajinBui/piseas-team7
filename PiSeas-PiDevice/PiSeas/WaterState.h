#ifndef _WATERSTATE_
#define _WATERSTATE_
#include <ctime>
#include "Log.h"

class WaterState {
	struct tm drainTime;
	struct tm fillTime;
	bool autoRegulate;
	bool manualDrain;
	bool manualFill;
	
public:
	WaterState();
	tm getDrainTime();
	void setDrainTime(struct tm);
	bool getAutoRegulate();
	void setAutoRegulate(bool);
	bool getManualDrain();
	void setManualDrain(bool);
	bool getManualFill();
	void setManualFill(bool);
	static bool getWaterSensorReading();
	static void toggleInPump(bool);
	static void toggleOutPump(bool);
	static void regulate(WaterState, Log&, Log&);
};
#endif
