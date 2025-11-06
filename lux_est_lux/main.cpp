#include "mbed.h"
#include <string>
#include <cstdio>

AnalogIn in = AnalogIn(A0);
float vRef = 3.3;
float luxRel = 500;
float rL = 10000;
float vOut = 1.0;

// main() runs in its own thread in the OS
int main()
{   
    while (true) {
        float inward = in.read();
        ThisThread::sleep_for(1s);
        printf("%.2f \n", inward);
        float first = (vRef * luxRel) * (inward * 3.3);
        printf("First: %.2f \n", first);
        float second = first - luxRel;
        printf("Second: %.2f \n", second);
        float kLux = second/rL;
        printf("K_Lux: %.2f \n", kLux);
        float lux = kLux * 1000;
        printf("Lux: %.2f \n", lux);
    }
}

