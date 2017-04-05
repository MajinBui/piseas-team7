#include "DataRange.h"

void DataRange::setData(float minimum, float maximum, bool autoReg){
	min = minimum;
	max = maximum;
	autoRegulate = autoReg;
}

float DataRange::getMin(){
	return min;
}

float DataRange::getMax(){
	return max;
}

bool DataRange::getAutoRegulate() {
	return autoRegulate;
}

void DataRange::setMin(float min_) {
	min = min_;
}

void DataRange::setMax(float max_) {
	max = max_;
}

void DataRange::setAutoRegulate(bool autoRegulate_) {
	autoRegulate = autoRegulate_;
}

