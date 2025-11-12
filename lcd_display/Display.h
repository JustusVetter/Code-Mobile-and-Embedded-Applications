#ifndef MBED_DISPLAY_H
#define MBED_DISPLAY_H

#include "mbed.h"
#include "Grove_LCD_RGB_Backlight.h"

class Display{
    public: 
    Display(PinName sda, PinName scl);

    void setRGB(char r, char g, char b);

    void clear();

    void write(char data1);

    void writech(char data2);

    void print(char *str);

    void locate(int col, int row);

    private:
        Grove_LCD_RGB_Backlight lcd;
};
#endif