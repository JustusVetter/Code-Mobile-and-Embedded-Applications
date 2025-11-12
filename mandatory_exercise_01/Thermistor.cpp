#include "Thermistor.h"
#include "ThisThread.h"
#include "mbed.h"
#include <cmath>

Thermistor::Thermistor(PinName pin) : _pin(pin){
    
}

float Thermistor::read(){
    ThisThread::sleep_for(_defaultDelay);
    float f =  _pin.read();
    
    float rTherm = _rb * ((1.0 / f) - 1);
    
    float tKelvin = (1.0 / ((log(rTherm/_rz) / _beta) + (1.0/_tz)));
    
    float result = 1;
    if (_enableKelvin) {
        result = tKelvin;
    }else {
        result = tKelvin - KONVERT_KELVIN;
    }
    return result;
}


void Thermistor::ioctl_enableKelvin(bool enableKelvin){
    _enableKelvin = enableKelvin;
}