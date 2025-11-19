/*
    Author: Dávid, Justus
    Date: 19.11.25
    info: outsource controll of all sensors 
*/
#ifndef SENSOR_CONTROLLER_H
#define SENSOR_CONTROLLER_H


class SensorController{
    public:
        SensorController();
        float runSensor(int sensor_nb);
};


#endif