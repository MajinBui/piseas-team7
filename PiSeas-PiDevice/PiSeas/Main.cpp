#include <ctime>
#include <iostream>
#include <thread>
#include <stdio.h>
#include <wiringPi.h>
#include <unistd.h>

#include "Log.h"
#include "Pins.h"
#include "Tank.h"
#include "xmlParser.h"
#include "FishyJavaVirtualMachine.h"

void printTime(LightSchedule ls) {
	std::list<LightAction> la = ls.getSchedule();

	std::list<LightAction>::iterator it = la.begin();

	std::cout << std::endl
		<< "Time in main" << std::endl
		<< "============" << std::endl << std::endl;

	while (it != la.end()) {
		std::cout
			<< it->getTime().tm_hour << ":"
			<< it->getTime().tm_min << " State = "
			<< it->getState() << std::endl;
		it++;
	}
	
	std::cout
		<< "Manual = " << ls.getManual() << std::endl
		<< "Auto = " << ls.getAutoRegulate() << std::endl 
		<< std::endl << std::endl;
}

void printTemp(TempData td) {
	std::cout
		<< "Temperature Data in main" << std::endl
		<< "========================" << std::endl
		<< "Min      = " << td.getMin() << std::endl
		<< "Max      = " << td.getMax() << std::endl
		<< "Auto Reg = " << td.getAutoRegulate() << std::endl << std::endl;
}

void printWater(WaterState wS){
	struct tm dT = wS.getDrainTime();
	std::cout
		<< "Water State in main" << std::endl
		<< "===================" << std::endl
		<< "AutoReg = "	<< wS.getAutoRegulate() << std::endl
		<< "Manual Drain = " << wS.getManualDrain() << std::endl
		<< "Manual Fill = " << wS.getManualFill() << std::endl << std::endl;
}

void printFeed(FeedSchedule fS){
	std::list<tm> feedTimes = fS.getSchedule();

	std::list<tm>::iterator it = feedTimes.begin();

	std::cout << std::endl
		<< "Feed in main" << std::endl
		<< "============" << std::endl << std::endl;

	while (it != feedTimes.end()) {
		std::cout
			<< it->tm_hour << ":"
			<< it->tm_min << " " << it->tm_wday << std::endl;
		it++;
	}
	
	std::cout
		<< "Manual = " << fS.getManual() << std::endl
		<< "Auto = " << fS.getAutoRegulate() << std::endl << std::endl;
}

void printConductivity(Conductivity c) {
	std::cout
		<< "Conductivity Data in main" << std::endl
		<< "=========================" << std::endl
		<< "Min      = " << c.getMin() << std::endl
		<< "Max      = " << c.getMax() << std::endl
		<< "Auto Reg = " << c.getAutoRegulate() << std::endl << std::endl;
}

void printPH(PH pH) {
	std::cout
		<< "PH Data in main" << std::endl
		<< "=========================" << std::endl
		<< "Min      = " << pH.getMin() << std::endl
		<< "Max      = " << pH.getMax() << std::endl
		<< "Auto Reg = " << pH.getAutoRegulate() << std::endl << std::endl;
}

void printTank(Tank t){
	printTime(t.getLightSchedule());
	printTemp(t.getTemperatureData());
	printWater(t.getWaterState());
	printFeed(t.getFeedSchedule());
	printConductivity(t.getConductivity());
	printPH(t.getPH());
}

bool midnight(){
	bool reset = false;
	struct tm curTime;
	time_t t = std::time(0);
	curTime = *localtime(&t);
	
	if(curTime.tm_hour == 0 && curTime.tm_min == 0)
		reset = true;
		
	return reset;
}

void setFeed(Tank &tank, FishyJavaVirtualMachine &v, FeedSchedule f){
	struct tm curTime;
	time_t t = std::time(0);
	
	curTime = *localtime(&t);
	
	v.updateFeedSensorData(tank.getId(), f.getCount(), curTime.tm_hour, curTime.tm_min);
}

