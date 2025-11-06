#include "LightSensor.h"
#include "ThisThread.h"
#include "mbed.h"

LightSensor::LightSensor(PinName pin): _pin(pin){

}

float LightSensor::read(){
    //response time between 20 and 30ms => 50ms in this library
    ThisThread::sleep_for(50ms);
    float inward = _pin.read();

    float first = (_vRef * _luxRel) * (inward * 3.3);
    float second = first - _luxRel;
    
    float lux = second/_rL;
    if (!_enableKiloLux) {
        lux = lux * 1000;
    }
    return lux;
}
void LightSensor::ioctl_kilolux(bool enableLux){
    _enableKiloLux = enableLux;
}

//old sensors might have different luxrel from 500
// => method for balance it 
void LightSensor::ioctl_refine_luxrel(float newLuxRel){
    _luxRel = newLuxRel;
}