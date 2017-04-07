#include  "Conductivity.h"

Conductivity::Conductivity(float min, float max, bool regulate) {
	condSettings.setData(min, max, regulate);
}

float Conductivity::getMin() {
	return condSettings.getMin();
}

float Conductivity::getMax() {
	return condSettings.getMax();
}

bool Conductivity::getAutoRegulate() {
	return condSettings.getAutoRegulate();
}

void Conductivity::setMin(float min_) {
	condSettings.setMin(min_);
}

void Conductivity::setMax(float max_) {
	condSettings.setMax(max_);
}

void Conductivity::setAutoRegulate(bool regulate_) {
	condSettings.setAutoRegulate(regulate_);
}

static float getConductivity() {
	//TO:DO -> need to get current conductivity
}

static void regulate(Conductivity, bool) {
	;
}