//Initial setup.
void setup(Tank &t, FishyJavaVirtualMachine &v){
	wiringPiSetupGpio();
	
	system("sudo modprobe w1-gpio");
	system("sudo modprobe w1-therm");
	
	v.updateUpdateMobileSettings(t.getId(), true, true, true, true, true, true);
}

//Retrieve settings xml from server and update tank object using the pulled xml.
void updateData(Tank &t, FishyJavaVirtualMachine &v){
	v.retrieveMobileXmlData(t.getId(), ".");
	
	if(midnight()){
		t.getFeedSchedule().setCount(0);
	}
	
	XmlParser::updateLightSchedule(t.getLightSchedule());
	XmlParser::updateTemperatureRange(t.getTemperatureData());
	XmlParser::updateWaterState(t.getWaterState());
	XmlParser::updateFeedSchedule(t.getFeedSchedule());
	XmlParser::updateConductivityRange(t.getConductivity());
	XmlParser::updatePHRange(t.getPH());
	
	v.updateUpdateMobileSettings(t.getId(), false, false, false, false, false, false);
}

float median(float* array, int length) {
	int k,l,m,n;
	float aux[length];

	// Initialization of the auxiliar array
	for(k=0;k<length;k++) aux[k]=-100000;
	aux[0] = array[0];

	// Ordering of the sensor from lower to higher value
	for (l=1;l<length;l++)
	{
		for(m=0;m<l+1;m++)
		{
			if(array[l]>aux[m])
			{
				for(n=length-1;n>m;n--) aux[n]=aux[n-1];
				aux[m]=array[l];
				m=l+1;
				continue;
			}
		}
	}

	// The value in the central position of the array is returned
	return aux[int(length/2)];
}

