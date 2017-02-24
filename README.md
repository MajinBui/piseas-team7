# PiSeas - Team 07

PiSeas is a prototype for an automated fish tank, created with the incorporation of the Raspberry Pi. The Raspberry Pi will be programmed to control all the use cases and tasks required to sustain a habitable fish tank environment. 

Some functionalities of the tank include scheduled feedings, water temperature control, water changing, camera monitoring, light control, and water analysis. The data obtained from the built-in sensors within the tank is made available on the user’s phone, and accessed through an app which we will also design and create. The app is able to connect to multiple fish tanks and will be able to monitor and control each individual tank in real time. The user is able to access real time tank information as well as schedule tasks for the system to maintain an optimal aquatic habitat. 

[Requirements documentation](./BTS530 Documentation.docx)

## Changes from the requirements 
A major change from the original requirements document is the connection process has changed from Bluetooth to WiFi. 

## Already Done
* XML parser for phone side  
* XML format done

* Plan and layout for hardware components, such as relay and sensors
* Open Aquarium API Research  

* Created merged app project file, all app updates to be added here: 
[PiSeas APP](./PiSeas-AndroidAPP)  

* Server set up to receive and send XML data
* Server is multi-threaded 
* Programming Pi side (relay, light and fan) 
* Programming Pi side (self regulation logic, light and fan) 
* Programming Pi side (temperature sensor)


## To-Do
* Integrate XML data transfer on phone app
* Integrate XML data transfer on Pi  
* Create XML parser for Pi side using c++ 
* Save relevant XML on Android device through app  

* Programming Pi side (relay, heater and pumps)
* Programming Pi side (sensors, with API)
* Programming Pi side (self regulation logic, heater and pumps)  

* Add missing validation on phone app   
* Create log activity on app
* Turn tank superclass into a service 
* Responsive layouts for all android devices 
* Additional graphics and UI/UX components

* Wrap FishyClient into C++ wrapper
