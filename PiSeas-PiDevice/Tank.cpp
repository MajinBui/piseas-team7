#include "Tank.h"

Tank::Tank(){}

void Tank::setLightSchedule(LightSchedule ls){
	lightSchedule = ls;
}

LightSchedule& Tank::getLightSchedule(){
	return lightSchedule;
}

void Tank::addLightAction(tm t, bool b){
	lightSchedule.addLightAction(t, b);
}

void Tank::setAutoLight(bool s){
	lightSchedule.setAutoRegulate(s);
}

bool Tank::getAutoLight(bool){
	return lightSchedule.getAutoRegulate();
}

TempData& Tank::getTemperatureData(){
	return tempData;
}

void Tank::setTempeData(float min, float max, bool autoReg){
	tempData.setTempData(min, max, autoReg);
}

float Tank::getMinTemp(){
	return tempData.getMin();
}

float Tank::getMaxTemp(){
	return tempData.getMax();
}

bool Tank::getAutoTemp(){
	return tempData.getAutoRegulate();
}


WaterState& Tank::getWaterState(){
	return waterState;
}
