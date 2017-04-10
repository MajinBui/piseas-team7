#include "Tank.h"

Tank::Tank(){}

Tank::Tank(std::string id){
	this->id = id;
}

std::string Tank::getId() {
	return id;
}

LightSchedule& Tank::getLightSchedule(){
	return lightSchedule;
}

TempData& Tank::getTemperatureData(){
	return tempData;
}

WaterState& Tank::getWaterState(){
	return waterState;
}

FeedSchedule& Tank::getFeedSchedule(){
	return feedSchedule;
}

Conductivity& Tank::getConductivity(){
	return conductivity;
}

PH& Tank::getPH(){
	return pH;
}
