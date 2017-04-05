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

void PH::setMin(float min_) {
	phSettings.setMin(min_);
}

void PH::setMax(float max_) {
	phSettings.setMax(max_);
}

void PH::setAutoRegulate(bool regulate_) {
	phSettings.setAutoRegulate(regulate_);
}

static float getConductivity() {
	//TO:DO -> need to get current PH
}

static void regulate(PH, bool) {
	;
}