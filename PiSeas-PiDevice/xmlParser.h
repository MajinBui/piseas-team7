#ifndef _XMLPARSE_
#define _XMLPARSE_
#include "Tank.h"
#include <string>
#include <string.h>
#include <stdio.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <ctime>
#include "rapidxml_utils.hpp"

const static char* mobileName = "1_mobile_settings.xml";

class XmlParser {
	static std::string parser(const char*, const char*);
public:
	static void updateLightSchedule(LightSchedule&);
	static void updateFeedSchedule(FeedSchedule&);
	static void updateTemperatureRange(TempData&);
	static void updateConductivityRange(Conductivity&);
	static void updatePHRange(PH&);
	static void updateWaterState(WaterState&);
	static bool getSettingsUpdated(const char*);
	static std::tm getSettingsDate();
	static std::string getSettingsID();
	static std::string getSettingsName();
	static std::string getSettingsPassword();
	static float getSettingsSize();
	static std::string getSettingsDescription();
	static bool getSettingsType();
	static bool getSettingsManualFeed();
	static bool getSettingsAutoFeed();
	static bool getSettingsManualLight();
	static bool getSettingsAutoLight();
	static float getSettingsMinTemp();
	static float getSettingsMaxTemp();
	static bool getSettingsTempAuto();
	static bool getSettingsDrain();
	static bool getSettingsFill();
	static bool getSettingsAutoWaterChange();
	static float getSettingsPHMin();
	static float getSettingsPHMax();
	static float getSettingsPHAuto();
	static float getSettingsCMin();
	static float getSettingsCMax();
	static bool getSettingsCAuto();
};

#endif