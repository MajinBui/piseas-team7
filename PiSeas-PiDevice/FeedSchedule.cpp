#include "FeedSchedule.h"

FeedSchedule::FeedSchedule() {
	autoFeed = false;
	count = 0;
}

FeedSchedule::FeedSchedule(std::list<FeedAction> FeedActions_) {
	FeedActions = FeedActions_;
}

std::list<FeedAction> FeedSchedule::getSchedule() {
	return FeedActions;
}

void FeedSchedule::setSchedule(std::list<FeedAction>, bool) {

}

void FeedSchedule::addFeedAction(FeedAction feed_) {
	FeedActions.push_back(feed_);
}

void FeedSchedule::setAutoFeed(bool autoFeed_) {
	autoFeed = autoFeed_;
}

bool FeedSchedule::getAutoFeed() {
	return autoFeed;
}

void FeedSchedule::manualFeed() {

}

void FeedSchedule::reset() {

}
