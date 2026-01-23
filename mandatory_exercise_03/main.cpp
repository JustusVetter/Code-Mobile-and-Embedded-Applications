#include <mbed.h>
#include "BufferedSerial.h"
#include "ThisThread.h"
#include <ByteBuffer.h>
BufferedSerial bs(D1,D0,9600);
// main() runs in its own thread in the OS
int main()
{
bs.set_format(
/* bits */ 8,
/* parity */ BufferedSerial::None,
/* stop bit */ 1
);
while (true) {
double buf[2] = {0};
bs.write(buf, sizeof(buf));
ThisThread::sleep_for(1000ms);
}
}