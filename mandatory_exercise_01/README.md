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
#### Measures
The LCD Display is displaying the value measured by the three sensors one after another. The different values will be displayed as an integer numbers. The  

Furthermore it is displaying if the at the moment presented value needs to be changed and in which direction it needs to be changed. This is done by the color of the background of the LCD Display. The colors and their meaning corresponding the different measurement variables are displayed in the following table.
|variable|color|(decrease/increase)|
|--------|-----|-------------------|
|temp|red|decrease|
|temp|blue|increase|
|light|red|decrease|
|light|blue|increase|
|sound|red|decrease|
#### Setting Menue
### LEDs
Beside of the backlight of the LCD Display the buttons are also providing the user with the information how the temprature, light and the sound should be changed to keep a comfortable environment in the room the system is monitoring.

### Button & Potentiometer

## Hardware Specifications
## Software Docuentation 
