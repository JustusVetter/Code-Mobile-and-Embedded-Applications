/*
Author: David, Justus
Date: 28.01.2026
*/
#include "SeeedBluetooth.h"
#include "mbed.h"

SeeedBluetooth::SeeedBluetooth(PinName pin0,PinName pin1,int rate):_bs(pin0,pin1,rate) {
    _bs.set_format(
    /* bits */ 8,
    /* parity */ BufferedSerial::None,
    /* stop bit */ 1
    );
    this->_init();
}

void SeeedBluetooth::write(const char* input){
    _bs.write(input,strlen(input));
}

void SeeedBluetooth::_init(){
    this-> write("AT+DEFAULT");
    ThisThread::sleep_for(2000ms);
    this-> write("AT+NAMESeeedMaster");
    ThisThread::sleep_for(400ms);
    this->write("AT+CLEAR");
    ThisThread::sleep_for(600ms);
}