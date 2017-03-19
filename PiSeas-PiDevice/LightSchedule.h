#ifndef _LIGHTSCHEDULE_
#define _LIGHTSCHEDULE_
#include <list>
#include "LightAction.h"
#include <string>

class LightSchedule{
	std::list<LightAction> lightActions;
	bool autoRegulate;
	bool manual;

public:
	LightSchedule();
	LightSchedule(std::list<LightAction>, bool, bool);
	std::list<LightAction>& getSchedule();
	void setSchedule(std::list<LightAction>, bool);
	bool getAutoRegulate();
	void setAutoRegulate(bool);
	bool getManual();
	void setManual(bool);
	void addLightAction(tm, bool);
	void updateLightAction(LightAction, LightAction);
	void removeLightAction(LightAction);
	static void regulate(std::list<LightAction>);
	static void toggleLight(bool);
};
#endif
