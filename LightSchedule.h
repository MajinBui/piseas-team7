#ifndef _LIGHTSCHEDULE_
#define _LIGHTSCHEDULE_
#include <list>
#include "LightAction.h"
#include <string>

class LightSchedule{
	std::list<LightAction> lightActions;
	bool autoRegulate;

public:
	LightSchedule();
	LightSchedule(std::list<LightAction>, bool);
	std::list<LightAction> getSchedule();
	void setSchedule(std::list<LightAction>, bool);
	void addLightAction(tm, bool);
	void setAutoRegulate(bool);
	bool getAutoRegulate();
	static void regulate(std::string, std::list<LightAction>, int);
};
#endif