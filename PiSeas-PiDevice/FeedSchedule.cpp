#include "FeedSchedule.h"
#include "Pins.h"
#include <string>
#include <stdio.h>
#include <algorithm>

FeedSchedule::FeedSchedule() {
	autoFeed = false;
}

std::list<tm>& FeedSchedule::getSchedule() {
	return schedules;
}

void FeedSchedule::addFeedSchedule(tm schedule_) {
	schedules.push_back(schedule_);
}

void FeedSchedule::setAutoFeed(bool autoFeed_) {
	autoFeed = autoFeed_;
}

void FeedSchedule::setManual(bool manual_) {
	manual = manual_;
}

bool FeedSchedule::getAutoFeed() {
	return autoFeed;
}

bool FeedSchedule::getManual() {
	return manual;
}

void FeedSchedule::manualFeed() {


}

void FeedSchedule::reset() {
	schedules.clear();
	autoFeed = false;
}

int FeedSchedule::getCount() {
	return schedules.size();
}

void FeedSchedule::operator=(FeedSchedule fs) {
	autoFeed = fs.getAutoFeed();
	schedules = fs.schedules;
}