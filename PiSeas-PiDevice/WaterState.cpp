#include "WaterState.h"
#include "wiringPi.h"
#include "Pins.h"

WaterState::WaterState(){
	pinMode(IN_PUMP_PIN, OUTPUT);
	pinMode(OUT_PUMP_PIN, OUTPUT);
	
	WaterState::toggleInPump(false);
	WaterState::toggleOutPump(false);
	
	bool autoRegulate = false;
	bool manualDrain = false;
	bool manualFill = false;
}

tm WaterState::getDrainTime(){
	return drainTime;
}

void WaterState::setDrainTime(struct tm dT){
	drainTime = dT;
}

bool WaterState::getAutoRegulate(){
	return autoRegulate;
}

void WaterState::setAutoRegulate(bool aR){
	autoRegulate = aR;
}

bool WaterState::getManualDrain(){
	return manualDrain;
}

void WaterState::setManualDrain(bool mD){
	manualDrain = mD;
}

bool WaterState::getManualFill(){
	return manualFill;
}

void WaterState::setManualFill(bool mF){
	manualFill = mF;
}

bool WaterState::getWaterSensorReading(){
	return digitalRead(WATER_LEVEL_SENSOR_PIN);
}
	
void WaterState::toggleInPump(bool state){
	digitalWrite(IN_PUMP_PIN, state);
}

bool WaterState::getInPumpStatus(){
	return digitalRead(IN_PUMP_PIN);
}

void WaterState::toggleOutPump(bool state){
	digitalWrite(OUT_PUMP_PIN, state);
}

bool WaterState::getOutPumpStatus(){
	return digitalRead(OUT_PUMP_PIN);
}
#include <iostream>
void WaterState::regulate(WaterState wS){
	if(WaterState::getWaterSensorReading()){
		std::cout << "WATER LOW, Turning IN pump ON, and OUT pump OFF" << std::endl;
		WaterState::toggleInPump(true);
		WaterState::toggleOutPump(false);
	}
	else{
		std::cout << "WATER HIGH, Turning IN pump OFF, and OUT pump ON" << std::endl;
		WaterState::toggleInPump(false);
		WaterState::toggleOutPump(true);
	}
}
