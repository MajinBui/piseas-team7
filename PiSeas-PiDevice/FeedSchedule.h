#ifndef _FeedSCHEDULE_
#define _FeedSCHEDULE_
#include <list>
#include "FeedAction.h"
#include <string>

class FeedSchedule {
	std::list<FeedAction> FeedActions;
	bool autoFeed;
	int count;
public:
	FeedSchedule();
	FeedSchedule(std::list<FeedAction>);
	std::list<FeedAction> getSchedule();
	void setSchedule(std::list<FeedAction>, bool);
	void addFeedAction(FeedAction);
	void setAutoFeed(bool);
	bool getAutoFeed();
	void manualFeed();
	void reset();
	//static void regulate(std::list<FeedAction>, bool);
};
#endif