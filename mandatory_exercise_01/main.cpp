/*
    Author: Dávid, Justus
    Date: 12.11.25
*/
#include "AnalogIn.h"
#include "DigitalOut.h"
#include "InterruptIn.h"
#include "PinNames.h"
#include "mbed.h"
#include "StateTable.h"
#include <cstdio>
#include "Thermistor.h"
#include "SoundSensor.h"
#include "LightSensor.h"

DigitalOut tempLed(LED1);
DigitalOut lightLed(LED2);
DigitalOut soundLed(LED3);

// The solution is very bad we should change it later
// variables for calculate igf values are increasing or decreasing
int delta_check_counter = 0;
float comp_temp;
float comp_light;
float comp_sound;
bool isTempIncreasing = true;
bool isLightIncreasing = true;
bool isSoundIncreasing = true;

AnalogIn potentiometer(A2);

InterruptIn button(D4);
Ticker timer_interrupt;
StateTable(controlFlow);

Thermistor myThermistor(A0);
LightSensor myLightSensor(A1);
SoundSensor mySoundSensor(A3);

float min_temp = 20;
float max_temp = 25;
const float min_mid_temp = 20;
const float max_mid_temp = 30;

float min_light = 300;
float max_light = 500;
const float min_mid_light = 300;
const float max_mid_light = 500;

float sound_barier = 50;
const float mid_barrier = 80;

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

void setTempLow();
void setTempHigh();
void setLightLow();
void setLightHigh();
void setSoundBarrier();

typedef void (*ActionFunc)();

ActionFunc actions[] = {
    runThermistor, runLightSensor, runSoundSensor, 
    setTempLow, setTempHigh, setLightLow, setLightHigh, setSoundBarrier
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

// TODO: make this number flexible
            if (nState < 8){
            actions[nState]();
            }
        }
    }
}

void display_delta(){
    if(isTempIncreasing){
            tempLed = 1;
    }else {
        tempLed = !tempLed;
    }
    if(isLightIncreasing){
            lightLed = 1;
    }else {
        lightLed = !lightLed;
    }
    if(isSoundIncreasing){
            soundLed = 1;
    }else {
        soundLed = !soundLed;
    }
}


void runThermistor(){
    display_delta();
    float temp = myThermistor.read();
    bool goodTempreture = min_temp <= temp && temp <= max_temp;
    if (delta_check_counter == 0){
        if(comp_temp < temp){
            isTempIncreasing = true;
        }else {
            isTempIncreasing = false;
        }

        comp_temp = temp;

    }

    printf("current tempreture: %.1f C Awarness: %d\n", temp, goodTempreture);
}

void runLightSensor(){
    display_delta();
    
    float light = myLightSensor.read();
    bool goodLight = min_light <= light && light <= max_light;
    if (delta_check_counter == 0){
        if(comp_light < light){
            isLightIncreasing = true;
        }else {
            isLightIncreasing = false;
        }

        comp_light = light;

    }

    printf("current light: %.0f lux Awarness: %d\n", light, goodLight);
}

void runSoundSensor(){
    display_delta();
    float sound = mySoundSensor.read();
    bool goodSound = sound <= sound_barier;
    printf("current noise: %.0f db Awarness: %d\n", sound, goodSound);
    if (delta_check_counter == 0){
        if(comp_sound < sound){
            isSoundIncreasing = true;
        }else {
            isSoundIncreasing = false;
        }

        comp_sound = sound;

    }

    delta_check_counter++;
    delta_check_counter = delta_check_counter % 5;
}

void setTempLow(){
    float factor = potentiometer.read();

    min_temp = min_mid_temp-5 + 10 * factor;
    printf("min_temp: %.0f\n",min_temp);
}

// 25-35 degree are quite "warm" (sometimes (maybe))
// we should reevaluate about it  
void setTempHigh(){
    float factor = potentiometer.read();

    max_temp = max_mid_temp-5 + 10 * factor;
    printf("max_temp: %.0f\n",max_temp);
}

void setLightLow(){
    float factor = potentiometer.read();

    min_light = min_mid_light-100 + 200 * factor;
    printf("min_light: %.0f\n", min_light);
}

void setLightHigh(){
    float factor = potentiometer.read();

    max_light = max_mid_light-100 + 200 * factor;
    printf("max_light: %.0f\n", max_light);
}

void setSoundBarrier(){
    float factor = potentiometer.read();

    sound_barier = mid_barrier - 40 + 80 * factor;
    printf("sound_barrier: %.0f\n", sound_barier);
}




