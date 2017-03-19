#include "LightSchedule.h"
#include "Pins.h"
#include <string>
#include <stdio.h>
#include <unistd.h>
#include <wiringPi.h>
#include <algorithm>

LightSchedule::LightSchedule(){
	autoRegulate = false;
	manual = false;
	
	pinMode(LIGHT_PIN, OUTPUT);	
	LightSchedule::toggleLight(false);
}

LightSchedule::LightSchedule(std::list<LightAction> lA, bool aR, bool m){
	lightActions = lA;
	autoRegulate = aR;
	manual = m;
}

std::list<LightAction>& LightSchedule::getSchedule(){
	return lightActions;
}

void LightSchedule::setSchedule(std::list<LightAction> lA, bool aR){
	lightActions = lA;
	autoRegulate = aR;
}

bool LightSchedule::getAutoRegulate(){
	return autoRegulate;
}

void LightSchedule::setAutoRegulate(bool s){
	autoRegulate = s;
}

bool LightSchedule::getManual(){
	return manual;
}

void LightSchedule::setManual(bool m){
	manual = m;
	autoRegulate = false;
}

void LightSchedule::addLightAction(tm t, bool s){
	lightActions.push_back(LightAction(t, s));
}

void LightSchedule::updateLightAction(LightAction oldLa, LightAction newLa){
	std::replace (lightActions.begin(), lightActions.end(), oldLa, newLa);
}

void LightSchedule::removeLightAction(LightAction lA){
	lightActions.remove(lA);
}

void LightSchedule::regulate(std::list<LightAction> la) {
	struct tm curTime;
	time_t t = std::time(0);
	curTime = *localtime(&t);

	if(!la.empty()){
		std::list<LightAction>::iterator it = la.begin();
	
		bool done = false;
		do{
			if(curTime.tm_hour == it->getTime().tm_hour
			&& curTime.tm_min == it->getTime().tm_min){
				LightSchedule::toggleLight(it->getState());
				done = true;
				LightAction temp(curTime, it->getState());
			}
			else{
				it++;
			}
		}while(it != la.end() && !done);
	}
}


void LightSchedule::toggleLight(bool state) {
	digitalWrite(LIGHT_PIN, state);
}
