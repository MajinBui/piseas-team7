#ifndef _FeedSCHEDULE_
#define _FeedSCHEDULE_
#include <list>
#include "FeedAction.h"
#include <string>

class FeedSchedule {
	std::list<tm> schedules;
	tm lastFed;
	bool autoFeed;
public:
	FeedSchedule();
	std::list<tm>& getSchedule();
	void addFeedSchedule(tm);
	void setAutoFeed(bool);
	bool getAutoFeed();
	void manualFeed();
	void reset();
	int getCount();
	bool getAutoFeed();
	void operator=(FeedSchedule);
	//static void regulate(std::list<FeedAction>, bool);
};
#endif