#include "LightSchedule.h"
#include "Pins.h"
#include <string>
#include <stdio.h>
#include <unistd.h>
#include <wiringPi.h>
#include <algorithm>

LightSchedule::LightSchedule(){
	autoRegulate = false;
	pinMode(LIGHT_PIN, OUTPUT);
	digitalWrite(LIGHT_PIN, LOW);
}

LightSchedule::LightSchedule(std::list<LightAction> lA, bool aR){
	lightActions = lA;
	autoRegulate = aR;
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

void LightSchedule::addLightAction(tm t, bool s){
	lightActions.push_back(LightAction(t, s));
}

void LightSchedule::updateLightAction(LightAction oldLa, LightAction newLa){
		std::replace (lightActions.begin(), lightActions.end(), oldLa, newLa);
}

void LightSchedule::removeLightAction(LightAction lA){
	lightActions.remove(lA);
}

#include <iostream>

void LightSchedule::regulate(std::list<LightAction>& la, bool manual) {
	struct tm curTime;
	time_t t = std::time(0);
	curTime = *localtime(&t);
	
	/*char buff[25];
	strftime(buff, 25, "%Y-%m-%dT%H:%M:%S%z", &curTime);
	
	std::string formattedTime(buff);
	
	std::cout
	<< "===TIME ZONE===\n"
	<< formattedTime << std::endl
	<< "===============" << std::endl;*/

	if(!la.empty()){
		std::list<LightAction>::iterator it = la.begin();
	
		bool done = false;
		do{
			if(curTime.tm_hour == it->getTime().tm_hour
				&& curTime.tm_min == it->getTime().tm_min){
				digitalWrite(LIGHT_PIN, it->getState());
				done = true;
				LightAction temp(curTime, it->getState());
			}
			else{
				it++;
			}
		}while(it != la.end() && !done);
	}
}





