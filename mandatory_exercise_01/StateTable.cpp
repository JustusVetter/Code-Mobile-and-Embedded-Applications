/*
Author: Dávid, Justus
Date: 12.11.25
*/

#include "StateTable.h"
#include "mbed.h"


StateTable::StateTable(): 
_stateTable {
        {display_temp,display_Light,set_Temp_L},
        {display_Light,display_Sound,set_Temp_L},
        {display_Sound,display_temp,set_Temp_L},
        {set_Temp_L,set_Temp_L,set_Temp_H},
        {set_Temp_H,set_Temp_H,set_Light_L},
        {set_Light_L,set_Light_L,set_Light_H},
        {set_Light_H,set_Light_H,set_Sound},
        {set_Sound,set_Sound,display_temp}
    } 
{
    
}

// will run on timer interrupt
void StateTable::next(){
    if(_isButtonPressed){
        _current = _stateTable[_current.buttonState];
    } else {
        _current = _stateTable[_current.interuptState];
    }
}

// will be executed on button interrupt
void StateTable::setButton(){
    if (_isButtonPressed == false){
        _isButtonPressed = true;
    }
}

tState StateTable::getCurrent(){
    return _current.thisState;
}