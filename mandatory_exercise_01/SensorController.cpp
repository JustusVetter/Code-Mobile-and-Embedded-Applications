#include "Thermistor.h"
#include "SoundSensor.h"
#include "LightSensor.h"

Thermistor myThermistor(A0);
LightSensor myLightSensor(A1);
SoundSensor mySoundSensor(A3);
int delta_check_counter = 0;
float comp_temp;
float comp_light;
float comp_sound;
bool isTempIncreasing = true;
bool isLightIncreasing = true;
bool isSoundIncreasing = true;
float min_light = 300;
float max_light = 500;
float min_temp = 20;
float max_temp = 25;
float sound_barier = 50;

void runSensor(){
    display_delta();
    float temp = myThermistor.read();
    bool goodTempreture = min_temp <= temp && temp <= max_temp;

    float light = myLightSensor.read();
    bool goodLight = min_light <= light && light <= max_light;

    float sound = mySoundSensor.read();
    bool goodSound = sound <= sound_barier;    

    if (delta_check_counter == 0){
        if(comp_temp < temp){
            isTempIncreasing = true;
        }else if(comp_temp >= temp){
            isTempIncreasing = false;
        }
        else if(comp_light < light){
            isLightIncreasing = true;
        }else if(comp_light >= light){
            isLightIncreasing = false;
        }else if(comp_sound < sound){
            isSoundIncreasing = true;
        }else if(comp_sound >= sound){
            isSoundIncreasing = false;
        }
        comp_temp = temp;
        comp_light = light;
        comp_sound = sound;
    }

    if(display_change_counter==2){
        char sentence[] = "sound:";
        output(sentence,sound);
    }else if(display_change_counter==0){
        char sentence[] = "temp:";    
        output(sentence,temp);
    }else if(display_change_counter==1){
        char sentence[] = "light:";    
        output(sentence,light);
    }
    printf("current light: %.0f lux Awarness: %d\n", light, goodLight);
    printf("current tempreture: %.1f C Awarness: %d\n", temp, goodTempreture);
    printf("current noise: %.0f db Awarness: %d\n", sound, goodSound);

    blub();
}

void blub(){
    delta_check_counter++;
    delta_check_counter = delta_check_counter % 4;

// There is a much better solution but „keep it simple stupid“
    display_change_counter++;
    display_change_counter = display_change_counter % 4;
}