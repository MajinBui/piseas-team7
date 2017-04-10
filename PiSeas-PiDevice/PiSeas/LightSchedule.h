#ifndef _LIGHTSCHEDULE_
#define _LIGHTSCHEDULE_
#include <list>
#include "LightAction.h"
#include "Log.h"

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
	static void regulate(LightSchedule, Log&);
	static void toggleLight(bool);
};
#endif
