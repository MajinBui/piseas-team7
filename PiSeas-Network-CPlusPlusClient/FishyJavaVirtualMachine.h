#pragma once
#include <jni.h>
#include <string>
#include <vector>

class FishyJavaVirtualMachine {
private:
	JavaVM *jvm; // Pointer to the JVM (Java Virtual Machine)
	JNIEnv *env; // Pointer to native interface
	jint rc_JVM; // Int to check if JVM is active
	jclass fishyClient; // the fishyClient class to call static methods from
	bool setFeeding(std::string tankId, bool* weekArr, int numberOfRows, int * hour, int * minute, bool autoF, bool manualF);  // might not be used
	bool setLighting(std::string tankId, int * onHr, int * onMin, int * offHr, int * offMin, bool autoLight, bool manualLight, int n); // might not be used
	bool updateTemperatureRange(std::string tankId, float min, float max); // might not be used
public:
	FishyJavaVirtualMachine();
	FishyJavaVirtualMachine(std::string classpath);
	bool isJVMRunning();
	bool isFishyClientRunning();
	bool sendMobileXmlData(std::string tankId, std::string parentPath);
	bool sendSensorData(std::string tankId, std::string parentPath);
	bool sendActionLog(std::string tankId, std::string parentPath);
	bool appendActionLog(std::string tankId, std::vector<std::string> date, std::vector<std::string> desc, std::vector<std::string> type);
	bool updateManualCommands(std::string tankId, bool manualFeed, bool manualLight, bool manualDrain, bool manualFill);
	bool updateUpdateMobileSettings(std::string tankId, bool conductivity, bool feed, bool light, bool pH, bool pump, bool temperature);
	bool appendActionLog(std::string tankId, std::string date, std::string desc, std::string type);
	bool checkMobileSettingsUpdated(std::string tankId, std::string date);
	bool updateTankDetailsSensorData(std::string tankId, std::string pasword);
	bool updateFeedSensorData(std::string tankId, int totalFeeds, int feedHr, int feedMin);
	bool updateTemperatureSensorData(std::string tankId, int currentTemp);
	bool updateSensorSensorData(std::string tankId, int conductivity, float pHcurrent);

	~FishyJavaVirtualMachine();
};