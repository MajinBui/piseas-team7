#include "Pins.h"
#include "TempData.h"
#include <fstream>
#include <string>
#include <wiringPi.h>
#include <iomanip>
#include <iostream>

TempData::TempData() {
	pinMode(FAN_PIN, OUTPUT);
	pinMode(HEATER_PIN, OUTPUT);
	
	TempData::toggleFan(false);
	TempData::toggleHeater(false);
}

TempData::TempData(float min, float max, bool autoReg){
	tempSettings.setData(min, max, autoReg);
}

void TempData::setTempData(float minTemp, float maxTemp, bool autoReg){
	tempSettings.setData(minTemp, maxTemp, autoReg);
}

float TempData::getMin(){
	return tempSettings.getMin();
}

float TempData::getMax(){
	return tempSettings.getMax();
}

bool TempData::getAutoRegulate(){
	return tempSettings.getAutoRegulate();
}

float TempData::getTemp(){
	float temperature = 0.0;
	std::string line;
	std::ifstream tempFile;
	tempFile.open("/sys/bus/w1/devices/28-000008290d46/w1_slave");
	if(tempFile.is_open()){
		while(!tempFile.eof()){
			getline(tempFile, line);
			if(line.find("t=") != std::string::npos){
				temperature = (float)atoi(line.substr(line.find("t=") + 2).c_str())/1000;
				return temperature;
			}
		}		
	}
	
	return temperature;
}

void TempData::regulate(TempData tempData, Log &fan, Log &heater){
	
	float curTemp = TempData::getTemp();
	
	if(tempData.getAutoRegulate()){
		if(curTemp > tempData.getMax()){
			TempData::toggleFan(true);
			
			fan.setDateTime();
			fan.setDesc(FANON);
			fan.setType(ACT);
			fan.setUsed(true);
			
			TempData::toggleHeater(false);
			
			heater.setDateTime();
			heater.setDesc(HEATEROFF);
			heater.setType(ACT);
			heater.setUsed(true);
		}
		else if(curTemp <= tempData.getMax() && curTemp >= tempData.getMin()){
			TempData::toggleFan(false);
			
			fan.setDateTime();
			fan.setDesc(FANOFF);
			fan.setType(ACT);
			fan.setUsed(true);
			
			TempData::toggleHeater(false);
			
			heater.setDateTime();
			heater.setDesc(HEATEROFF);
			heater.setType(ACT);
			heater.setUsed(true);
		}
		else if(curTemp < tempData.getMin()){		
			TempData::toggleFan(false);
			
			fan.setDateTime();
			fan.setDesc(FANOFF);
			fan.setType(ACT);
			fan.setUsed(true);
			
			TempData::toggleHeater(true);
			
			heater.setDateTime();
			heater.setDesc(HEATERON);
			heater.setType(ACT);
			heater.setUsed(true);
		}
	}
	else{
		TempData::toggleFan(false);
		
		fan.setDateTime();
		fan.setDesc(FANOFF);
		fan.setType(ACT);
		fan.setUsed(true);
		
		TempData::toggleHeater(false);
		
		heater.setDateTime();
		heater.setDesc(HEATEROFF);
		heater.setType(ACT);
		heater.setUsed(true);
		
	}
	
	std::cout << std::fixed << std::setprecision(2)
	<< "Cur Temp = " << TempData::getTemp() << "°C" << std::endl << std::endl;
}

void TempData::toggleFan(bool state){
	digitalWrite(FAN_PIN, state);
}

void TempData::toggleHeater(bool state){
	digitalWrite(HEATER_PIN, state);
}
