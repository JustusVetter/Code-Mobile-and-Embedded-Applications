/*
Author: David, Justus
Date: 28.01.2026
*/
#ifndef MBED_SEEEDBLUETOOTH_H
#define MBED_SEEEDBLUETOOTH_H
#include "BufferedSerial.h"
#include "PinNames.h"
#include "mbed.h"
#include "AnalogIn.h"


class SeeedBluetooth{
public:
    SeeedBluetooth(PinName pin0,PinName pin1,int rate);
/*
    will call default options
*/
    void write(const char input[]);




private:
    void _init();

    PinName _pin0 = D1;
    PinName _pin1 = D0;
    int _rate = 9600;
    BufferedSerial _bs;
    char _buffer[32];
};

#endif
