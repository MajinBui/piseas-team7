#include "TempData.h"
#include "Pins.h"
#include <cstdlib>
#include <fstream>
#include <string>
//#include <wiringPi.h>
#include <iomanip>
#include <iostream>

TempData::TempData() {
	//pinMode(FAN_PIN, OUTPUT);
	//pinMode(HEATER_PIN, OUTPUT);

	//TempData::toggleFan(false);
	//TempData::toggleHeater(false);
}

TempData::TempData(float min, float max, bool autoReg){
	tempeSettings.setData(min, max, autoReg);
}

void TempData::setTempData(float minTemp, float maxTemp, bool autoReg){
	tempeSettings.setData(minTemp, maxTemp, autoReg);
}

float TempData::getMin(){
	return tempeSettings.getMin();
}

float TempData::getMax(){
	return tempeSettings.getMax();
}

bool TempData::getAutoRegulate(){
	return tempeSettings.getAutoRegulate();
}

void TempData::setMin(float min_) {
	tempeSettings.setMin(min_);
}

void TempData::setMax(float max_) {
	tempeSettings.setMax(max_);
}

void TempData::setAutoRegulate(bool regulate_) {
	tempeSettings.setAutoRegulate(regulate_);
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
void TempData::regulate(TempData tempData, bool manual){
	
	//float curTemp = TempData::getTemp();
	//
	//if(curTemp > tempData.getMax()){
	//	digitalWrite(FAN_PIN, HIGH);
	//}
	//else if(curTemp <= tempData.getMax() && curTemp >= tempData.getMin()){
	//	digitalWrite(FAN_PIN, LOW);
	//}
	//else if(curTemp < tempData.getMin()){
	//
	//}
	//std::cout << std::fixed << std::setprecision(2)
	//<< "Cur Temp = " << TempData::getTemp() << "°C" << std::endl;
	
}

//void TempData::toggleFan(bool state) {
//	digitalWrite(FAN_PIN, state);
//}
//
//void TempData::toggleHeater(bool state) {
//	digitalWrite(HEATER_PIN, state);
//}

