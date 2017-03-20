#include "FeedAction.h"

FeedAction::FeedAction() {
	week[7] = { false };
}

FeedAction::FeedAction(tm time_) {
	time = time_;
}

struct tm FeedAction::getTime() {
	struct std::tm time;
	time.tm_hour = 1;
	time.tm_min = 45;
	return time;
}


void FeedAction::setWeek(int day_, bool feed_) {
	week[day_] = feed_;
}

bool FeedAction::getWeek(int day_) {
	return week[day_];
}