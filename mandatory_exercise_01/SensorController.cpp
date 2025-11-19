
#include "SensorController.h"

#include "Thermistor.h"
#include "SoundSensor.h"
#include "LightSensor.h"



Thermistor myThermistor(A0);
LightSensor myLightSensor(A1);
SoundSensor mySoundSensor(A3);


SensorController::SensorController(){

}

float SensorController::runSensor(int sensor_nb){
    float result = 0;
    

    switch (sensor_nb) {
    case 0:
        result = myThermistor.read();
    break;

    case 1:
        result = myLightSensor.read();
    break;

    case 2:
        result = mySoundSensor.read();
    break;
    
    }
    return result;
}
