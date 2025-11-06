/*
Author: David, Justus
Date: 04.10.2025
*/
#ifndef MBED_THERMISTOR_H
#define MBED_THERMISTOR_H
#include "AnalogIn.h"
#define _defaultDelay 1s

#include "mbed.h"

class Thermistor {
public:
 Thermistor(PinName pin);
 float read();
 
 // default is Celsius
 void ioctl_enableKelvin(bool enableKelvin);
 

private:
 AnalogIn _pin;
 float _beta = 4250.0;
 float _rb = 100000.0;
 float _rz = 100000.0;
 bool _enableKelvin = false;
 float _tz = 298.15;
 const float KONVERT_KELVIN = 273.15;

  
};

#endif