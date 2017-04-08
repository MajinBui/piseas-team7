#ifndef _FeedSCHEDULE_
#define _FeedSCHEDULE_
#include <list>
#include "FeedAction.h"
#include <string>

class FeedSchedule {
	std::list<tm> schedules;
	tm lastFed;
	bool autoFeed;
	bool manual;
public:
	FeedSchedule();
	std::list<tm>& getSchedule();
	void addFeedSchedule(tm);
	void setAutoFeed(bool);
	void setManual(bool);
	bool getAutoFeed();
	bool getManual();
	void manualFeed();
	void reset();
	int getCount();
	void operator=(FeedSchedule);
	//static void regulate(std::list<FeedAction>, bool);
};
#endif