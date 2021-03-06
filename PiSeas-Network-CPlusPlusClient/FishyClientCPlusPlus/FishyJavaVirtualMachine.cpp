#include "FishyJavaVirtualMachine.h"
#include <iostream>


FishyJavaVirtualMachine::FishyJavaVirtualMachine() {
	JavaVMInitArgs vm_args;                        // Initialization arguments
	JavaVMOption* options = new JavaVMOption[1];   // JVM invocation options
	options[0].optionString = (char*)"-Djava.class.path=fishyServer.jar";   // where to find java .class
	vm_args.version = JNI_VERSION_1_6;             // minimum Java version
	vm_args.nOptions = 1;                          // number of options
	vm_args.options = options;
	vm_args.ignoreUnrecognized = false;
	rc_JVM = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
	delete options;    // we then no longer need the initialisation options. 
	if (isJVMRunning()) {
		std::cout << "JVM load succeeded: Version ";
		jint ver = env->GetVersion();
		std::cout << ((ver >> 16) & 0x0f) << "." << (ver & 0x0f) << std::endl;
		fishyClient = env->FindClass("piseas/network/FishyClient");  // try to find the class
		if (fishyClient == nullptr) {
			if (env->ExceptionOccurred())
				env->ExceptionDescribe();
			else
				std::cout << "fishyClient is null but no exception was thrown.  Most likely jar file not found..." << std::endl;
		}
		else {
			std::cout << "FishyJVM successfully running" << std::endl;
		}
	}
	else {
		std::cerr << "JVM not running...";
	}
}

FishyJavaVirtualMachine::FishyJavaVirtualMachine(std::string classpath) {
	JavaVMInitArgs vm_args;                        // Initialization arguments
	JavaVMOption* options = new JavaVMOption[1];   // JVM invocation options
	options[0].optionString = (char*)std::string("-Djava.class.path=" + classpath).c_str();   // where to find java .class
	vm_args.version = JNI_VERSION_1_6;             // minimum Java version
	vm_args.nOptions = 1;                          // number of options
	vm_args.options = options;
	vm_args.ignoreUnrecognized = false;
	rc_JVM = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
	delete options;    // we then no longer need the initialisation options. 
	if (isJVMRunning()) {
		std::cout << "JVM load succeeded: Version ";
		jint ver = env->GetVersion();
		std::cout << ((ver >> 16) & 0x0f) << "." << (ver & 0x0f) << std::endl;
		fishyClient = env->FindClass("piseas/network/FishyClient");  // try to find the class
		if (fishyClient == nullptr) {
			if (env->ExceptionOccurred())
				env->ExceptionDescribe();
			else
				std::cout << "fishyClient is null but no exception was thrown.  Most likely jar file not found..." << std::endl;
		}
		else {
			std::cout << "FishyJVM successfully running" << std::endl;
		}
	}
	else {
		std::cerr << "JVM not running...";
	}
}

bool FishyJavaVirtualMachine::isJVMRunning() {
	return rc_JVM == JNI_OK;
}

bool FishyJavaVirtualMachine::isFishyClientRunning()
{
	return (fishyClient != nullptr);
}


