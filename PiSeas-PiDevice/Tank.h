#ifndef _TANK_
#define _TANK_
#include "LightSchedule.h"
#include "FeedSchedule.h"
#include "Conductivity.h"
#include "PH.h"
#include "WaterState.h"
#include "TempData.h"

class Tank {
	LightSchedule lightSchedule;
	FeedSchedule feedSchedule;
	TempData tempData;
	Conductivity conductivity;
	PH pH;
	WaterState waterState;

public:
	Tank();
	LightSchedule& getLightSchedule();
	void setLightSchedule(LightSchedule);
	void addLightAction(tm, bool);
	FeedSchedule& getFeedSchedule();
	void setFeedSchedule(FeedSchedule);
	void addFeedAction(tm, bool);
	void setAutoLight(bool);
	bool getAutoLight(bool);
	TempData& getTemperatureData();
	void setTempeData(float, float, bool);
	float getMinTemp();
	float getMaxTemp();
	bool getAutoTemp();
	Conductivity& getConductivity();
	void setConductivity(Conductivity);
	PH& getPH();
	void setPH(PH);
	WaterState& getWaterState();
	void setWaterState(WaterState);

};
#endif
