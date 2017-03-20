#ifndef _WATERSTATE_
#define _WATERSTATE_
#include <ctime>

class WaterState {
	struct tm drainTime;
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
	static bool getInPumpStatus();
	static void toggleOutPump(bool);
	static bool getOutPumpStatus();
	static void regulate(WaterState);
};
#endif
