#ifndef _FEEDSCHEDULE_
#define _FEEDSCHEDULE_
#include <list>
#include <string>
#include "Log.h"

class FeedSchedule {
	std::list<tm> feedTimes;
	tm lastFed;
	bool autoRegulate;
	bool manual;
	int count;
	
public:
	FeedSchedule();
	std::list<tm>& getSchedule();
	bool getAutoRegulate();
	void setAutoRegulate(bool);
	bool getManual();
	void setManual(bool);
	int getCount();
	void setCount(int);
	void operator=(FeedSchedule);
	void addFeedTime(tm);
	static void toggleFeeder(bool);
	static bool feed(FeedSchedule&);
	static void regulate(FeedSchedule&, Log&);
};
#endif
