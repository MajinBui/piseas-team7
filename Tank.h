#ifndef _TANK_
#define _TANK_
#include "LightSchedule.h"
#include "TempData.h"

class Tank {
	LightSchedule lightSchedule;
	TempData tempData;

public:
	Tank();
	LightSchedule& getLightSchedule();
	void setLightSchedule(LightSchedule);
	void addLightAction(tm, bool);
	void setAutoLight(bool);
	bool getAutoLight(bool);

	TempData& getTemperatureData();
	void setTempeData(float, float, bool);
	float getMinTemp();
	float getMaxTemp();
	bool getAutoTemp();
};
#endif