/**
* Client will send the sensor data with the given tankId located to the specified file path. 
* @param tankId the TankID of the tank
* @param parentFilePath the parent path of the saved data
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::sendMobileXmlData(std::string tankId, std::string parentPath) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "sendMobileXmlData", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void writeToPiData() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF((char *)parentPath.c_str()));                      // call method
		return (bool)rc;
	}
}

/**
* Client will send the sensor data with the given tankId located to the specified file path.
* @param tankId the TankID of the tank
* @param parentFilePath the parent path of the saved data
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::sendSensorData(std::string tankId, std::string parentPath) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "sendSensorData", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void writeToMobileData() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF((char *)parentPath.c_str()));                      // call method
		return (bool)rc;
	}
}


/**
* Client will send the action log with the given tankId located to the specified file path.
* @param tankId the TankID of the tank
* @param parentFilePath the parent path of the saved data
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::sendActionLog(std::string tankId, std::string parentPath) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "sendActionLog", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void sendActionLog() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF((char *)parentPath.c_str()));                      // call method
		return (bool)rc;
	}
}

bool FishyJavaVirtualMachine::retrieveMobileXmlData(std::string tankId, std::string parentPath)
{
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "retrieveMobileXmlData", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void retrieveMobileXmlData() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF((char *)parentPath.c_str()));                      // call method
		return (bool)rc;
	}
}

bool FishyJavaVirtualMachine::retrieveSensorData(std::string tankId, std::string parentPath)
{
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "retrieveSensorData", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void retrieveSensorData() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF((char *)parentPath.c_str()));                      // call method
		return (bool)rc;
	}
}

bool FishyJavaVirtualMachine::retrieveActionLog(std::string tankId, std::string parentPath)
{
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "retrieveActionLog", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void retrieveActionLog() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF((char *)parentPath.c_str()));                      // call method
		return (bool)rc;
	}
}


/**
* Client will send the action log with the given tankId located to the specified file path.
* @param tankId the TankID of the tank
* @param parentFilePath the parent path of the saved data
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::updateTemperatureRange(std::string tankId, float min, float max) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "updateTemperatureRange", "(Ljava/lang/String;FF)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void updateTemperatureRange() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), min, max);  // call method
		return (bool)rc;
	}
}

/**
* Sets the light xml data on the server using the given arguments.  Each element in the arrays
* are associated to each other 1 to 1 for each details row.
*
* Example of how to use the following function:
* <pre>
* {@code
*
* int onHr[2] = { 12, 30 };
* int onMin[2] = { 12, 00 };
* 
* int offHr[2] = { 12, 18 };
* int offMin[2] = { 30, 30 };
* }
*
* FishyClient.setLighting(tankID2, onHr, onMin, offHr, offMin, true, false);
* </pre>
*
* This will update the xml to contain the following information:
*
* <pre>
* {@code
* <Light auto="true" manual="false" schedules="2">
* 		<details offHr="12" offMin="30" onHr="12" onMin="12"/>
* 		<details offHr="18" offMin="30" onHr="30" onMin="0"/>
* </Light>
* }
* </pre>
*
*
* @param tankId the TankID of the tank that needs updating
* @param onHr an array of hours to turn on
* @param onMin an array of mins to turn on
* @param offHr an array of hours to turn off
* @param offMin an array of mins to turn off
* @param autoLight bool to save
* @param manualLight bool to save
* @param n the amount of schedules to send
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::setLighting(std::string tankId, int* onHr, int* onMin, int* offHr, int* offMin, bool autoLight, bool manualLight, int n) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "setLighting", "(Ljava/lang/String;[I[I[I[IZZ)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void setLighting() not found !" << std::endl;
	}
	else {
		jintArray onHours = env->NewIntArray(n);
		jintArray onMins = env->NewIntArray(n);
		jintArray offHours = env->NewIntArray(n);
		jintArray offMins = env->NewIntArray(n);
		jint *onHoursBody = env->GetIntArrayElements(onHours, 0);
		jint *onMinsBody = env->GetIntArrayElements(onHours, 0);
		jint *offHoursBody = env->GetIntArrayElements(onHours, 0);
		jint *offMinsBody = env->GetIntArrayElements(onHours, 0);
		for (int i = 0; i < n; i++) {
			onHoursBody[i] = onHr[i];
			onMinsBody[i] = onMin[i];
			offHoursBody[i] = offHr[i];
			offMinsBody[i] = offMin[i];
		}
		env->ReleaseIntArrayElements(onHours, onHoursBody, 0);
		env->ReleaseIntArrayElements(onMins, onMinsBody, 0);
		env->ReleaseIntArrayElements(offHours, offHoursBody, 0);
		env->ReleaseIntArrayElements(offMins, offMinsBody, 0);

		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), onHours, onMins, offHours, offMins, autoLight, manualLight);  // call method
		return (bool)rc;
	}
}

/**
* Sets the Feed xml data on the server using the given arguments.  Each row of a week is
* associated to an row of hour and minute.
*
* Example of how to use the following function:
* <pre>
* {@code
* bool weekArr[2][7] = {
*  		{ true, false, true, true, true, false, false },
*  		{ false, true, false, true, false, false, false }
* };
*
* int hour[2] = { 13, 07 };
* int minute[2] = { 50, 30 };
*
* }
*
* FishyClient.setFeeding(tankID2, weekArr, hour, minute, offMin, true, false);
* </pre>
*
* This will update the xml to contain the following information:
*
* <pre>
* {@code
* <Feed auto="true" manual="false" schedules="2">
* 		<details Fri="true" Mon="true" Sat="false" Sun="false" Thu="true" Tue="false" Wed="true" hr="13" min="50"/>
* 		<details Fri="false" Mon="false" Sat="false" Sun="false" Thu="true" Tue="true" Wed="false" hr="7" min="30"/>
* </Feed>
* }
* </pre>
*
* @param tankId the TankID of the tank
* @param weekArr days of the week to feed
* @param hour hour of the day to feed
* @param minute minute of the hour to feed
* @param auto set automatic feeding
* @param manual send manual feeding
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::setFeeding(std::string tankId, bool* weekArr, int numberOfRows, int* hour, int* minute, bool autoF, bool manualF) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "setFeeding", "(Ljava/lang/String;[I[I[I[IZZ)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void setFeeding() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), weekArr, hour, minute, autoF, manualF);  // call method
		return (bool)rc;
	}
}

/**
* Sets the light xml data on the server using the given arguments.  Each element in the arrays
* are associated to each other 1 to 1 for each details row.
*
* Example of how to use the following function:
* <pre>
* {@code
*
* int onHr[2] = { 12, 30 };
* int onMin[2] = { 12, 00 };
*
* int offHr[2] = { 12, 18 };
* int offMin[2] = { 30, 30 };
* }
*
* FishyClient.setLighting(tankID2, onHr, onMin, offHr, offMin, true, false);
* </pre>
*
* This will update the xml to contain the following information:
*
* <pre>
* {@code
* <Light auto="true" manual="false" schedules="2">
* 		<details offHr="12" offMin="30" onHr="12" onMin="12"/>
* 		<details offHr="18" offMin="30" onHr="30" onMin="0"/>
* </Light>
* }
* </pre>
*
*
* @param tankId the TankID of the tank that needs updating
* @param onHr an array of hours to turn on
* @param onMin an array of mins to turn on
* @param offHr an array of hours to turn off
* @param offMin an array of mins to turn off
* @param autoLight bool to save
* @param manualLight bool to save
* @param n the amount of schedules to send
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::appendActionLog(std::string tankId, std::vector<std::string> date, std::vector<std::string> desc, std::vector<std::string> type) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "appendActionLog", "(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void appendActionLog() not found !" << std::endl;
	}
	else {
		jobjectArray jDateArr = env->NewObjectArray(date.size(),
			env->FindClass("java/lang/String"),
			env->NewStringUTF(""));
		jobjectArray jDescArr = env->NewObjectArray(desc.size(),
			env->FindClass("java/lang/String"),
			env->NewStringUTF(""));
		jobjectArray jTypeArr = env->NewObjectArray(type.size(),
			env->FindClass("java/lang/String"),
			env->NewStringUTF(""));
		for (int i = 0; i < date.size(); i++) {
			env->SetObjectArrayElement(jDateArr, i, env->NewStringUTF(date.at(i).c_str()));
			env->SetObjectArrayElement(jDescArr, i, env->NewStringUTF(desc.at(i).c_str()));
			env->SetObjectArrayElement(jTypeArr, i, env->NewStringUTF(type.at(i).c_str()));
		}

		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), jDateArr, jDescArr, jTypeArr);  // call method
		return (bool)rc;
	}
}

/**
* Updates the manual attribute in the mobile settings xml.
* @param tankId tankId the tankId
* @param manualFeed true or false if changed
* @param manualLight true or false if changed
* @param manualDrain true or false if changed
* @param manualFill true or false if changed
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::updateManualCommands(std::string tankId, bool manualFeed, bool manualLight, bool manualDrain, bool manualFill) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "updateManualCommands", "(Ljava/lang/String;ZZZZ)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void updateManualCommands() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), manualFeed, manualLight, manualDrain, manualFill);  // call method
		return (bool)rc;
	}
}

/**
* Set the updated field to the specified value.  For the Piseas device to mark
* updated and processed changes.
* @param tankId the tankId
* @param conductivity true or false if changed
* @param feed true or false if changed
* @param light true or false if changed
* @param pH true or false if changed
* @param pump true or false if changed
* @param temperature true or false if changed
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::updateUpdateMobileSettings(std::string tankId, bool conductivity, bool feed, bool light, bool pH, bool pump, bool temperature) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "updateUpdateMobileSettings", "(Ljava/lang/String;ZZZZZZ)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void updateUpdateMobileSettings() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), conductivity, feed, light, pH, pump, temperature);  // call method
		return (bool)rc;
	}
}

/**
* Append a single line to the log xml.
* @param tankId the tankId
* @param date the date of the log
* @param desc the desc enum
* @param type the type enum
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::appendActionLog(std::string tankId, std::string date, std::string desc, std::string type) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "appendActionLog", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void appendActionLog() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF(date.c_str()), env->NewStringUTF(desc.c_str()), env->NewStringUTF(type.c_str()));  // call method
		return (bool)rc;
	}
}

/**
* Compares the given date to the date the server has on the mobile settings xml.
* @param tankId the tankId
* @param date the date to compare
* @return true if the dates are the same, false otherwise
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::checkMobileSettingsUpdated(std::string tankId, std::string date) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "checkMobileSettingsUpdated", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void checkMobileSettingsUpdated() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF(date.c_str()));  // call method
		return (bool)rc;
	}
	return false;
}

/**
* Update the tank details of the sensor data xml.
* @param tankId the tankId
* @param password the updated pin
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::updateTankDetailsSensorData(std::string tankId, std::string pasword) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "updateTankDetailsSensorData", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void updateTankDetailsSensorData() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF((char *)pasword.c_str()));  // call method
		return (bool)rc;
	}
}


/**
* Update the feed data of the sensor xml.
* @param tankId the tankId
* @param feedHr the updated last fed hour
* @param feedMin the updated last fed minute of the hour
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::updateFeedSensorData(std::string tankId, int totalFeeds, int feedHr, int feedMin) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "updateFeedSensorData", "(Ljava/lang/String;III)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void updateFeedSensorData() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), totalFeeds, feedHr, feedMin);  // call method
		return (bool)rc;
	}
}


/**
* Update the temperature data of the sensor xml.
* @param tankId the tankId
* @param currentTemp the updated temperature
* @return true if successful, false otherwise
*/
bool FishyJavaVirtualMachine::updateTemperatureSensorData(std::string tankId, int currentTemp) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "updateTemperatureSensorData", "(Ljava/lang/String;I)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void updateTemperatureSensorData() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), currentTemp);  // call method
		return (bool)rc;
	}
}

/**
* Update the sensor data of the sensor xml.
* @param tankId the tankId
* @param conductivity the updated conductivity value
* @param pHcurrent the updated ph value
*/
bool FishyJavaVirtualMachine::updateSensorSensorData(std::string tankId, int conductivity, float pHcurrent) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return false;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "updateSensorSensorData", "(Ljava/lang/String;IF)Z");
	if (mid == nullptr) {
		if (env->ExceptionOccurred())
			env->ExceptionDescribe();
		else
			std::cerr << "ERROR: method void updateSensorSensorData() not found !" << std::endl;
	}
	else {
		jboolean rc = env->CallStaticBooleanMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), conductivity, pHcurrent);  // call method
		return (bool)rc;
	}
}


FishyJavaVirtualMachine::~FishyJavaVirtualMachine() {
	jvm->DestroyJavaVM();
}

