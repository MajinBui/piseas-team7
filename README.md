# PiSeas - Team 07
_Currently no longer being worked on_

PiSeas is a prototype for an automated fish tank, created with the incorporation of the Raspberry Pi. The Raspberry Pi will be programmed to control all the use cases and tasks required to sustain a habitable fish tank environment such as feeding, temperature control, water changing, lighting and water analysis. The data obtained from the built-in sensors within the tank is made available on the user’s phone, and accessed through an app which we will also design and create. The app is able to connect to multiple fish tanks and will be able to monitor and control each individual tank in real time. The user is able to access real time tank information as well as schedule tasks for the system to maintain an optimal aquatic habitat. 

[Requirements documentation](./BTS530 Documentation.docx)

## Project structure
The project was large in scale and was divided 3 parts: the Android App, the Networking Server and PiSeas(raspberry pi device).  The android app was created to allow the user to control and set the settings for the Piseas device remotely by communicating an internet network.  The app would contact the server which stored XML data that contained the settings of the android device(s) and the Piseas device.  The Piseas would be the device to analysis and perform all the intended actions requested by the user.   

### Android App (PiSeas-AndroidApp)
The Android app is used to communicate to the Piseas device.  Any and all actions are saved into an xml and then sent to the server to be processed for the Piseas device.  The user is able to communicate to the Piseas device as long as there is a network connection on all devices.   

### PiSeas-Network (PiSeas-Network and PiSeas-Network-CPlusPlusClient)
The server was built with Java and parsed xml files and stored it for later usage by any other mobile devices and the Piseas device.  The server acts as a mailbox where it would await for the any device to pick up the most recent xml data.  

The server was also wrapped with the Jave jni interface to allow a C++ version so the Piseas device(built in C++) could communicate with the server without recreating the client.   

### PiSeas-PiDevice (PiSeas-AndroidApp)
The Piseas device was built in C++ to natively use the GPIO pins along with the raspberry pi shield.  The Pidevice would tick every 20-60 seconds to retrieve new xml data, and then act on any tasks that may need to be done.  The PiSeas device ensures all the user's settings are used and monitored.    

## Prototype designs
![Application view](https://github.com/MajinBui/piseas-Team07/tree/master/images/App-view.png "App View")   
![Hardware design](https://github.com/MajinBui/piseas-Team07/tree/master/hardware-images/Overview.jpg "Prototype design")   
![Hardware diagram](https://github.com/MajinBui/piseas-Team07/tree/master/hardware-images/hardware-dg.JPG "Prototype design")   
