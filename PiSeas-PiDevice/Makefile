CC=g++
JAVA_INC= -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -L$(LIBPATH) -ljvm
CFLAGS= -std=c++0x -pthread -lwiringPi

piseas:
	$(CC) $(CFLAGS) $(JAVA_INC) *.cpp -o piseas.out
