#include "Pins.h"
#include "PH.h"
#include "../libraries/arduPi.h"

float readpH() {
	float value_array[FILTER_SAMPLES];
	pinMode(ecPowerPin,OUTPUT);
	digitalWrite(ecPowerPin,LOW);
	delay(100);
	pinMode(pHPowerPin,OUTPUT);

	digitalWrite(pHPowerPin,HIGH);
	delay(100);

	// Take some measurements to filter the signal noise and glitches
	for(int i = 0; i < FILTER_SAMPLES; i++)
	{
		//Read from the ADC channel selected
		value_array[i] = analogRead(pHLevelPin);
	}

	delay(100);

	float pH = median(value_array, FILTER_SAMPLES);
	digitalWrite(pHPowerPin,LOW);

	pinMode(pHPowerPin,INPUT);
	delay(100);

	return (pH*5000.0)/1024.0;
}

PH::PH() {
	Serial.begin(115200);
	Wire.begin();
}

PH::PH(float min, float max, bool autoReg) {
	pHSettings.setData(min, max, autoReg);
}

void PH::setPH(float minTemp, float maxTemp, bool autoReg) {
	pHSettings.setData(minTemp, maxTemp, autoReg);
}

float PH::getMin() {
	return pHSettings.getMin();
}

float PH::getMax() {
	return pHSettings.getMax();
}

bool PH::getAutoRegulate() {
	return pHSettings.getAutoRegulate();
}

float PH::getPH(void) {
	int _mvpH = readpH();
	float sensitivity;
	// Two ranges calibration
	if (_mvpH > calibration_point_7 ) {
		// The sensitivity is calculated using the other two calibration values
		// Asumme that the pH sensor is lineal in the range.
		// sensitivity = pHVariation / volts
		// Divide by 3 = (pH) 7 - (pH) 4
		sensitivity = (calibration_point_4-calibration_point_7)/3;

		// The value at pH 7.0 is taken as reference
		// => Units balance => 7 (pH) + (volts) / ((pH) / (volts))
		return 7.0 + (calibration_point_7-_mvpH) / sensitivity;
		// | |
		// (pH 7 voltage - Measured volts) = Variation from the reference
	}
	else {
		// The sensitivity is calculated using the other two calibration values
		sensitivity = (calibration_point_7-calibration_point_10) / 3;

		return 7.0 + (calibration_point_7-_mvpH)/sensitivity;
	}
}
