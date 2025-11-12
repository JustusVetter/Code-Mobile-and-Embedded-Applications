/*
    Author: Justus, Dávid
    Date: 10.11.2025
*/

#ifndef MBED_SOUNDSENSOR_H
#define MBED_SOUNDSENSOR_H
#include "AnalogIn.h"
#include "mbed.h"

class SoundSensor
{
    public:
    SoundSensor(PinName pin);
    float read();

    // The amount of measures done directly after each other and combined to one mesure
    void ioctl_set_measures(int measures);

    //sensitivity in Volt / Pascal
    void ioctl_set_sensitivity(float sensitivity);

    private:
    AnalogIn _pin;
    int _measures = 150;
    float _sensitivity = 0.0316;
};

#endif