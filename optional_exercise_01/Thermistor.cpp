#include "Thermistor.h"
#include "ThisThread.h"
#include "mbed.h"

Thermistor::Thermistor(PinName pin) : _pin(pin){
    _pin = 0;
}

float Thermistor::read(){
    ThisThread::sleep_for(_defaultDelay);
    float f =  _pin.read();
    float rTherm = _rb * ((1.0 / f) - 1);
    return 0.00;
}

void Thermistor::ioctl_changeVoltage(float rb){
    _rb = rb;
}

void Thermistor::ioctl_defaultDelay(int defaultDelay){
    _defaultDelay=defaultDelay;
}

void Thermistor::ioctl_enableKelvin(bool enableKelvin){
    _enableKelvin = enableKelvin;
}