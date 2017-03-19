#include "xmlParser.h"
#include <iostream>
#include <ctime>
#include <iomanip>

void XmlParser::updateLightSchedule(LightSchedule &ls){
	if(ls.getSchedule().empty()){
		ls.setAutoRegulate(true);
		struct tm myTm;
		time_t t;
		int min;
	
		t = std::time(0);
		myTm = *localtime(&t);
		min = myTm.tm_min;
		std::cout
		<< "Light Schedule" << std::endl
		<< "==============" << std::endl;
	
		for (int i = 0; myTm.tm_min < 59; i++) {
			myTm.tm_min = min + i;
			bool state;
			if(i%2 == 0)
				state = false;
			else
				state = true;
			ls.addLightAction(myTm, state);
			std::cout
			<< myTm.tm_hour << ":"
			<< myTm.tm_min
			<< "    State = "
			<< state << std::endl;
		}	
	}
}

void XmlParser::updateTemperatureRange(TempData &td){
	if(td.getMin() == 0){
		float min = 20;
		float max = 30;
		bool autoReg = true;

		td.setTempData(min, max, autoReg);
		std::cout << std::endl << std::fixed << std::setprecision(2)
			<< "Temperature Range:" << std::endl
			<< "==================" << std::endl
			<< "Min      = " << min << "°C" << std::endl
			<< "Max      = " << max << "°C" << std::endl << std::endl;
	}
}
