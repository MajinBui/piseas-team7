#include "TempData.h"
#include "Pins.h"
#include <cstdlib>
#include <fstream>
#include <string>
#include <wiringPi.h>
#include <iomanip>
#include <iostream>

TempData::TempData(float min, float max, bool autoReg){
	tempeSettings.setData(min, max, autoReg);
}

void TempData::setTempData(float minTemp, float maxTemp, bool autoReg){
	tempeSettings.setData(minTemp, maxTemp, autoReg);
}

float TempData::getMin()
{
	return tempeSettings.getMin();
}

float TempData::getMax()
{
	return tempeSettings.getMax();
}

bool TempData::getAutoRegulate()
{
	return tempeSettings.getAutoRegulate();
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
	
	float curTemp = TempData::getTemp();
	
	if(curTemp > tempData.getMax()){
		digitalWrite(FAN_PIN, HIGH);
	}
	else if(curTemp <= tempData.getMax() && curTemp >= tempData.getMin()){
		digitalWrite(FAN_PIN, LOW);
	}
	else if(curTemp < tempData.getMin()){
	
	}
	std::cout << std::fixed << std::setprecision(2)
	<< "Cur Temp = " << TempData::getTemp() << "°C" << std::endl;
	
}

