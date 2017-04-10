#ifndef _PINS_
#define _PINS_

//wiringPi
const int LIGHT_PIN = 26;
const int FAN_PIN = 19;
const int HEATER_PIN = 13;

//arduPi
const int IN_PUMP_PIN = 3;
const int OUT_PUMP_PIN = 2;
const int WATER_LEVEL_SENSOR_PIN = 7;
const int FEEDER_PIN = 13;

#define point_1_cond 40000   // Write here your EC calibration value of the solution 1 in µS/cm
#define point_1_cal 50       // Write here your EC value measured in resistance with solution 1
#define point_2_cond 10500   // Write here your EC calibration value of the solution 2 in µS/cm
#define point_2_cal 120      // Write here your EC value measured in resistance with solution 2

#define calibration_point_4 2153  //Write here your measured value in mV of pH 4
#define calibration_point_7 1972  //Write here your measured value in mV of pH 7
#define calibration_point_10 1816 //Write here your measured value in mV of pH 10

#define FILTER_SAMPLES 7

#define pHLevelPin 1
#define pHPowerPin 8
#define ecLevelPin 0
#define ecPowerPin 9

#endif
