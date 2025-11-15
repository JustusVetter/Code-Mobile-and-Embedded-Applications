#include "LightSensor.h"
#include "ThisThread.h"
#include "mbed.h"

LightSensor::LightSensor(PinName pin): _pin(pin){

}


float LightSensor::read(){
    //response time between 20 and 30ms => 30ms in this library
    float inward = _pin.read();
    ThisThread::sleep_for(30ms);
    float first = (_vRef * _luxRel) * (inward * _voltage);
    float second = first - _luxRel;
    
    float lux = second/_rL;
    if (!_enableKiloLux) {
        lux = lux * 1000;
    }
    // filter not possible values
    if (lux < 0){
        lux = 0;
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

void LightSensor::ioctl_set_five_volt(bool enableFiveVolt){
    if(enableFiveVolt){
        _voltage= 5;
        _vRef= 5;
    }else {
        _voltage = 3.3;
        _vRef = 3.3;
    }
}