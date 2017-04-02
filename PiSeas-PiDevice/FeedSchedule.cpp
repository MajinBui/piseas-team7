#include "FeedSchedule.h"
#include "Pins.h"
#include <string>
#include <stdio.h>
#include <algorithm>

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

int FeedSchedule::getCount() {
	return count;
}

void FeedSchedule::setAutoFeed(bool autoFeed_) {
	autoFeed = autoFeed_;
}

bool FeedSchedule::getAutoFeed() {
	return autoFeed;
}

void FeedSchedule::updateFeedAction(FeedAction oldFa, FeedAction newFa) {
	std::replace(FeedActions.begin(), FeedActions.end(), oldFa, newFa);
}

void FeedSchedule::removeLightAction(FeedAction fa) {
	FeedActions.remove(fa);
}

void FeedSchedule::operator=(FeedSchedule fs) {
	autoFeed = fs.getAutoFeed();
	count = fs.count;
	FeedActions = fs.FeedActions;
}