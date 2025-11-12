#include "InterruptIn.h"
#include "PinNames.h"
#include "mbed.h"
#include "StateTable.h"
#include <cstdio>
#include "Thermistor.h"
#include "SoundSensor.h"
#include "LightSensor.h"

InterruptIn button(D4);
Ticker timer_interrupt;
StateTable(controlFlow);

Thermistor myThermistor(A0);
LightSensor myLightSensor(A1);
SoundSensor mySoundSensor(A3);


bool timer = false;
bool buttonPressed = false;

void buttonISR(){
    controlFlow.setButton();
    buttonPressed = true;
}

void timerISR(){
    
    timer = true;
    
    
}

void runThermistor();
void runLightSensor();
void runSoundSensor();

typedef void (*ActionFunc)();

ActionFunc actions[] = {
    runThermistor, runLightSensor, runSoundSensor
};


// main() runs in its own thread in the OS
int main()
{
    button.rise(&buttonISR);

    // set timer on 1 second => user will have to wait 1 second until the system react (maybe to much)
    timer_interrupt.attach(&timerISR, 1s);

    while (true) {
        if(buttonPressed ==true){
            buttonPressed = false;
            printf("Gilgamesh kills humbaba\n");
        }
        if (timer == true) {
            timer=false;
            int pState = controlFlow.getCurrent();
            controlFlow.next();
            int nState = controlFlow.getCurrent();
            printf("%d -> %d\n",pState,nState);

            if (nState < 3){
            actions[nState]();
            }
        }
    }
}

void runThermistor(){
    float temp = myThermistor.read();
    bool goodTempreture = 20 < temp && temp < 25;
    
    printf("current tempreture: %.1f C Awarness: %d\n", temp, goodTempreture);
}

void runLightSensor(){
    float light = myLightSensor.read();
    bool goodLight = 300 < light && light < 500;
    
    printf("current light: %.0f lux Awarness: %d\n", light, goodLight);
}

void runSoundSensor(){
    float sound = mySoundSensor.read();
    bool goodSound = sound < 50.0f;
    printf("current noise: %.0f db Awarness: %d\n", sound, goodSound);
}