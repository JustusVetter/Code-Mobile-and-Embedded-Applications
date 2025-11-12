/*
    Author: Justus, Dávid
    Date: 10.11.2025
*/

#include "SoundSensor.h"
#include "mbed.h"
#include <cmath>

SoundSensor::SoundSensor(PinName pin): _pin(pin) {

}

float SoundSensor::read(){

    float spl = 0;  
        for (int i = 0; i <_measures; i++) {
            float input = _pin.read();
            // soundsensor only works with 5 Volt
            float input_volt = 5.0f * input;
            float nspl = (94 + (20 * log10f(input_volt / _sensitivity)));
            if(nspl > 0){
            spl = spl + nspl;
            //ThisThread::sleep_for();
            }
        }

        spl = spl / _measures;
    return spl;
}

void SoundSensor::ioctl_set_measures(int measures){
    _measures = measures;
}

// sensitivity in Volt / Pascal not in db have to be calculated manually 
void SoundSensor::ioctl_set_sensitivity(float sensitivity){
    _sensitivity = sensitivity;
}
