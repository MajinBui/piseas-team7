CC=g++
JAVA_INC= -I/usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/include -I/usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/include/linux -L/usr/lib/jvm/jdk-8-oracle-arm32-vfp-hflt/jre/lib/arm/server -ljvm
CFLAGS= -std=c++0x -pthread -lwiringPi

piseas: ../libraries/arduPi.o DataRange.o LightSchedule.o TempData.o WaterState.o FeedSchedule.o Conductivity.o PH.o Main.cpp Tank.h Tank.cpp xmlParser.cpp xmlParser.h FishyJavaVirtualMachine.cpp FishyJavaVirtualMachine.h
	$(CC) $(CFLAGS) $(JAVA_INC) Main.cpp Tank.cpp xmlParser.cpp FishyJavaVirtualMachine.cpp *.o ../libraries/arduPi.o -o piseas
	
arduPi.o: ../libraries/arduPi.cpp ../libraries/arduPi.h
	$(CC) $(CFLAGS) -c ../libraries/arduPi.cpp
	
DataRange.o LightAction.o: DataRange.h DataRange.cpp LightAction.h LightAction.cpp
	$(CC) $(CFLAGS) -c DataRange.cpp LightAction.cpp

LightSchedule.o: LightSchedule.h LightSchedule.cpp Pins.h Log.h LightAction.o
	$(CC) $(CFLAGS) -c LightSchedule.cpp
	
TempData.o: TempData.h TempData.cpp Pins.h Log.h DataRange.o
	$(CC) $(CFLAGS) -c TempData.cpp

WaterState.o: WaterState.h WaterState.cpp Pins.h Log.h
	$(CC) $(CFLAGS) -c WaterState.cpp

FeedSchedule.o: FeedSchedule.cpp FeedSchedule.h Pins.h Log.h
	$(CC) $(CFLAGS) -c FeedSchedule.cpp

Conductivity.o: Conductivity.cpp Conductivity.h Pins.h
	$(CC) $(CFLAGS) -c Conductivity.cpp
	
PH.o: PH.cpp PH.h Pins.h
	$(CC) $(CFLAGS) -c PH.cpp

clean:
	rm *.o ../libraries/*.o piseas
