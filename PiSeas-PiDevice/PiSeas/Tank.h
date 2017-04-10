#ifndef _TANK_
#define _TANK_
#include <string>
#include "LightSchedule.h"
#include "TempData.h"
#include "WaterState.h"
#include "FeedSchedule.h"
#include "Conductivity.h"
#include "PH.h"

class Tank {
	std::string id;
	LightSchedule lightSchedule;
	TempData tempData;
	WaterState waterState;
	FeedSchedule feedSchedule;
	Conductivity conductivity;
	PH pH;

public:
	Tank();
	Tank(std::string id);
	std::string getId();
	LightSchedule& getLightSchedule();
	TempData& getTemperatureData();
	WaterState& getWaterState();
	FeedSchedule& getFeedSchedule();
	Conductivity& getConductivity();
	PH& getPH();
};
#endif
