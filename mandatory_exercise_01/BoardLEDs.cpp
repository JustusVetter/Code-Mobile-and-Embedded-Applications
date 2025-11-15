
#include "BoardLEDs.h"

BoardLEDs::BoardLEDs()
    : _green(LED1), _blue(LED2), _red(LED3)
{

}

void BoardLEDs::write_pattern(int pattern){
    _green = pattern & 1;
    _blue = pattern >> 1 & 1;
    _red = pattern >> 2 & 1;
}

int BoardLEDs::read_LED_state(int led_number) {
    switch (led_number) {
    case 0:
        return _green;
    break;
    case 1:
        return _blue;
    break;
    case 2:
        return _red;
    break;
    default:
        return 0;
    }
}
