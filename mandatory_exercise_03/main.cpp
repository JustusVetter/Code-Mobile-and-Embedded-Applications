//#include <cstdio>
#include <stdio.h>
#include <mbed.h>
#include "BufferedSerial.h"
#include "ThisThread.h"
#include <ByteBuffer.h>
#include "Thermistor.h"
#include "SeeedBluetooth.h"

SeeedBluetooth bt(D1,D0,9600);
Thermistor th(A0);
// main() runs in its own thread in the OS
int main()
{
    while (true) {
        double temp = th.read();
        char msg[20];
        snprintf(msg, sizeof(msg), "<tmp>%.2f</tmp>", temp);
        bt.write(msg);
        ThisThread::sleep_for(250ms);
    }
}