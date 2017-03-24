#include <ctime>
#include <iostream>
#include <thread>
#include <stdio.h>
#include <wiringPi.h>
#include <unistd.h>

#include "Pins.h"
#include "Tank.h"
#include "xmlParser.h"

void printTime(LightSchedule &ls) {
	std::list<LightAction> la = ls.getSchedule();

	std::list<LightAction>::iterator it = la.begin();

	std::cout << std::endl
		<< "Time in main" << std::endl
		<< "============" << std::endl << std::endl;

	while (it != la.end()) {
		std::cout
			<< it->getTime().tm_hour << ":"
			<< it->getTime().tm_min << " State = "
			<< it->getState() << " Manual = "
			<< ls.getManual() << std::endl;
		it++;
	}
}

void printTemp(TempData &td) {
	std::cout << std::endl
		<< "Temperature Data in main" << std::endl
		<< "========================" << std::endl
		<< "Min      = " << td.getMin() << std::endl
		<< "Max      = " << td.getMax() << std::endl
		<< "Auto Reg = " << td.getAutoRegulate() << std::endl;
}

void printWater(WaterState &wS){
	struct tm dT = wS.getDrainTime();
	std::cout
		<< "Water State in main" << std::endl
		<< "===================" << std::endl
		<< dT.tm_hour << ":"
		<< dT.tm_min << " AutoReg = "
		<< wS.getAutoRegulate() << std::endl;
}

void setup(){
	wiringPiSetupGpio();
	
	system("sudo modprobe w1-gpio");
	system("sudo modprobe w1-therm");
}

void updateLight(LightSchedule &ls) {
	XmlParser::updateLightSchedule(ls);
}

void updateTemperature(TempData &td) {
	XmlParser::updateTemperatureRange(td);
}

void updateWaterState(WaterState &wS){
	//XmlParser::updateWaterState(wS);
}

void updateData(Tank &t){
	updateLight(t.getLightSchedule());
	updateTemperature(t.getTemperatureData());
	updateWaterState(t.getWaterState());
}

int main() {
	setup();
	Tank t;

	for(;;){
		updateData(t);

		std::thread lightThread;
		std::thread temperatureThread(&TempData::regulate, t.getTemperatureData());
		std::thread waterStateThread;
		
		if(t.getLightSchedule().getAutoRegulate()){
			lightThread = std::thread(&LightSchedule::regulate, t.getLightSchedule().getSchedule());
		}
		else {
			LightSchedule::toggleLight(t.getLightSchedule().getManual());
		}
		sleep(0.01);
		if(t.getWaterState().getAutoRegulate()){
			waterStateThread = std::thread(&WaterState::regulate, t.getWaterState());
		}
		else if(t.getWaterState().getManualDrain()){
			if(!WaterState::getWaterSensorReading()){
				WaterState::toggleOutPump(true);
			}
			else{
				WaterState::toggleOutPump(false);
			}
		}
		else if(t.getWaterState().getManualFill()){
			if(WaterState::getWaterSensorReading()){
				WaterState::toggleInPump(true);
			}
			else{
				WaterState::toggleInPump(false);
			}
		}
		
		
		if(lightThread.joinable()){
			lightThread.join();
		}
		
		if(waterStateThread.joinable()){
			waterStateThread.join();
		}
		
		temperatureThread.join();

		std::cout << std::endl << "======================================="
		 << std::endl << std::endl;

		sleep(5);
	}
}
