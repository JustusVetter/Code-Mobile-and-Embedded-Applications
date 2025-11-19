/*
Author: Dávid, Justus
Date: 12.11.25
*/

#include "StateTable.h"
#include "mbed.h"


StateTable::StateTable(): 
_stateTable {
        {display_temp,display_Light,set_Temp_L, "temp:"},
        {display_Light,display_Sound,set_Temp_L, "light:"},
        {display_Sound,display_temp,set_Temp_L, "sound:"},
        {set_Temp_L,set_Temp_L,set_Temp_H, "min temp:"},
        {set_Temp_H,set_Temp_H,set_Light_L, "max temp:"},
        {set_Light_L,set_Light_L,set_Light_H, "min light:"},
        {set_Light_H,set_Light_H,set_Sound, "max light:"},
        {set_Sound,set_Sound,display_temp, "sound barr:"}
    } 
{
    
}

// will run on timer interrupt
void StateTable::next(){
    if(_isButtonPressed){
        _isButtonPressed = false;
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

char* StateTable::getSentence(){
    return _current.sentence;
}

tState StateTable::getCurrent(){
    return _current.thisState;
}