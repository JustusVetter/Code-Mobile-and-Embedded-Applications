/*
    Author: Justus, Dávid
    Date: 2025.11.06
*/

#ifndef MBED_LIGHTSENSOR_H
#define MBED_LIGHTSENSOR_H
#include "AnalogIn.h"
#include "mbed.h"

class LightSensor
{
    public:
        LightSensor(PinName pin);

        float read();

        void ioctl_refine_luxrel(float newLuxRel);

        void ioctl_kilolux(bool enableKiloLux);

        void ioctl_set_five_volt(bool enableFiveVolt);

    private:
        AnalogIn _pin;
        float _rL = 10000;
        float _vRef = 3.3;
        float _luxRel = 500;
        bool _enableKiloLux = false;
        float _voltage = 3.3;
};
#endif