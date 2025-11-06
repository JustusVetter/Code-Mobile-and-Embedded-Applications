#include "mbed.h"
#include <string>
#include <cstdio>
#include "LightSensor.h"

#include "ThisThread.h"

void test_lightsensor();

// main() runs in its own thread in the OS
int main()
{   
    test_lightsensor();
}

void test_lightsensor(){
    LightSensor myLightSensor(A0);
    while(true){
        ThisThread::sleep_for(950ms);
        myLightSensor.ioctl_kilolux(false);
        float read = myLightSensor.read();
        printf("Standard lux: %.0f \n", read);
        myLightSensor.ioctl_kilolux(true);
        read = myLightSensor.read();
        printf("Kilolux: %.3f \n", read);
    }
}