int main() {
	FishyJavaVirtualMachine vm;
	Tank t("x");
	setup(t, vm);

	for(;;){
		//Update the settings in the tank to match the server's current xml.
		updateData(t, vm);
		
		//TESTING 
		//printTank(t);
		
		Log lightLog;
		Log fanLog;
		Log heaterLog;
		Log inPumpLog;
		Log outPumpLog;
		Log feedLog;
		Log conductivityLog;
		Log pHLog;

		std::thread lightThread;
		std::thread temperatureThread(&TempData::regulate, t.getTemperatureData(), std::ref(fanLog), std::ref(heaterLog));
		std::thread waterStateThread;
		std::thread feedThread;
		
		//Create thread(s) or perform manual action(s).
		if(t.getLightSchedule().getAutoRegulate()){
			lightThread = std::thread(&LightSchedule::regulate, t.getLightSchedule(), std::ref(lightLog));
		}
		else {
			LightSchedule::toggleLight(t.getLightSchedule().getManual());
			lightLog.setDateTime();
			if(t.getLightSchedule().getManual())
				lightLog.setDesc(LIGHTON);
			else
				lightLog.setDesc(LIGHTOFF);
			lightLog.setType(MANACT);
			lightLog.setUsed(true);
		}
		
		if(t.getWaterState().getAutoRegulate()){
			waterStateThread = std::thread(&WaterState::regulate, t.getWaterState(), std::ref(inPumpLog), std::ref(outPumpLog));
		}
		else if(t.getWaterState().getManualDrain()){
			WaterState::toggleOutPump(true);
			WaterState::toggleInPump(false);
			
			inPumpLog.setDateTime();
			inPumpLog.setDesc(INPUMPON);
			inPumpLog.setType(MANACT);
			inPumpLog.setUsed(true);
			
			outPumpLog.setDateTime();
			outPumpLog.setDesc(OUTPUMPOFF);
			outPumpLog.setType(MANACT);
			outPumpLog.setUsed(true);
		}
		else if(t.getWaterState().getManualFill()){
			if(WaterState::getWaterSensorReading()){
				WaterState::toggleInPump(true);
				WaterState::toggleOutPump(false);

				inPumpLog.setDateTime();
				inPumpLog.setDesc(INPUMPON);
				inPumpLog.setType(MANACT);
				inPumpLog.setUsed(true);
				
				outPumpLog.setDateTime();
				outPumpLog.setDesc(OUTPUMPOFF);
				outPumpLog.setType(MANACT);
				outPumpLog.setUsed(true);
			}
			else{
				WaterState::toggleInPump(false);
				
				inPumpLog.setDateTime();
				inPumpLog.setDesc(INPUMPON);
				inPumpLog.setType(MANACT);
				inPumpLog.setUsed(true);
			}
		}
		else{
			WaterState::toggleOutPump(false);
			WaterState::toggleInPump(false);
			
			inPumpLog.setDateTime();
			inPumpLog.setDesc(INPUMPOFF);
			inPumpLog.setType(MANACT);
			inPumpLog.setUsed(true);
				
			outPumpLog.setDateTime();
			outPumpLog.setDesc(OUTPUMPOFF);
			outPumpLog.setType(MANACT);
			outPumpLog.setUsed(true);
		}
		
		if(t.getFeedSchedule().getAutoRegulate()){
			feedThread = std::thread(&FeedSchedule::regulate, std::ref(t.getFeedSchedule()), std::ref(feedLog));
		}
		else if (t.getFeedSchedule().getManual()){
			if(FeedSchedule::feed(t.getFeedSchedule())){
				feedLog.setDateTime();
				feedLog.setDesc(FEEDING);
				feedLog.setType(MANACT);
				feedLog.setUsed(true);
				
				setFeed(t, vm, t.getFeedSchedule());
			}
			else{
				feedLog.setDateTime();
				feedLog.setDesc(FEEDING);
				feedLog.setType(MANACT);
				feedLog.setUsed(true);
			}
		}
		
		//Send sensor data to server.
		float cond = Conductivity::getConductivity();
		int pH = PH::getPH();
		
		std::cout << "Conductivity = " << cond << std::endl;
		std::cout << "pH = " << pH << std::endl;
		
		vm.updateTemperatureSensorData(t.getId(), TempData::getTemp());
		vm.updateSensorSensorData(t.getId(), cond, pH);
		
		if(t.getConductivity().getAutoRegulate()){
			if(cond > t.getConductivity().getMax() || cond < t.getConductivity().getMin()){
				conductivityLog.setDateTime();
				conductivityLog.setDesc(WATERCHANGEREQ);
				conductivityLog.setType(NOT);
				conductivityLog.setUsed(true);
			}
		}
		
		if(t.getPH().getAutoRegulate()){
			if(pH > t.getPH().getMax() || pH < t.getPH().getMin()){
				pHLog.setDateTime();
				pHLog.setDesc(PHOUTOFBOUNDS);
				pHLog.setType(NOT);
				pHLog.setUsed(true);
			}			
		}
		
		sleep(15);
		
		//Join all threads here.
		if(lightThread.joinable()){
			lightThread.join();
		}
		
		if(waterStateThread.joinable()){
			waterStateThread.join();
		}
		
		if(feedThread.joinable()){
			feedThread.join();
		}
		
		temperatureThread.join();
		
		
		//Send logs to server.		
		if(lightLog.getUsed()){
			vm.appendActionLog(t.getId(), lightLog.getDateTime(), lightLog.getDesc(), lightLog.getType());
		}
		if(fanLog.getUsed()){
			vm.appendActionLog(t.getId(), fanLog.getDateTime(), fanLog.getDesc(), fanLog.getType());
		}
		if(heaterLog.getUsed()){
			vm.appendActionLog(t.getId(), heaterLog.getDateTime(), heaterLog.getDesc(), heaterLog.getType());
		}
		if(inPumpLog.getUsed()){
			vm.appendActionLog(t.getId(), inPumpLog.getDateTime(), inPumpLog.getDesc(), inPumpLog.getType());
		}
		if(outPumpLog.getUsed()){
			vm.appendActionLog(t.getId(), outPumpLog.getDateTime(), outPumpLog.getDesc(), outPumpLog.getType());
		}
		if(conductivityLog.getUsed()){
			vm.appendActionLog(t.getId(), conductivityLog.getDateTime(), conductivityLog.getDesc(), conductivityLog.getType());
		}
		if(pHLog.getUsed()){
			vm.appendActionLog(t.getId(), pHLog.getDateTime(), pHLog.getDesc(), pHLog.getType());
		}		
		
		
		sleep(45);
	}
}
