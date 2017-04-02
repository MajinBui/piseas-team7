#include "Tank.h"

Tank::Tank(){
}

void Tank::setLightSchedule(LightSchedule ls){
	lightSchedule = ls;
}

LightSchedule& Tank::getLightSchedule(){
	return lightSchedule;
}

void Tank::addLightAction(tm t, bool b){
	lightSchedule.addLightAction(t, b);
}

FeedSchedule& Tank::getFeedSchedule() {
	return feedSchedule;
}

void Tank::setFeedSchedule(FeedSchedule fs) {
	feedSchedule = fs;
}

void Tank::addFeedAction(tm t, bool b) {
	feedSchedule.addFeedAction(t, b);
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

Conductivity& Tank::getConductivity() {
	return conductivity;
}

void Tank::setConductivity(Conductivity c_) {
	conductivity = c_;
}

PH& Tank::getPH() {
	return pH;
}

void Tank::setPH(PH ph_) {
	pH = ph_;
}

WaterState& Tank::getWaterState() {
	return waterState;
}

void Tank::setWaterState(WaterState ws_) {
	waterState = ws_;
}
