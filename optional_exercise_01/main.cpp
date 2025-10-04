#include "mbed.h"
#include <cstdio>
#include <cmath>

AnalogIn pin(A0);



// main() runs in its own thread in the OS
int main()
{
    float rb = 3.3;
    int rz = 100000;
    int beta = 4250000;
    
    
    float tz = 298.15;

    ThisThread::sleep_for(500);
    float f =  pin.read();
    printf("Inp: %f \n", f);
    float rTherm = rb * ((1.0 / f) - 1);
    printf("Therm: %f \n", rTherm);
    float tKelvin = 1 / ((log(rTherm/rz)) / beta + (1/tz));
    float celsius = tKelvin - 273.15;
    //printf("normalized: 0x%04X \n", pin.read_u16());
    printf("Kelvin: %f \n", tKelvin );
    printf("Celsius: %f \n", celsius);
}

