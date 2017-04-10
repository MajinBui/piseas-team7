#include "Pins.h"
#include "Conductivity.h"
#include "../libraries/arduPi.h"

float calculateResistanceEC(void) {

	float value_array[FILTER_SAMPLES];
	// Take some measurements to filter the signal noise and glitches
	for(int i = 0; i < FILTER_SAMPLES; i++)
	{
		//Read from the ADC channel selected
		value_array[i] = analogRead(ecLevelPin);
	}

	delay(100);

	float EC = median(value_array,FILTER_SAMPLES);
	float resistance;

	if(EC <= 11){
		resistance = EC/0.050;
	}
	else if((EC > 11) && (EC <= 23)){
		resistance = ((EC-0.44)/0.0480);
	}
	else if((EC > 23) && (EC <= 47)){
		resistance = ((EC-1.7170)/0.0453);
	}
	else if((EC > 47) && (EC <= 94)){
		resistance = ((EC-7.8333)/0.0392);
	}
	else if((EC > 94) && (EC <= 162)){
		resistance = ((EC-34.16)/0.0272);
	}
	else if((EC > 162) && (EC <= 186)){
		resistance = ((EC-36.667)/0.0267);
	}
	else if((EC > 186) && (EC <= 259)){
		resistance = ((EC-93.0909)/0.0161);
	}
	else if((EC > 259) && (EC <= 310)){
		resistance = ((EC-157.0)/0.0102);
	}
	else if((EC > 310) && (EC <= 358)){
		resistance = ((EC-207.1429)/0.0069);
	}
	else if((EC > 358) && (EC <= 401)){
		resistance = ((EC-272.0)/0.0039);
	}
	else if((EC > 401) && (EC <= 432)){
		resistance = ((EC-327.9286)/0.0022);
	}
	else if((EC > 432) && (EC <= 458)){
		resistance = ((EC-373.8095)/0.0012);
	}
	else if((EC > 458) && (EC <= 479)){
		resistance = ((EC-413.3750)/0.0007);
	}
	else if((EC > 479) && (EC <= 525)){
		resistance = ((EC-473.8889)/0.0001);
	}
	else if(EC > 525){
		resistance = ((EC-525.7778)/0.00001);
	}

	return resistance;
}

Conductivity::Conductivity() {
	Serial.begin(115200);
	Wire.begin();
}

Conductivity::Conductivity(float min, float max, bool autoReg) {
	conductivitySettings.setData(min, max, autoReg);
}

void Conductivity::setConductivityData(float minTemp, float maxTemp, bool autoReg) {
	conductivitySettings.setData(minTemp, maxTemp, autoReg);
}

float Conductivity::getMin() {
	return conductivitySettings.getMin();
}

float Conductivity::getMax() {
	return conductivitySettings.getMax();
}

bool Conductivity::getAutoRegulate() {
	return conductivitySettings.getAutoRegulate();
}

float Conductivity::getConductivity(void) {
	//Check if sensorPowerPin is HIGH or LOW because
	//sensorPowerPin and ecPowerPin NEVER must be connected at the same time
	pinMode(ecPowerPin,OUTPUT);
	pinMode(pHPowerPin,OUTPUT);
	digitalWrite(pHPowerPin,LOW);
	delay(100);
	digitalWrite(ecPowerPin,HIGH);
	delay(100);
	float ecmeasure = calculateResistanceEC();
	digitalWrite(ecPowerPin, LOW);
	digitalWrite(pHPowerPin, HIGH);
		
	pinMode(pHPowerPin,INPUT);
	digitalWrite(ecPowerPin,LOW);
	delay(100);
		
	return ecmeasure;
}
