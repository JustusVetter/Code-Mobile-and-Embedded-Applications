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
 void ioctl_changeVoltage(float rb);
 
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
 
 int _defaultDelay = 500; 
};

#endif