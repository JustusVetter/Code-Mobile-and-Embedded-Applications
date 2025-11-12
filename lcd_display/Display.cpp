#include "Display.h"

Display::Display(PinName sda, PinName scl) : lcd(sda, scl){
    
}

void Display::setRGB(char r, char g, char b) {
    lcd.setRGB(r, g, b);
}

void Display::clear(){
    lcd.clear();
}

void Display::write(char data1){
    lcd.write(data1);
}

void Display::writech(char data2){
    lcd.writech(data2);
}

void Display::print(char *str){
    lcd.print(*str);
}

void Display::locate(int row, int col){
    lcd.locate(raw, col);
}