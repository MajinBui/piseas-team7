#include "LightSchedule.h"
#include "Pins.h"
#include <string>
#include <stdio.h>
#include <unistd.h>
#include <wiringPi.h>

LightSchedule::LightSchedule(){
	autoRegulate = false;
}

LightSchedule::LightSchedule(std::list<LightAction> lA, bool aR){
	lightActions = lA;
	autoRegulate = aR;
}

void LightSchedule::setSchedule(std::list<LightAction> lA, bool aR){
	lightActions = lA;
	autoRegulate = aR;
}

void LightSchedule::addLightAction(tm t, bool s){
	lightActions.push_back(LightAction(t, s));
}

void LightSchedule::setAutoRegulate(bool s){
	autoRegulate = s;
}

bool LightSchedule::getAutoRegulate(){
	return autoRegulate;
}

std::list<LightAction> LightSchedule::getSchedule(){
	return lightActions;
}

#include <iostream>
void LightSchedule::regulate(std::list<LightAction> la, bool manual) {
	struct tm curTime;
	time_t t = std::time(0);
	curTime = *localtime(&t);

	if(!la.empty()){
		std::list<LightAction>::iterator it = la.begin();
	
		bool done = false;
		do{
			if(curTime.tm_hour == it->getTime().tm_hour
				&& curTime.tm_min == it->getTime().tm_min){
				digitalWrite(LIGHT_PIN, it->getState());
				done = true;
			}
			else{
				it++;
			}
		}while(it != la.end() && !done);
	}
}
