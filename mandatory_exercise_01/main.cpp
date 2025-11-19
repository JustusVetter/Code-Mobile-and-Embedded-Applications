/*
    Author: Dávid, Justus
    Date: 12.11.25
*/
#include "AnalogIn.h"
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
#include "SensorController.h"


Grove_LCD_RGB_Backlight display(D14,D15);

AnalogIn potentiometer(A2);

BoardLEDs leds;

InterruptIn button(D4);
Ticker timer_interrupt;
StateTable controlFlow;

SensorController(mySensorController);

int menue_array[]{
    20,20, 5,
    25,30, 5,
    300,300, 100,
    500,500, 100,
    50,80, 40
};

bool timer = false;

void buttonISR(){controlFlow.setButton();}

void timerISR(){timer = true;}

void output(char sentence[], float number);

void setBarrier(int * barrier, const int *mid_barrier, const int *change_range, char output_sentence[]){
    float factor = potentiometer.read();
    *barrier = *mid_barrier - *change_range + (*change_range * 2) * factor;
    float out_flt = *barrier;
    output(output_sentence, out_flt);
}

int main()
{
    //INIT
    button.rise(&buttonISR);
    timer_interrupt.attach(&timerISR, 2s);
    
    // MAIN LOOP
    while (true) {
        if (timer == true) {
            
            timer=false;
            controlFlow.next();
            int nState = controlFlow.getCurrent();
            
            if (nState < 3) { // State associated with output
                timer_interrupt.attach(&timerISR, 2s);
                float value = mySensorController.runSensor(nState);
                int pattern_creator = 0;

                if(nState<2){
                    if (value > menue_array[nState*6+3] ) {
                        pattern_creator = 3;
                        display.setRGB(0, 0, 255 );
                    
                    } else if (value < menue_array[nState*6] ) {
                        pattern_creator = 6;
                        display.setRGB(255, 0, 0);
                    }else{
                        display.setRGB(0, 0, 0);
                    }
                } else {
                    
                    if (value > menue_array[nState*6]) {
                        pattern_creator = 3;
                        display.setRGB(0, 0, 255);
                    }else{
                        display.setRGB(0, 0, 0);
                    }
                }
                leds.write_pattern(nState+pattern_creator);
                output(controlFlow.getSentence(), value);
                
                
            } else{ //State associated with menue
                timer_interrupt.attach(&timerISR,500ms);
                controlFlow.next();
                int cr_state = controlFlow.getCurrent();
                int state = cr_state-3;
                setBarrier(&menue_array[state*3], &menue_array[state*3+1], &menue_array[state*3+2], controlFlow.getSentence());
            }

        }
        
    }
 }


void output(char sentence[], float number){
        display.clear();
        ThisThread::sleep_for(2ms);
        char buffer[16];
        display.print(sentence);
        sprintf(buffer, " %.0f", number);
        display.print(buffer);
}
