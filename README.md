<h2>kaazingPi</h2> <br/>

Kaazing JMS Websocket Gateway integrated with the RaspberryPi

=========
<br/>
<h5>Projects</h5> 
LightAndToggleSwitch: If you are getting started with this libraries I would start here.  The code will walk you through interfacing RaspberryPi's with simple hardware (a light or LED and a switch), the connecting them to a JMS message broker, and then running them to the message broker with a websocket gateway (one line of code change).
<br/>
RCCar: Remote controlled car using Kaazing JMS websocket server, Raspberry Pi, a Remote controlled car, and a smart phone
<br/>
PiClient: Controlling an LED using Kaazing JMS websocket server, Raspberry Pi, and any browser
<br/>
<h5> Documentation/Reference </h5>
<br/>
For starting out see LightAndToggleSwitch below
<br/>
http://tech.kaazing.com/documentation/jms/
<br/>
http://pi4j.com/
<br/>
<h2>LightAndToggleSwitch</h2> (https://github.com/dpwspoon/kaazingPi/tree/master/LightAndToggleSwitch)
<br/>
This is a simple project that will connect a RaspberryPi with a simple toggleSwitch, to another RaspberryPi with a light.  <br/>
<br/>
This project and the IoT architecture that it demonstrates will be presented at JavaOne 2013.  A link to the presentation will be included after the conference. 
<br/>
<h6> Getting started: </h6>
1) Set up the RaspberryPi with Pi4J and Java (see http://pi4j.com for help)
<br/>2) On your development computer (ie not the pi) checkout the LightAndToggleSwitch project.
<br/>3) Import the project into eclipse
<br/>4) Depending on your version of eclipse, the required library folders may not be included.  If this is the case you should see numerous errors.  To include the libraries edit the BuildPath to include the jars in LightAndToggleSwitch/lib/.  Note: Having all the jars included will not hurt anything but only a small subset is truely required for each individual project you can build.

