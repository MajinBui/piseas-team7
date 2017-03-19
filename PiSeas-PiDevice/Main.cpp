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
			<< it->getTime().tm_min << ":"
			<< it->getTime().tm_sec << " State = "
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

void updateData(Tank &t){
	updateLight(t.getLightSchedule());
	updateTemperature(t.getTemperatureData());
}

int main() {
	setup();
	Tank t;

	for(;;){
		updateData(t);

		std::thread lightThread;
		std::thread temperatureThread(&TempData::regulate, t.getTemperatureData());
		
		if(t.getLightSchedule().getAutoRegulate()){
			lightThread = std::thread(&LightSchedule::regulate, t.getLightSchedule().getSchedule());
		}
		else {
			LightSchedule::toggleLight(t.getLightSchedule().getManual());
		}
		
		if(t.getLightSchedule().getAutoRegulate()){
			lightThread.join();
		}
		
		temperatureThread.join();

		sleep(5);
	}
}
