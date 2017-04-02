#include  "PH.h"

PH::PH(float min, float max, bool regulate) {
	phSettings.setData(min, max, regulate);
}

float PH::getMin() {
	phSettings.getMin();
}

float PH::getMax() {
	phSettings.getMax();
}

bool PH::getAutoRegulate() {
	phSettings.getAutoRegulate();
}

static float getConductivity() {
	//TO:DO -> need to get current PH
}

static void regulate(PH, bool) {
	;
}