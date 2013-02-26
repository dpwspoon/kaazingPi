kaazingPi
=========

Kaazing Websocket Gateway integrated with the RaspberryPi

Projects:

<h2>PiClient</h2> (https://github.com/dpwspoon/kaazingPi/tree/master/PiClient)

The project is a JMS Client created using Java Kaazing JMS Websocket Client library. The client runs on the Raspberry Pi.
The client sends the status of the led connected to the RaspberryPi to the Kaazing JMS Gateway. The client sends the status to 
the topic "/topic/status". Any consumer connected to the same Kaasing JMS Gateway will be able to retrieve the status of 
the led from any client anywhere in the world as long as it can connect to the gateway.