<br/>5) There are 6 class files with main methods in them that you can run on the pi.  Each requires a specific setup.  The simplist is the Light.java.  To run Light.java on the pi do the following.
<br/>&nbsp;    a) go to Light.java and run as an Application  (you should see errors saying no Gpio)
<br/>&nbsp;    b) from eclipse go to export, Java, runnable jars
<br/>&nbsp;    c) select Light.java in the drop down
<br/>&nbsp;    d) select Copy required libraries into a sub-folder ...
<br/>&nbsp;    e) select finish
<br/> &nbsp;   f) ftp the exported files onto the pi 
<br/>        (tar -cvf outputDirectory.tar outputDirectory; scp outputDirectory.tar Pi@xxx.xxx.xxx.xxx: 
<br/>&nbsp;    g) from the pi, extract the file (tar -xvf outputDirectory.tar)
<br/>&nbsp;    h) run the project (cd outputDirectory; sudo java -jar nameOfJar.jar)A
<br/>&nbsp;    NOTE: This project assumes you have a light connected to GPIO pin 1 (via Pi4J numbering scheme).  You can see this by connecting an led to that spot.
<br/>
<h6> Running all the classes </h6>
<br/>6) You can repeat step 5 for the various other classes:
<br/>    Light, Toggle, LightJmsTcpController, LightJmsWsController, ToggleJmsTcpListener, ToggleJmsWsListener
<br/>
<br/>    The setup for each project is as follows
<br/>
<br/>&nbsp;    a) Light: light or led connected to pin 1
<br/>&nbsp;    b) Toggle: Switch connected to pin 2
<br/>&nbsp;    c) LightJmsTcpController (meant to be ran on one pi while ToggleJmsTcpListener is running on the other)
<br/>&nbsp;&nbsp;        - led connected to pin 1
<br/>&nbsp;&nbsp;        - ActiveMq running a server/development machine under the hostname listed in <br/>src/main/com/kaazing/demo/util/AbstractJmsMessenger.java
<br/>&nbsp;&nbsp;        - set the etc/hosts on the pi to point the ActiveMq ip / hostname
<br/>&nbsp;    d) ToggleJmsTcpListener (meant to be ran on one pi while LightJmsTcpController is running on the other)
<br/>&nbsp;&nbsp;        - switch connected to pin 0 
<br/>&nbsp;&nbsp;        - ActiveMq running a server/development machine under the hostname listed in <br/>src/main/com/kaazing/demo/util/AbstractJmsMessenger.java
<br/>&nbsp;&nbsp;        - set the etc/hosts on the pi to point the ActiveMq ip / hostname
<br/>&nbsp;    e) LightJmsWsController (meant to be ran on one pi while ToggleJmsWsListener is running on the other)
<br/>&nbsp;&nbsp;        - led connected to pin 1
<br/>&nbsp;&nbsp;        - ActiveMq running a server/development machine under the hostname listed in <br/>src/main/com/kaazing/demo/util/AbstractJmsMessenger.java
<br/>&nbsp;&nbsp;        - Kaazing JMS Websocket Gateway  running a server/development machine under the hostname listed in src/main/com/kaazing/demo/util/AbstractJmsMessenger.java.  (If you download the JMS Websocket Gateway you will need to set the Gateway.Hostname to the same hostname in the $GATEWAY_HOME/conf/gateway-config.xml)
<br/>&nbsp;&nbsp;        - set the etc/hosts on the pi to point the Gateway's ip / hostname
<br/>&nbsp;    f) ToggleJmsTcpListener (meant to be ran on one pi while LightJmsTcpController is running on the other)
<br/>&nbsp;&nbsp;        - switch connected to pin 0 
<br/>&nbsp;&nbsp;        - ActiveMq running a server/development machine under the hostname listed in src/main/com/kaazing/demo/util/AbstractJmsMessenger.java
<br/>&nbsp;&nbsp;        - Kaazing JMS Websocket Gateway  running a server/development machine under the hostname listed in src/main/com/kaazing/demo/util/AbstractJmsMessenger.java.  (If you download the JMS Websocket Gateway you will need to set the Gateway.Hostname to the same hostname in the $GATEWAY_HOME/conf/gateway-config.xml)
<br/>&nbsp;&nbsp;        - set the etc/hosts on the pi to point the Gateway's ip / hostname
<br/>
<br/>
<br/>
<h2>RCCar</h2> (https://github.com/dpwspoon/kaazingPi/tree/master/RCCar)
<br/>
An extension of the technology used in PiClient to control an RC car from any browser on any smart phone.   
<br/>
Video Demo: To Come
<br/>
Blog Post: To Come
<br/>
<br/>
<h2>PiClient</h2> (https://github.com/dpwspoon/kaazingPi/tree/master/PiClient)
<br/>
Video Demo http://www.youtube.com/watch?feature=player_embedded&v=bLF7QwHOvRY
<br/>
The project is a JMS Client created using Java Kaazing JMS Websocket Client library. The client runs on the Raspberry Pi (Wheezy).
<br/>The client sends the status of the LED connected to the RaspberryPi to the Kaazing JMS Gateway (We control the GPIO thanks to the awesome Pi4J Library). The client sends the status to 
the topic "/topic/status". Any consumer connected to the same Kaasing JMS Gateway will be able to retrieve the status of 
the LED from any client anywhere in the world (in realtime, thanks to websockets). 

<br/>To setup the raspberry pi we followed the Pi4J instructions http://pi4j.com/
<br/>To setup the kaazing websocket gateway, you can run against <br/>http://demo.kaazing.com/demo/jms/javascript/jms-javascript.html
<br/>or follow the documentation @ http://tech.kaazing.com/documentation/  

<br/>If you have any questions please feel free to post an issue. 
<br/>
<br/>Future Goals:
<br/>Add client interface,
<br/>Add more Pi4J Demos,
<br/>Add server sent events demo,
<br/>Add raspberry pi as gateway demo,
<br/>Add clusters,
<br/>Add more.... (stay tuned)
<br/>
<br/>enjoy ;-)
<br/>
<br/>
<br/>=========
<br/>=========
<br/><h2>NOTE</h2>
<br/>
<br/>All code and thoughts posted here are independent of Kaazing Corporation,  Source code maybe compiled/changed/editted <br/>and used independently by Kaazing or others for there own use
<br/>
