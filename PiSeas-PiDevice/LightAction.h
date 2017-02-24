#ifndef _LIGHTACTION_
#define _LIGHTACTION_
#include <ctime>
class LightAction {
	struct tm time;
	bool state;

public:
	LightAction();
	LightAction(tm, bool);
	struct tm getTime();
	bool getState();
	void setAction(tm, bool);
};
#endif