kaazingPi
=========

Kaazing JMS Websocket Gateway integrated with the RaspberryPi

Projects:

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


