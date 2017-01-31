#include "FishyJavaVitualMachine.h"
#include <iostream>
int main()
{
	FishyJavaVitualMachine vm = FishyJavaVitualMachine();
	vm.writeToPiData("QWERT", "input/QWERT_pi.xml");
	vm.writeToMobileData("QWERT", "input/QWERT_mobile.xml");
	vm.retrieveServerData("QWERT", "output/", "output/");
	std::cin.get();
}
