#include "WaterState.h"
#include "../libraries/arduPi.h"
#include "Pins.h"

WaterState::WaterState(){
	pinMode(IN_PUMP_PIN, OUTPUT);
	pinMode(OUT_PUMP_PIN, OUTPUT);
	
	WaterState::toggleInPump(false);
	WaterState::toggleOutPump(false);
	
	autoRegulate = false;
	manualDrain = false;
	manualFill = false;
	
	//TODO : Replace below with config file settings.
	time_t t;
	t = std::time(0);
	drainTime = *localtime(&t);
	
	if(drainTime.tm_min != 59)
		drainTime.tm_min += 1;
	else{
		drainTime.tm_min = 0;
		drainTime.tm_hour += 1;
	}
	
	fillTime = drainTime;
	
	if(fillTime.tm_min < 55){
		fillTime.tm_min += 1;
	}
	else{
		fillTime.tm_min = (fillTime.tm_min + 5) - 60;
		fillTime.tm_hour += 1;
	}
	
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

void WaterState::toggleOutPump(bool state){
	digitalWrite(OUT_PUMP_PIN, state);
}

void WaterState::regulate(WaterState wS, Log &inPump, Log &outPump){
	struct tm curTm;
	time_t t;
	t = std::time(0);
	curTm = *localtime(&t);
	
	if(curTm.tm_wday == wS.drainTime.tm_wday){
		if(difftime(t, mktime(&wS.drainTime)) >= 0 && difftime(t, mktime(&wS.drainTime)) <= difftime(mktime(&wS.fillTime), mktime(&wS.drainTime))){
			WaterState::toggleInPump(false);
			WaterState::toggleOutPump(true);

			inPump.setDateTime();
			inPump.setDesc(INPUMPOFF);
			inPump.setType(ACT);
			inPump.setUsed(true);
			
			outPump.setDateTime();
			outPump.setDesc(OUTPUMPON);
			outPump.setType(ACT);
			outPump.setUsed(true);
		}
		else if(WaterState::getWaterSensorReading()){
			WaterState::toggleInPump(true);
			WaterState::toggleOutPump(false);
			
			inPump.setDateTime();
			inPump.setDesc(INPUMPON);
			inPump.setType(ACT);
			inPump.setUsed(true);
			
			outPump.setDateTime();
			outPump.setDesc(OUTPUMPOFF);
			outPump.setType(ACT);
			outPump.setUsed(true);
		}
		else{
			WaterState::toggleInPump(false);
			WaterState::toggleOutPump(false);
			inPump.setDateTime();
			inPump.setDesc(INPUMPOFF);
			inPump.setType(ACT);
			inPump.setUsed(true);
			
			outPump.setDateTime();
			outPump.setDesc(OUTPUMPOFF);
			outPump.setType(ACT);
			outPump.setUsed(true);			
		}
	}
}
