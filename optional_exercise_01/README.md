# Thermistor Libary
This exercise is about developing a small libary for a thermistor.
It will support celsius and kelvin output as well. 

## Documentation

### Constructor
####Thermistor(PinName pin)

Example:
```cpp
myThermistor = Thermistor(A0)
```
### Methods

####float read()

Example:
```cpp
temprature = myThermistor.read()
```
####void ioctl_setVoltage()

####void ioctl_defaultDelay(int defaultDelay)

####void ioctl_enableKelvin(bool enableKelvin)

