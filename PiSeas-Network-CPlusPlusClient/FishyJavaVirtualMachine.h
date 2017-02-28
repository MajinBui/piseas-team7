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
	void setFeeding(std::string tankId, bool* weekArr, int numberOfRows, int * hour, int * minute, bool autoF, bool manualF);  // might not be used
	void setLighting(std::string tankId, int * onHr, int * onMin, int * offHr, int * offMin, bool autoLight, bool manualLight, int n); // might not be used
	void updateTemperatureRange(std::string tankId, float min, float max); // might not be used
public:
	FishyJavaVirtualMachine();
	FishyJavaVirtualMachine(std::string classpath);
	bool isJVMRunning();
	bool isFishyClientRunning();
	void sendMobileXmlData(std::string tankId, std::string parentPath);
	void sendSensorData(std::string tankId, std::string parentPath);
	void sendActionLog(std::string tankId, std::string parentPath);
	void appendActionLog(std::string tankId, std::vector<std::string> date, std::vector<std::string> desc, std::vector<std::string> type);
	void updateManualCommands(std::string tankId, bool manualFeed, bool manualLight, bool manualDrain, bool manualFill);
	void updateUpdateMobileSettings(std::string tankId, bool conductivity, bool feed, bool light, bool pH, bool pump, bool temperature);
	void appendActionLog(std::string tankId, std::string date, std::string desc, std::string type);
	bool checkMobileSettingsUpdated(std::string tankId, std::string date);
	void updateTankDetailsSensorData(std::string tankId, std::string pasword);
	void updateFeedSensorData(std::string tankId, int totalFeeds, int feedHr, int feedMin);
	void updateTemperatureSensorData(std::string tankId, int currentTemp);
	void updateSensorSensorData(std::string tankId, int conductivity, float pHcurrent);

	~FishyJavaVirtualMachine();
};