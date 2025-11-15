/*
    Author: Dávid, Justus
    Date: 15.11.25
*/

#ifndef BOARD_LED_H
#define BOARD_LED_H
#include "DigitalInOut.h"
#include "DigitalOut.h"
#include "PinNames.h"
#include "mbed.h"



class BoardLEDs{
    public:
    BoardLEDs();

    // is taking numbers from 0 - 7 and display this pattern in binary over all LEDS
    void write_pattern(int pattern);

    // is lighten or unlighten one specific Led
    void write_change_LED(int led_number, bool state);

    // output 1 if LED "led_number" is on and 0 if it is of
    int read_LED_state(int led_number);

    private:
    DigitalOut _green;
    DigitalOut _blue;
    DigitalOut _red;

};


#endif