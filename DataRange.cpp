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

bool DataRange::getAutoRegulate(){
	return autoRegulate;
}
