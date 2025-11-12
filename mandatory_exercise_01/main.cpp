#include "InterruptIn.h"
#include "PinNames.h"
#include "mbed.h"
#include "StateTable.h"
#include <cstdio>

InterruptIn button(BUTTON1);
Ticker timer_interrupt;
StateTable controlFlow;

void buttonISR(){
    controlFlow.setButton();
}

void timerISR(){
    int pState = controlFlow.getCurrent();

    controlFlow.next();

    int nState = controlFlow.getCurrent();
    printf("%d -> %d", pState, nState);
}

// main() runs in its own thread in the OS
int main()
{
    button.rise(&buttonISR);

    // set timer on 1 second => user will have to wait 1 second until the system react (maybe to much)
    timer_interrupt.attach(&timerISR, 1s);

    while (true) {
        
    }
}

