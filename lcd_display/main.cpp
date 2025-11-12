#include "mbed.h"
#include "Display.h"

// main() runs in its own thread in the OS
int main()
{
    Display lcd(D14, D15);
    lcd.setRGB(255, 0, 0);
}

