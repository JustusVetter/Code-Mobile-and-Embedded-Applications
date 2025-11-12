#ifndef GROVE_LCD_RGB_BACKLIGHT_H
#define GROVE_LCD_RGB_BACKLIGHT_H

#include "mbed.h"

class Grove_LCD_RGB_Backlight {
public:
    Grove_LCD_RGB_Backlight(PinName sda, PinName scl);
    void setRGB(char r, char g, char b);     
    void clear(void);                          
    void write(char data);                     
    void print(const char *str);              
    void locate(int column, int row);          

private:
    I2C i2c;
};

#endif
