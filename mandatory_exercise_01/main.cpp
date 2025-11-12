#include "InterruptIn.h"
#include "PinNames.h"
#include "mbed.h"
#include "StateTable.h"
#include <cstdio>

InterruptIn button(D4);
Ticker timer_interrupt;
StateTable(controlFlow);

bool timer = false;
bool buttonPressed = false;

void buttonISR(){
    controlFlow.setButton();
    buttonPressed = true;
}

void timerISR(){
    
    timer = true;
    
    
}

// main() runs in its own thread in the OS
int main()
{
    button.rise(&buttonISR);

    // set timer on 1 second => user will have to wait 1 second until the system react (maybe to much)
    timer_interrupt.attach(&timerISR, 1s);

    while (true) {
        if(buttonPressed ==true){
            buttonPressed = false;
            printf("Gilgamesh kills hubmaba\n");
        }
        if (timer == true) {
            timer=false;
            int pState = controlFlow.getCurrent();
            controlFlow.next();
            int nState = controlFlow.getCurrent();
            printf("%d -> %d\n",pState,nState);
        }
    }
}

