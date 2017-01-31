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
	- JRE and JDK installed run time installed
	- Add jvm to PATH: export LD_LIBRARY_PATH=/usr/lib/jvm/default-java/jre/lib/amd64:/usr/lib/jvm/default-java/jre/lib/amd64/server
### Example compile line:
	g++ main.cpp -I/usr/lib/jvm/java-7-openjdk-amd64/include -std=c++0x -L/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/amd64/server -ljvm
		
