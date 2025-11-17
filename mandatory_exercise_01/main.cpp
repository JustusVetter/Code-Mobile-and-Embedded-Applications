/*
    Author: Dávid, Justus
    Date: 12.11.25

    Notes:
    There are several code smells and a bad design. 
    It would have been better if we would have get all the date once and then display one by one.
    
*/
#include "AnalogIn.h"
#include "DigitalOut.h"
#include "InterruptIn.h"
#include "PinNames.h"
#include "ThisThread.h"
#include "mbed.h"
#include "StateTable.h"
#include <cstdio>
#include "Thermistor.h"
#include "SoundSensor.h"
#include "LightSensor.h"
#include "Grove_LCD_RGB_Backlight.h"
#include "BoardLEDs.h"

DigitalOut tempLed(LED1);
DigitalOut lightLed(LED2);
DigitalOut soundLed(LED3);

Grove_LCD_RGB_Backlight display(D14,D15);
int display_change_counter = 0;

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

void buttonISR(){
    controlFlow.setButton();
    display_change_counter = 0;
}

void timerISR(){
    timer = true;
}

void runThermistor();
void runLightSensor();
void runSoundSensor();

void output(char sentence[], float number);

typedef void (*ActionFunc)();

void setBarrier(float *barrier, const float *mid_barrier, int change_range, char output_sentence[]){
    float factor = potentiometer.read();
    *barrier = *mid_barrier - change_range + (change_range*2) * factor;
    output(output_sentence, *barrier);
}

ActionFunc actions[] = {
    runThermistor, runLightSensor, runSoundSensor
};

// main() runs in its own thread in the OS
int main()
{
    //INIT
    button.rise(&buttonISR);
    timer_interrupt.attach(&timerISR, 500ms);

    // MAIN LOOP
    while (true) {
        if (timer == true) {
            timer=false;
            int pState = controlFlow.getCurrent();
            controlFlow.next();
            int nState = controlFlow.getCurrent();
            printf("%d -> %d\n",pState,nState);

            if (nState < 3) {
                actions[nState]();
            }else{
                switch (nState) {
                case 3:
                    setBarrier(&min_temp, &min_mid_temp, 5, "min temp:");
                break;
                case 4:
                    setBarrier(&max_temp, &max_mid_temp, 5, "max temp:");
                break;
                case 5:
                    setBarrier(&min_light, &min_mid_light, 100, "min light:");
                break;
                case 6:
                    setBarrier(&max_light, &max_mid_light, 100, "max light:");
                break;
                case 7:
                    setBarrier(&sound_barier, &mid_barrier, 40, "sound barr:");
                break;
                }
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


void output(char sentence[], float number){
    
        display.clear();
        ThisThread::sleep_for(2ms);
        char buffer[16];
        display.print(sentence);
        display.writech(' ');
        sprintf(buffer, "%.0f", number);
        display.print(buffer);
    
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
    char sentence[] = "temp:";
    if(display_change_counter==0){
    output(sentence,temp);
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
   
    char sentence[] = "light:";
    if(display_change_counter==1){
    output(sentence,light);
    }
    printf("current light: %.0f lux Awarness: %d\n", light, goodLight);
}

void blub();

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
    if(display_change_counter==2){
    char sentence[] = "sound:";
    output(sentence,sound);
    }
    blub();
}

/*void runMesureAndDisplay(){
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
    if(display_change_counter==2){
    char sentence[] = "sound:";
    output(sentence,sound);
    }
}*/

void blub(){
    delta_check_counter++;
    delta_check_counter = delta_check_counter % 4;

// There is a much better solution but „keep it simple stupid“
    display_change_counter++;
    display_change_counter = display_change_counter % 4;
}
