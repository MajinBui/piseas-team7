#pragma once
#include <jni.h>
#include <string>
class FishyJavaVitualMachine {
private:
	JavaVM *jvm; // Pointer to the JVM (Java Virtual Machine)
	JNIEnv *env; // Pointer to native interface
	jint rc_JVM; // Int to check if JVM is active
	jclass fishyClient; // the fishyClient class to call static methods from
public:
	FishyJavaVitualMachine();
	FishyJavaVitualMachine(std::string classpath);
	bool isJVMRunning();
	bool isFishyClientRunning();
	void writeToPiData(std::string tankId, std::string piXMLSavePath);
	void writeToMobileData(std::string tankId, std::string mobileXMLSavePath);
	void retrieveServerData(std::string tankId, std::string mobileXMLSavePath, std::string piXMLSavePath);

	~FishyJavaVitualMachine();
};