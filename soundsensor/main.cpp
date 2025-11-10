#include "AnalogIn.h"
#include "ThisThread.h"
#include "mbed.h"
#include <cmath>


// main() runs in its own thread in the OS

AnalogIn in(A1);

int main()
{
    while (true) {
        
        ThisThread::sleep_for(1s);
        
        float spl = 0;
        for (int i = 0; i <100; i++) {
            float input = in.read();
            float input_volt = 5.0f * input;
            float nspl = (94 + (20 * log10f(input_volt / 0.0316f)));
            if(nspl > 0){
            spl = spl + nspl;
            //ThisThread::sleep_for();
            }
        }

        spl = spl / 100.0f;
        printf("spl: %0.2f\n",spl);
    }
}

