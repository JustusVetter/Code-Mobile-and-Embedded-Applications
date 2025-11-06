#include "Thermistor.h"
#include "ThisThread.h"
#include "mbed.h"
#include <cmath>

Thermistor::Thermistor(PinName pin) : _pin(pin){
    
}

float Thermistor::read(){
    ThisThread::sleep_for(_defaultDelay);
    float f =  _pin.read();
    //printf("Read from pin: %f\n",f);
    float rTherm = _rb * ((1.0 / f) - 1);//3.3
    //float rTherm = 100000.0 * ((1.0 / f) - 1);
    float tKelvin = (1.0 / ((log(rTherm/_rz) / _beta) + (1.0/_tz)));//b:4250;tz:298.15;_rz:100000
    //printf("%f\n",rTherm);
    //float tKelvin = (1.0 / ((log(rTherm/100000.0) / 4250.0 )+ (1.0/298.15)));
    float result = 1;
    if (_enableKelvin) {
        result = tKelvin;
    }else {
        result = tKelvin - KONVERT_KELVIN;
    }
    printf("%f\n",tKelvin);
    return result;
}


void Thermistor::ioctl_enableKelvin(bool enableKelvin){
    _enableKelvin = enableKelvin;
}