//#include <cstdio>
#include <stdio.h>
#include <mbed.h>
#include "BufferedSerial.h"
#include "ThisThread.h"
#include <ByteBuffer.h>
#include "Thermistor.h"

BufferedSerial bs(D1,D0,9600);
Thermistor th(A0);
// main() runs in its own thread in the OS
int main()
{
    bs.set_format(
    /* bits */ 8,
    /* parity */ BufferedSerial::None,
    /* stop bit */ 1
    );
    printf("start init...");
    // Restore all value to factory setup
    bs.write("AT+DEFAULT",strlen("AT+DEFAULT"));
    ThisThread::sleep_for(2000ms);
    // set the bluetooth name as "SeeedMaster" ,the length of bluetooth name
    bs.write("AT+NAMESeeedMaster",strlen("AT+NAMESeeedMaster"));
    ThisThread::sleep_for(400ms);
    // set the bluetooth work in master mode
    //bs.write("AT+ROLEM",strlen("AT+ROLEM"));
    //ThisThread::sleep_for(400ms);
    //bs.write("AT+AUTH1",strlen("AT+AUTH1"));
    //ThisThread::sleep_for(400ms);
    // Clear connected device mac address
    printf("end init...");
    char testSettings[] = "AT+CLEAR";
    int buffer[2]= {2100144,24753};
    double buf[2] = {'0'};
    bs.write(testSettings, sizeof(testSettings));
    ThisThread::sleep_for(600ms);
    while (true) {
        double temp = th.read();
        buf[0]=temp;
        //temp = std::round(temp * 100.0) / 100.0;
        //int len = snprintf(buf, sizeof(buf), "tmp=%.2f \t", temp);
        bs.write(buffer, sizeof(buffer));
        ThisThread::sleep_for(1000ms);
    }
}