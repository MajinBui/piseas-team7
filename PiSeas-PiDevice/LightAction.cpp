#include "LightAction.h"

LightAction::LightAction(){
	state = false;
}

LightAction::LightAction(tm t, bool s){
	time = t;
	state = s;
}

void LightAction::setAction(tm t, bool s){
	time = t;
	state = s;
}

tm LightAction::getTime(){
	return time;
}

bool LightAction::getState(){
	return state;
}

#include <iostream>
bool LightAction::operator==(LightAction lA) {
	if(difftime(mktime(&time),mktime(&lA.time)) == 0 && state == lA.state)
		return true;
	else
		return false;
}
