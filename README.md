<h2>kaazingPi</h2> 

Kaazing JMS Websocket Gateway integrated with the RaspberryPi

=========

<h5>Projects</h5> 

RCCar: Remote controlled car using Kaazing JMS websocket server, Raspberry Pi, a Remote controlled car, and a smart phone

PiClient: Controlling an LED using Kaazing JMS websocket server, Raspberry Pi, and any browser

<h5> Documentation/Reference </h5>

For starting out see LightAndToggleSwitch below

http://tech.kaazing.com/documentation/jms/

http://pi4j.com/

<h2>LightAndToggleSwitch</h2> (https://github.com/dpwspoon/kaazingPi/tree/master/LightAndToggleSwitch)

This is a simple project that will connect a RaspberryPi with a simple toggleSwitch, to another RaspberryPi with a light.  

This project and the IoT architecture that it demonstrates will be presented at JavaOne 2013.  A link to the presentation will be included after the conference. 

Getting started:
1) Set up the RaspberryPi with Pi4J and Java (see http://pi4j.com for help)
2) On your development computer (ie not the pi) checkout the LightAndToggleSwitch project.
3) Import the project into eclipse
4) Depending on your version of eclipse, the required library folders may not be included.  If this is the case you should see numerous errors.  To include the libraries edit the BuildPath to include the jars in LightAndToggleSwitch/lib/.  Note: Having all the jars included will not hurt anything but only a small subset is truely required for each individual project you can build.

5) There are 6 class files with main methods in them that you can run on the pi.  Each requires a specific setup.  The simplist is the Light.java.  To run Light.java on the pi do the following.
    a) go to Light.java and run as an Application  (you should see errors saying no Gpio)
    b) from eclipse go to export, Java, runnable jars
    c) select Light.java in the drop down
    d) select Copy required libraries into a sub-folder ...
    e) select finish
    f) ftp the exported files onto the pi 
        (tar -cvf outputDirectory.tar outputDirectory; scp outputDirectory.tar Pi@xxx.xxx.xxx.xxx: 
    g) from the pi, extract the file (tar -xvf outputDirectory.tar)
    h) run the project (cd outputDirectory; sudo java -jar nameOfJar.jar)A
    NOTE: This project assumes you have a light connected to GPIO pin 1 (via Pi4J numbering scheme).  You can see this by connecting an led to that spot.

6) You can repeat step 5 for the various other classes:
    Light, Toggle, LightJmsTcpController, LightJmsWsController, ToggleJmsTcpListener, ToggleJmsWsListener

    The setup for each project is as follows

    a) Light: light or led connected to pin 1
    b) Toggle: Switch connected to pin 2
    c) LightJmsTcpController (meant to be ran on one pi while ToggleJmsTcpListener is running on the other)
        - led connected to pin 1
        - ActiveMq running a server/development machine under the hostname listed in src/main/com/kaazing/demo/util/AbstractJmsMessenger.java
        - set the etc/hosts on the pi to point the ActiveMq ip / hostname
    d) ToggleJmsTcpListener (meant to be ran on one pi while LightJmsTcpController is running on the other)
        - switch connected to pin 0 
        - ActiveMq running a server/development machine under the hostname listed in src/main/com/kaazing/demo/util/AbstractJmsMessenger.java
        - set the etc/hosts on the pi to point the ActiveMq ip / hostname
    e) LightJmsWsController (meant to be ran on one pi while ToggleJmsWsListener is running on the other)
        - led connected to pin 1
        - ActiveMq running a server/development machine under the hostname listed in src/main/com/kaazing/demo/util/AbstractJmsMessenger.java
        - Kaazing JMS Websocket Gateway  running a server/development machine under the hostname listed in src/main/com/kaazing/demo/util/AbstractJmsMessenger.java.  (If you download the JMS Websocket Gateway you will need to set the Gateway.Hostname to the same hostname in the $GATEWAY_HOME/conf/gateway-config.xml)
        - set the etc/hosts on the pi to point the Gateway's ip / hostname
    f) ToggleJmsTcpListener (meant to be ran on one pi while LightJmsTcpController is running on the other)
        - switch connected to pin 0 
        - ActiveMq running a server/development machine under the hostname listed in src/main/com/kaazing/demo/util/AbstractJmsMessenger.java
        - Kaazing JMS Websocket Gateway  running a server/development machine under the hostname listed in src/main/com/kaazing/demo/util/AbstractJmsMessenger.java.  (If you download the JMS Websocket Gateway you will need to set the Gateway.Hostname to the same hostname in the $GATEWAY_HOME/conf/gateway-config.xml)
        - set the etc/hosts on the pi to point the Gateway's ip / hostname



<h2>RCCar</h2> (https://github.com/dpwspoon/kaazingPi/tree/master/RCCar)

An extension of the technology used in PiClient to control an RC car from any browser on any smart phone.   

Video Demo: To Come

Blog Post: To Come


<h2>PiClient</h2> (https://github.com/dpwspoon/kaazingPi/tree/master/PiClient)

Video Demo http://www.youtube.com/watch?feature=player_embedded&v=bLF7QwHOvRY

The project is a JMS Client created using Java Kaazing JMS Websocket Client library. The client runs on the Raspberry Pi (Wheezy).
The client sends the status of the LED connected to the RaspberryPi to the Kaazing JMS Gateway (We control the GPIO thanks to the awesome Pi4J Library). The client sends the status to 
the topic "/topic/status". Any consumer connected to the same Kaasing JMS Gateway will be able to retrieve the status of 
the LED from any client anywhere in the world (in realtime, thanks to websockets). 

To setup the raspberry pi we followed the Pi4J instructions http://pi4j.com/
To setup the kaazing websocket gateway, you can run against http://demo.kaazing.com/demo/jms/javascript/jms-javascript.html
or follow the documentation @ http://tech.kaazing.com/documentation/  

If you have any questions please feel free to post an issue. 

Future Goals:
Add client interface,
Add more Pi4J Demos,
Add server sent events demo,
Add raspberry pi as gateway demo,
Add clusters,
Add more.... (stay tuned)

enjoy ;-)


=========
=========
<h2>NOTE</h2>

All code and thoughts posted here are independent of Kaazing Corporation,  Source code maybe compiled/changed/editted and used independently by Kaazing or others for there own use
