#ifndef _FEEDACTION_
#define _FEEDACTION_
#include <ctime>
class FeedAction {
	struct tm time;			// hr/min of feed
	bool week[7];			// array of 7, each element being a day of the week. Starts on sunday
public:
	FeedAction();
	FeedAction(tm);
	struct tm getTime();
	void setWeek(int, bool);
	bool getWeek(int);
};
#endif