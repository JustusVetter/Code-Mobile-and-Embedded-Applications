#include "mbed.h"
#include "Grove_LCD_RGB_Backlight.h"
#include <cstdio>

Grove_LCD_RGB_Backlight lcd(D14, D15); 

// main() runs in its own thread in the OS
int main()
{
    
    while(true){
        lcd.writech('f');
        ThisThread::sleep_for(2s);
    }
}

