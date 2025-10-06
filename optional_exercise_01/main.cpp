#include "mbed-os/mbed.h"
#include <cstdio>
#include <string>
#include "Thermistor.h"

AnalogIn pin(A0);

void test_thermistor();


int main()
{
    test_thermistor();
}

void test_thermistor(){

    Thermistor my_thermistor(A1);
    printf("Celsius, 3V: %.2f \n", my_thermistor.read());

    my_thermistor.ioctl_enableKelvin(true);
    printf("Kelvin, 3V: %.2f \n\n",my_thermistor.read());
    

    my_thermistor.ioctl_setVoltage(5.5);
    my_thermistor.ioctl_enableKelvin(false);
    printf("Celsius, 5.5 V: %.2f \n", my_thermistor.read());

    my_thermistor.ioctl_enableKelvin(true);
    printf("Kelvin, 5.5 V: %.2f \n\n", my_thermistor.read());

    //Reset
    my_thermistor.ioctl_setVoltage(3);
    my_thermistor.ioctl_enableKelvin(false);
    printf("standard settings: %.2f \n", my_thermistor.read());

}

