#include "FishyJavaVitualMachine.h"
#include <iostream>

FishyJavaVitualMachine::FishyJavaVitualMachine() {
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

FishyJavaVitualMachine::FishyJavaVitualMachine(std::string classpath) {
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

bool FishyJavaVitualMachine::isJVMRunning() {
	return rc_JVM == JNI_OK;
}

bool FishyJavaVitualMachine::isFishyClientRunning()
{
	return (fishyClient != nullptr);
}

/**
* Updates the pi device server data with the xml given according to id
* @param tankId the TankID of the tank that needs updating
* @param mobileXMLSavePath path to the xml file to send
*/
void FishyJavaVitualMachine::writeToPiData(std::string tankId, std::string piXMLSavePath) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "writeToPiData", "(Ljava/lang/String;Ljava/lang/String;)V");
	if (mid == nullptr)
		std::cerr << "ERROR: method void writeToPiData() not found !" << std::endl;
	else {
		env->CallStaticVoidMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF((char *)piXMLSavePath.c_str()));                      // call method
		std::cout << std::endl;
	}
}

/**
* Updates the mobile device server data with the xml given according to id
* @param tankId the TankID of the tank that needs updating
* @param mobileXMLSavePath path to the xml file to send
*/
void FishyJavaVitualMachine::writeToMobileData(std::string tankId, std::string mobileXMLSavePath) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "writeToMobileData", "(Ljava/lang/String;Ljava/lang/String;)V");
	if (mid == nullptr)
		std::cerr << "ERROR: method void writeToMobileData() not found !" << std::endl;
	else {
		env->CallStaticVoidMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF((char *)mobileXMLSavePath.c_str()));                      // call method
		std::cout << std::endl;
	}
}
/**
* Updates the server data with the xml given according to id
* @param tankId the TankID of the tank that needs updating
* @param command directive the server will perform
* @param mobileXMLSavePath path to the xml file to send
*/
void FishyJavaVitualMachine::retrieveServerData(std::string tankId, std::string mobileXMLSavePath, std::string piXMLSavePath) {
	if (!isFishyClientRunning()) {
		std::cerr << "FishyClient is not active." << std::endl;
		return;
	}
	// Find method
	jmethodID mid = env->GetStaticMethodID(fishyClient, "retrieveServerData", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	if (mid == nullptr)
		std::cerr << "ERROR: method void retrieveServerData() not found !" << std::endl;
	else {
		std::cout << tankId.c_str() << " " << mobileXMLSavePath.c_str() << " " << piXMLSavePath.c_str() << std::endl;
		env->CallStaticVoidMethod(fishyClient, mid, env->NewStringUTF((char *)tankId.c_str()), env->NewStringUTF((char *)mobileXMLSavePath.c_str()), env->NewStringUTF((char *)piXMLSavePath.c_str()));    // call method
		std::cout << std::endl;
	}
}

FishyJavaVitualMachine::~FishyJavaVitualMachine() {
	jvm->DestroyJavaVM();
}

