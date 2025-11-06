#include "mbed-os/mbed.h"
#include <cstdio>
#include <string>
#include "Thermistor.h"

//AnalogIn pin(A0);

void test_thermistor();


int main()
{
    test_thermistor();
}

void test_thermistor(){

    while(1){
    Thermistor my_thermistor(A0);
    printf("Celsius, 3V: %.2f \n", my_thermistor.read());

    my_thermistor.ioctl_enableKelvin(true);
    printf("Kelvin, 3V: %.2f \n\n",my_thermistor.read());
    
    //Reset
    my_thermistor.ioctl_enableKelvin(false);
    printf("standard settings: %.2f \n", my_thermistor.read());
    }
}

