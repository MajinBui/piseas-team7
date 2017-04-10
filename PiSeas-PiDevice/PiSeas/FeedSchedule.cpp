#include "FeedSchedule.h"
#include "Pins.h"
#include "../libraries/arduPi.h"
#include <unistd.h>

FeedSchedule::FeedSchedule() {
	pinMode(FEEDER_PIN, OUTPUT);
	
	autoRegulate = false;
	manual = false;
	count = 0;
}

std::list<tm>& FeedSchedule::getSchedule() {
	return feedTimes;
}

bool FeedSchedule::getAutoRegulate(){
	return autoRegulate;
}

void FeedSchedule::setAutoRegulate(bool autoFeed_) {
	autoRegulate = autoFeed_;
}

bool FeedSchedule::getManual(){
	return manual;
}

void FeedSchedule::setManual(bool m){
	manual = m;
}

int FeedSchedule::getCount() {
	return count;
}

void FeedSchedule::setCount(int c){
	count = c;
}

void FeedSchedule::operator=(FeedSchedule fs) {
	autoRegulate = fs.getAutoRegulate();
	lastFed = fs.lastFed;
	feedTimes = fs.feedTimes;
	count = fs.count;
}

void FeedSchedule::addFeedTime(tm time) {
	feedTimes.push_back(time);
}

void FeedSchedule::toggleFeeder(bool state){
	digitalWrite(FEEDER_PIN, state);
}

bool FeedSchedule::feed(FeedSchedule &fS){
	if(fS.getCount() < 2){
		FeedSchedule::toggleFeeder(true);
		unistd::sleep(10);
		FeedSchedule::toggleFeeder(false);
		fS.count++;
		return true;
	}
	return false;
}

void FeedSchedule::regulate(FeedSchedule &fS, Log& log){
	std::list<tm> feedTimes = fS.getSchedule();
	struct tm curTime;
	time_t t = std::time(0);
	curTime = *localtime(&t);

	if(!feedTimes.empty()){
		std::list<tm>::iterator it = feedTimes.begin();
	
		bool done = false;
		do{
			if(curTime.tm_hour == it->tm_hour
			&& curTime.tm_min == it->tm_min){
				if(FeedSchedule::feed(fS)){
					log.setDateTime();
					log.setDesc(FEEDING);
					log.setType(ACT);
					log.setUsed(true);
				}
			}
			else{
				it++;
			}
		}while(it != feedTimes.end() && !done);
	}
}
