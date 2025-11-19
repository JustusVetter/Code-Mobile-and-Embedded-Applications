# Smart Ambient Awareness System
The smart ambient awarness system is an embedded application and system developed with the mbed library in c++. 
It will monitore the light sensitivity in _lux_, the sound in _db_ as well as the temprature in _celsius_. If one of this values will fall out of a specific range the user will be contacted by a light signal. 
Furthermore the user can change the ranges using a potentiometer. Therefore he have to access a setting menue throu pressing a button. The specific change domains are documented below.
## User Interface
The user interface of the embedded system consist of the followin parts:
 - 1x LCD Display
 - 3x LEDs
 - 1x Button
 - 1x Potentiometer

### LCD Display
The LCD Display is the most important part of the user interface of this system. In generally it is communicating two two different domains with the user. Firstly the Measures and the interpretation of this measures, and secondly the setting menue for the ranges of the variables.
#### Measures
The LCD Display is displaying the value measured by the three sensors one after another. The different values will be displayed as an integer numbers and follow a short cut for the variable. The shortcuts and the longform of this shorts cuts are displayed in the table below. The measurements (_db_,_lux_,_C_) will not be displayed beside the numbers because they are all in their standard form and well interpretable by the user.
|long form|short form|
|---------|----------|
|temperature|temp|
|sound|sound|
|light|light| 

Furthermore it is displaying if the at the moment presented value needs to be changed and in which direction it needs to be changed. This is done by the color of the background of the LCD Display. The colors and their meaning corresponding the different measurement variables are displayed in the following table.
|variable|color|(decrease/increase)|
|--------|-----|-------------------|
|temp|red|decrease|
|temp|blue|increase|
|light|red|decrease|
|light|blue|increase|
|sound|red|decrease|
#### Setting Menue
The Setting Menue is displaying the max and min value or the barrier in the case of sound for every measurement. Thesebarriers can be changes in a specific range which was selected by us. The following ranges are possible:

|barrier|range|
|-------|-----|
|min temp|15-25|
|max temp|25-35|
|min light|200-400|
|max light|400-600|
|sound barrier|40-120|
  
### LEDs
```
may need changement
```
Beside of the backlight of the LCD Display the buttons are also providing the user with the information how the temprature, light and the sound should be changed to keep a comfortable environment in the room the system is monitoring.
The following table will help the user to decrypt the meaning of the light signal. The LEDs are represented from left to right as L1,L2,L3 and 0 mean the LED is inactive while 1 mean the LED is active.
|L1|L2|L3|meaning|
|---|---|---|-------|
|0|0|0|temp is comfortable|
|0|0|1|light is comfortable|
|0|1|0|sound is comfortable|
|0|1|1|temp needs decreasing|
|1|0|0|temp needs increasing|
|1|0|1|light needs decreasing|
|1|1|0|light needs increasing|
|1|1|1|sound needs decreasing|

### Button & Potentiometer
The Button and the potentiometer are necessary for interacting with the setting menue.
When the button is pressed  it will change between the different menues for setting the comfort ranges and the main display routine.
While one of the menues for settings is activated the user will be abel to use the potentiometer to change the value in the corresponding range mentioned above. a rotation to left will increase the values while a rotation to right will decrease it. When the button is pressed the setted value will be saved. It is necessary to mention that when the button is pressed the setting Menue is changed and the value in this menue is directly changed according on the angle of the potentiometer. This means that the user might be forced to remember the old values if he want to edit just one of the values because he is forced to set it manually back.
## Hardware Specifications
The Software is written for a arduino nucleon board.
The used hardware consist out of several sensors, an lcd display and the board. The connection of the different peripherals with the different ports on the controller will be displayed in the following table.
|peripheral|port|
|----------|----|
|temp sensor|A0|
|light sensor|A1|
|potentiometer|A2|
|sound sensor|A3|
|button|D4|
|LCD Display|D14-D15|
## Software Documentation 
The programm is following a specific flow. this flow is displayed in the following mermaid graph.

```graph TD
    A[Christmas] -->|Get money| B(Go shopping)
    B --> C{Let me think}
    C -->|One| D[Laptop]
    C -->|Two| E[iPhone]
    C -->|Three| F[fa:fa-car Car]
```

```

File Descriptions:
main.cpp: The entry point of the application, handles the libraries for the different hardware components.
StateTable.(h, cpp): State machine that handles the changing the threshold of the sensors based on button presses using interrupts.
SoundSensor.(h, cpp): Handles the soundsensor. Reads the noise level and implements a function for setting the sensitivity of the sensor.
Thermistor.(h, cpp): Handles the thermistor. Reads and calculates the tempreture and implements a function to switch the temperature between Celsius and Kelvin.
LightSensor.(h, cpp): Handles the lightsensor. Reads and calculates the light level and implements functions to convert the measurment from Lux to KiloLux and to switch the voltage between 3.3 and 5 Volts.
Grove_LCD_RGB_Backlight.(h, cpp): Handles the LCD display, and contains functions such as setting the background color for the display, clearing and writing to the display and locating the cursor on the display.

