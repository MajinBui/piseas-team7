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
			<< it->getState() << std::endl;
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

void setTime(LightSchedule &ls) {
	XmlParser::updateLightSchedule(ls);
}

void setTemp(TempData &td) {
	XmlParser::updateTemperatureRange(td);
}

void setup(){
	wiringPiSetupGpio();
	
	system("sudo modprobe w1-gpio");
	system("sudo modprobe w1-therm");
	
	pinMode(LIGHT_PIN, OUTPUT);
	pinMode(FAN_PIN, OUTPUT);
	
	digitalWrite(LIGHT_PIN, LOW);
	digitalWrite(FAN_PIN, LOW);
}

int main() {
	Tank t;
	setup();

	for(;;){
		setTime(t.getLightSchedule());
		setTemp(t.getTemperatureData());

		std::thread lightThread;
		std::thread temperatureThread(&TempData::regulate, t.getTemperatureData(), false);

		if(t.getLightSchedule().getAutoRegulate()){
			lightThread = std::thread(&LightSchedule::regulate, "LightSchedule Thread 1 : ", t.getLightSchedule().getSchedule(), .350);
		}
		if(t.getLightSchedule().getAutoRegulate()){
			lightThread.join();
		}
		
		temperatureThread.join();

		sleep(5);
	}
}
