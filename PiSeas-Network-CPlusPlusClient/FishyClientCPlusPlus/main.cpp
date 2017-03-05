#include "FishyJavaVirtualMachine.h"
#include <iostream>
#include <string>
void printSuccess(bool rc, std::string functionName) {
	std::cout << functionName << ": " << ((rc) ? "passed" : "failed") << std::endl;
}

int main()
{
	FishyJavaVirtualMachine vm;
	std::string tankId = "QWERT";
	std::string inputPath = "input";
	std::string outputPath = "output";

	bool weekArr[2][7] = {
		{ true, false, true, true, true, false, false },
		{ false, true, false, true, false, false, false }
	};

	std::vector<std::string> date;
	date.push_back("2017-02-20T19:19:19+0500");
	date.push_back("2017-02-20T20:19:19+0500");
	date.push_back("2017-02-20T21:19:19+0500");
	date.push_back("2017-02-20T22:19:19+0500");
	std::vector<std::string> desc;
	desc.push_back("FEEDACTIVE");
	desc.push_back("TEMPRANGE");
	desc.push_back("MANFEEDING");
	desc.push_back("FEEDACTIVE");
	std::vector<std::string> type;
	type.push_back("ACT");
	type.push_back("NOT");
	type.push_back("MANACT");
	type.push_back("ACT");

	int hour[2] = { 13, 07 };
	int minute[2] = { 50, 30 };


	int onHr[2] = { 12, 30 };
	int onMin[2] = { 12, 00 };

	int offHr[2] = { 12, 18 };
	int offMin[2] = { 30, 30 };

	int conductivity = 2;;
	float pHcurrent = 10;

	int feedHr = 12;
	int feedMin = 30;

	printSuccess(vm.sendMobileXmlData(tankId, inputPath), "sendMobileXmlData");
	printSuccess(vm.sendSensorData(tankId, inputPath), "sendSensorData");
	printSuccess(vm.sendActionLog(tankId, inputPath), "sendActionLog");

	printSuccess(vm.appendActionLog(tankId, date.at(1), desc.at(1), type.at(1)), "appendActionLog");
	printSuccess(vm.appendActionLog(tankId, date, desc, type), "appendActionLog");
	printSuccess(vm.updateManualCommands(tankId, true, true, true, false), "updateManualCommands");
	printSuccess(vm.updateUpdateMobileSettings(tankId, true, false, false, false, true, true), "updateUpdateMobileSettings");
	printSuccess(vm.updateTankDetailsSensorData(tankId, "1234"), "updateTankDetailsSensorData");
	printSuccess(vm.updateFeedSensorData(tankId, 3, feedHr, feedMin), "updateFeedSensorData");
	printSuccess(vm.updateTemperatureSensorData(tankId, -4), "updateTemperatureSensorData");
	printSuccess(vm.updateSensorSensorData(tankId, conductivity, pHcurrent), "updateSensorSensorData");
	printSuccess(vm.checkMobileSettingsUpdated(tankId, date.at(1)), "checkMobileSettingsUpdated");

	printSuccess(vm.retrieveMobileXmlData(tankId, outputPath), "retrieveMobileXmlData");
	printSuccess(vm.retrieveSensorData(tankId, outputPath), "retrieveSensorData");
	printSuccess(vm.retrieveActionLog(tankId, outputPath), "retrieveActionLog");

#ifdef _WIN32
	system("pause");
#else
	system("read -r -p 'Press any key to continue...' key");
#endif
	
}
