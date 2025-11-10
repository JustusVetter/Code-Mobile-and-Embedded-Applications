#include "AnalogIn.h"
#include "ThisThread.h"
#include "mbed.h"
#include <cmath>


// main() runs in its own thread in the OS
#include "SoundSensor.h"
#include "mbed.h"
#include <string>
#include <cstdio>

#include "ThisThread.h"

SoundSensor sound(A0);
int main()
{
    while (true) {
        
        ThisThread::sleep_for(1s);
        
        float spl = sound.read();
        printf("spl: %0.2f\n",spl);
    }
}

