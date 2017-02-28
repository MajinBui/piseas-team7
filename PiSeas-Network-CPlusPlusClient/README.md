## General Prerequisites
	- server code exported to jar, source and target at 1.6
	- Java SDK and JRE installed
	
## Windows
### Prerequisites:
	- JRE set to path; i.e. <JRE-PATH>/bin/server
	- JDK set to path; i.e. <JDK-DIR-PATH>/bin
	- Must include <JDK-DIR>/include and <JDK-DIR>/include/win32 when compiling
	- Must add <JDK-DIR>/lib/jvm.lib to the linker
Detailed guide: https://www.codeproject.com/Articles/993067/Calling-Java-from-Cplusplus-with-JNI
	
## Linux set up
### Prerequisites:
	- JRE and JDK installed run time installed (should already be done on Rasbian)
	- Add these lines to .bashrc in the home (~):
		-export JAVA_HOME="/usr/lib/jvm/java-1.8.0-openjdk-armhf"
		-export PATH=$PATH:$JAVA_HOME/bin
	- After adding to .bashrc for the first time, execute the above lines in the terminal as well.
	
### Example compile line:
	g++ main.cpp FishyJavaVirtualMachine.cpp -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -std=c++0x -L$JAVA_HOME/jre/lib/arm/server/ -ljvm
		
