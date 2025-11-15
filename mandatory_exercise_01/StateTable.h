

#ifndef STATE_TABLE_H
#define STATE_TABLE_H

#include "mbed.h"

typedef enum { 
    display_temp = 0, display_Light = 1, display_Sound = 2, 
    set_Temp_L= 3, set_Temp_H = 4,set_Light_L = 5, 
    set_Light_H= 6, set_Sound = 7 
} tState;

struct stateTableEntry {
    //void *StateFunction;
    tState thisState;
    tState interuptState;
    tState buttonState;
    char sentence[];
};

class StateTable
{
    public:
    StateTable();

    void next();
    void setButton();
    tState getCurrent();

    private:

    bool _isButtonPressed = false;

    struct stateTableEntry _stateTable[8];
    stateTableEntry _current = _stateTable[0];
};

#endif