#include <mbed.h>
#include "BufferedSerial.h"
#include "ThisThread.h"
#include <ByteBuffer.h>
#include "Thermistor.h"

BufferedSerial bs(D1,D0,9600);
Thermistor th(A0);
// main() runs in its own thread in the OS
int main()
{
    bs.set_format(
    /* bits */ 8,
    /* parity */ BufferedSerial::None,
    /* stop bit */ 1
    );
    double buf[2] = {'0'};
    while (true) {
        double temp = th.read();
        buf[0]=temp;
        //temp = std::round(temp * 100.0) / 100.0;
        //int len = snprintf(buf, sizeof(buf), "tmp=%.2f \t", temp);
        bs.write(buf, sizeof(buf));
        ThisThread::sleep_for(1000ms);
    }
}