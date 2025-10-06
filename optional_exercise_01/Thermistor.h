/*
Author: David, Justus
Date: 04.10.2025
*/
#ifndef MBED_THERMISTOR_H
#define MBED_THERMISTOR_H

#include "DigitalOut.h"
#include "mbed.h"

class Thermistor {
public:
 Thermistor(PinName pin);
 float read();
 void ioctl_setVoltage(float rb);
 
 // default is Celsius
 void ioctl_enableKelvin(bool enableKelvin);
 
 // default delay is 500 
 void ioctl_defaultDelay(int defaultDelay);

private:
 DigitalOut _pin;
 int _beta = 4250;
 float _rb = 3.3;
 int _rz = 100000;
 bool _enableKelvin = false;
 float _tz = 298.15;
 const float KONVERT_KELVIN = 273.15;

 int _defaultDelay = 500; 
};

#endif